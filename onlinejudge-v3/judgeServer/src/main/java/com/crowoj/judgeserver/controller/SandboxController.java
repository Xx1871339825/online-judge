package com.crowoj.judgeserver.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.crowoj.judgeserver.dto.RunDto;
import com.crowoj.judgeserver.entity.JudgeCase;
import com.crowoj.judgeserver.entity.ProblemCase;
import com.crowoj.judgeserver.enums.*;
import com.crowoj.judgeserver.exception.SimpleException;
import com.crowoj.judgeserver.param.RunParam;
import com.crowoj.judgeserver.service.*;
import com.crowoj.judgeserver.utils.OkHttpUtil;
import com.crowoj.judgeserver.utils.ResultUtil;
import com.crowoj.judgeserver.vo.ResultVO;
import com.crowoj.judgeserver.vo.SandBoxResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

/**
 * @author crow
 * @create 2022/2/18 22:45
 * @description
 */
@RestController
@RequestMapping("/sandbox")
public class SandboxController {

    @Value("${judge.server.key}")
    String JudgeServerKey;
    @Value("${judge.server.key.expire}")
    Long Expire;//分钟

    private void validToken(HttpServletRequest request){
        var valid = request.getHeader("valid");
        var timestampStr = request.getHeader("timestamp");
        if (!StrUtil.isAllNotBlank(valid,timestampStr)){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }
        var timestamp = Long.parseLong(timestampStr);

        var expire = Expire * 60 * 1000;
        var currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - timestamp > expire){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }
        var key = SecureUtil.md5(JudgeServerKey + timestamp);
        if (valid.equals(key)){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }
    }

    private RunEnum getRunEnum(String language){
        if (language.equals("java")){
            return RunEnum.JAVA;
        }else if (language.equals("cpp")){
            return RunEnum.CPP;
        }
        return null;
    }

    private Map<String, List<RunDto>> getDataMap(String[] args,String[] copyOut,String[] copyOutCached,String fileKey,boolean isCompile,RunParam... param){
        var dataMap = new HashMap<String, List<RunDto>>();
        var list = new ArrayList<RunDto>();
        for (RunParam runParam : param) {
            var runDto = new RunDto()
                    .setArgs(args);

            var files = new ArrayList<Map<String,Object>>();
            var map = new HashMap<String,Object>();

            if (isCompile){
                map.put("content",runParam.getContent());
            }else {
                map.put("content",runParam.getStdin());
            }

            files.add(map);


            map = new HashMap<>();
            map.put("name","stdout");
            map.put("max",runParam.getStdoutMax());
            files.add(map);

            map = new HashMap<>();
            map.put("name","stderr");
            map.put("max",runParam.getStderrMax());
            files.add(map);




            var fileMap = new HashMap<String,String>();
            if (isCompile){
                fileMap.put("content", runParam.getContent());
            }else {
                fileMap.put("fileId", runParam.getFileId());
            }
            map = new HashMap<>();
            map.put(fileKey,fileMap);
            runDto.setCopyIn(map);
            runDto.setFiles(files);


            if (copyOut != null){
                runDto.setCopyOut(copyOut);
            }
            if (copyOutCached != null){
                runDto.setCopyOutCached(copyOutCached);
            }
            BeanUtil.copyProperties(runParam,runDto);
            if (isCompile){
                //编译内存*10
                runDto.setMemoryLimit(runDto.getMemoryLimit()*10);
            }
            list.add(runDto);
        }
        dataMap.put("cmd",list);
        return dataMap;
    }

    @Value("${sandbox.address}")
    String sandboxAddress;

    @Resource
    private UserSubmitService userSubmitService;
    @Resource
    private ProblemService problemService;
    @Resource
    private ProblemCaseService problemCaseService;
    @Resource
    private JudgeCaseService judgeCaseService;
    @Resource
    private UserService userService;
    @PostMapping("judge/{sid}")
    public Object judge(@PathVariable Long sid, HttpServletRequest request) throws IOException {
        //验证token
        //validToken(request);
        //查库取得题目
        var submit = userSubmitService.getById(sid);
        var problem = problemService.getById(submit.getProblemId());
        problem.setTryCount(problem.getTryCount()+1);
        //获得所有测试用例
        var cQw = new QueryWrapper<ProblemCase>();
        cQw.lambda().eq(ProblemCase::getPid,problem.getPid());
        var caseList = problemCaseService.list(cQw);

        //构建param
        var toNs = 1000000;
        var toByte = 1024;

        var ml = problem.getMemoryLimit() * toByte; //kb转化为byte
        var tl = problem.getTimeLimit() * toNs; // ms转化为ns

        //如果不是c++则空间/时间限制放宽一倍
        if (!submit.getLanguage().equals(LanguageEnum.CPP.getLanguageNo())){
            ml *= 2;
            tl *= 2;
        }
        LanguageEnum language = null;
        for (LanguageEnum value : LanguageEnum.values()) {
            if (submit.getLanguage().equals(value.getLanguageNo())){
                language = value;
                break;
            }
        }
        if (language == null){
            language = LanguageEnum.CPP;
        }
        var runParam = new RunParam();
        runParam.setCpuLimit(tl);
        runParam.setClockLimit(tl * 2);
        runParam.setMemoryLimit(ml);
        runParam.setStackLimit(problem.getStackLimit() * toByte);
        runParam.setProcLimit(50L);
        runParam.setStderrMax(10240L * 5);
        runParam.setStdoutMax(10240L * 5);
        runParam.setLanguage(language.getLanguage());
        runParam.setContent(submit.getCode());
        //编译
        submit.setStatus(SubmitStatusEnum.Compiling);
        userSubmitService.saveOrUpdate(submit);
        var body = compile(runParam);
        System.err.println("编译返回："+body);
        //运行
        submit.setStatus(SubmitStatusEnum.Judging);
        userSubmitService.saveOrUpdate(submit);
        var resultList = testCase(body,runParam,caseList);
        if (resultList == null){
            //编译未通过，获取body的错误信息，写入submit
            var objMapper = new ObjectMapper();
            var error = objMapper.readTree(body).get(0).get("files").get("stderr").toString();
            submit.setStatus(SubmitStatusEnum.CompileError);
            submit.setErrorMessage(error);
            userSubmitService.saveOrUpdate(submit);
            return ResultUtil.error(5555,"编译未通过");
        }
        //运行通过，修改user_submit,添加judge_case
        var judgeCaseList = new ArrayList<JudgeCase>();
        var status = StatusEnum.AC.getStatus(); //默认Ac
        StringBuilder errMsg = new StringBuilder();
        for (int i = 0; i < resultList.size(); i++) {
            var result = resultList.get(i);
            var currentCase = caseList.get(i);
            var judgeCase = new JudgeCase();
            judgeCase.setCaseId(currentCase.getId())
                    .setUid(submit.getUserId())
                    .setPid(problem.getPid())
                    .setMemory(result.getMemory() / toByte)
                    .setTime(result.getTime() / toNs)
                    .setSubmitId(submit.getId());
            //根据result判断判题结果
            switch (result.getStatus()) {
                case "Accepted" -> judgeCase.setStatus(SubmitStatusEnum.Accepted.getStatusNO());
                case "Memory Limit Exceeded" -> judgeCase.setStatus(SubmitStatusEnum.MemoryLimitExceeded.getStatusNO());
                case "Time Limit Exceeded" -> judgeCase.setStatus(SubmitStatusEnum.TimeLimitExceeded.getStatusNO());
                default -> judgeCase.setStatus(SubmitStatusEnum.RuntimeError.getStatusNO());
            }

            if (!status.equals(result.getStatus()))
                status = result.getStatus();
            //验证输出是否正确
            var map = result.getFiles();
            var stdout = map.get("stdout");
            var stderr = map.get("stderr");
            if (judgeCase.getStatus().equals(SubmitStatusEnum.Accepted.getStatusNO())){
                //判断标准输出是否正确
                //处理stdout末位\n
                if (stdout.charAt(stdout.length()-1) == '\n'){
                    stdout = stdout.substring(0,stdout.length()-1);
                    System.err.println("处理过后的stdout:"+stdout);
                }
                var caseStdout = currentCase.getStdout();
                System.err.println("标准输出："+stdout + "样例标准输出："+caseStdout);
                if (!Objects.equals(stdout, caseStdout)){
                    //不匹配，设置case状态为Wrong Answer
                    judgeCase.setStatus(SubmitStatusEnum.WrongAnswer.getStatusNO());
                }
            }
            if (judgeCase.getStatus().equals(SubmitStatusEnum.RuntimeError.getStatusNO())){
                if (StrUtil.isNotBlank(stdout)){
                    errMsg.append(stdout);
                }
                if(StrUtil.isNotBlank(stderr)){
                    errMsg.append(stdout);
                }
            }
            judgeCaseList.add(judgeCase);
        }
        //如果存在就更新，不存在就save
        for (JudgeCase judgeCase : judgeCaseList) {
            var jcQw = new QueryWrapper<JudgeCase>();
            jcQw.lambda().eq(JudgeCase::getUid,submit.getUserId())
                    .eq(JudgeCase::getPid,submit.getProblemId())
                    .eq(JudgeCase::getSubmitId,submit.getId())
                    .eq(JudgeCase::getCaseId,judgeCase.getCaseId());
            if (judgeCaseService.count(jcQw)==0){
                judgeCaseService.save(judgeCase);
            }else {
                var jcUw = new UpdateWrapper<JudgeCase>();
                jcUw.lambda().eq(JudgeCase::getUid,submit.getUserId())
                        .eq(JudgeCase::getPid,submit.getProblemId())
                        .eq(JudgeCase::getSubmitId,submit.getId())
                        .eq(JudgeCase::getCaseId,judgeCase.getCaseId())
                        .set(JudgeCase::getTime,judgeCase.getTime())
                        .set(JudgeCase::getMemory,judgeCase.getMemory());
                judgeCaseService.update(jcUw);
            }
        }

        //修改user_submit 每通过一个测试点加一次分
        var isAc = true;
        var score = 0.00;
        var avgScore = 100.00 / (double) caseList.size();
        var avgTime = 0L;
        var avgMemory = 0L;
        for (JudgeCase judgeCase : judgeCaseList) {
            if (judgeCase.getStatus().equals(SubmitStatusEnum.Accepted.getStatusNO())){
                score += avgScore;
            }else {
                isAc = false;
            }
            avgTime += judgeCase.getTime();
            avgMemory += judgeCase.getMemory();
        }
        avgTime = avgTime/caseList.size();
        avgMemory = avgMemory/caseList.size();
        submit.setTime(avgTime)
                .setMemory(avgMemory)
                .setScore(score)
                .setErrorMessage(errMsg.toString());
        //是否全部通过
        if (isAc){
            //全部通过
            submit.setScore(100.00);
            problem.setAcceptedCount(problem.getAcceptedCount()+1);
            if (submit.getCid() == null){
                var user = userService.getById(submit.getUserId());
                user.setAcceptCount(user.getAcceptCount()+1);
                userService.saveOrUpdate(user);
            }
            submit.setStatus(SubmitStatusEnum.Accepted);
        }else {
            if (score == 0){
                submit.setStatus(SubmitStatusEnum.WrongAnswer);
            }else {
                submit.setStatus(SubmitStatusEnum.NotAllAccepted);
                submit.setScore(score);
            }
        }
        userSubmitService.saveOrUpdate(submit);
        problemService.saveOrUpdate(problem);
        return ResultUtil.success(submit);
    }

    @PostMapping
    public ResultVO<Object> run(@RequestBody @Valid RunParam param) throws IOException {
        System.err.println(param.toString());
        //        var language = param.getLanguage().toLowerCase();
//        var runEnum = getRunEnum(language);
//        if (runEnum == null){
//            throw new SimpleException(ResultEnum.NOT_FOUND);
//        }
//        //执行编译
//        //获取参数
//        var dataMap = getDataMap(param,runEnum.getCompileArgs(),
//                new String[]{"stdout", "stderr"},
//                new String[]{runEnum.getSource(),runEnum.getOutput()},runEnum.getSource(),true);
//        var objMapper = new ObjectMapper();
//        var json = objMapper.writeValueAsString(dataMap);
//        System.err.println(json);
//        var body = OkHttpUtil.postData(sandboxAddress + "/run", json, null);

        var body = compile(param);
        System.err.println(body);

        //运行
        var objMapper = new ObjectMapper();
        var root = objMapper.readTree(body).get(0);
        var status = root.get("status").asText();

        if (!status.equals(StatusEnum.AC.getStatus())){
            return ResultUtil.error(501,"编译出错");
        }

        var respList = testCase(body,param,null);
        if (respList == null||respList.size() == 0){
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR);
        }
        ;
//        var language = param.getLanguage().toLowerCase();
//        var runEnum = getRunEnum(language);
//        if (runEnum == null){
//            throw new SimpleException(ResultEnum.NOT_FOUND);
//        }
//        //获取文件key
//        var sourceKey= root.get("fileIds").get(runEnum.getSource()).asText();
//        var outputKey = root.get("fileIds").get(runEnum.getOutput()).asText();
//        //运行
//
//        param.setFileId(outputKey);
//        var dataMap = getDataMap(param, runEnum.getRunArgs(), null, null,runEnum.getOutput(),false);
//        var json = objMapper.writeValueAsString(dataMap);
//        body = OkHttpUtil.postData(sandboxAddress + "/run",json,null);
//        //删除文件
//        OkHttpUtil.delete(sandboxAddress + "/file/" + sourceKey);
//        OkHttpUtil.delete(sandboxAddress + "/file/" + outputKey);
//        return objMapper.readValue(body.substring(1,body.length()-1), Map.class);
        return ResultUtil.success(respList);
    }

    //编译
    private String compile(RunParam param) throws IOException {
        var language = param.getLanguage().toLowerCase();
        var runEnum = getRunEnum(language);
        if (runEnum == null){
            throw new SimpleException(ResultEnum.NOT_FOUND);
        }
        //执行编译
        //获取参数
        var dataMap = getDataMap(runEnum.getCompileArgs(),
                new String[]{"stdout", "stderr"},
                new String[]{runEnum.getSource(),runEnum.getOutput()},runEnum.getSource(),true,param);
        var objMapper = new ObjectMapper();
        var json = objMapper.writeValueAsString(dataMap);
        System.err.println("编译参数json："+json);
        return OkHttpUtil.postData(sandboxAddress + "/run", json, null);
    }

    //运行测试用例
    private List<SandBoxResult> testCase(String body, RunParam param, List<ProblemCase> caseList) throws IOException {
        var objMapper = new ObjectMapper();
        var root = objMapper.readTree(body).get(0);
        var status = root.get("status").asText();

        if (!status.equals(StatusEnum.AC.getStatus())){
            //如果不为AC
            return null;
        }
        var language = param.getLanguage().toLowerCase();
        var runEnum = getRunEnum(language);
        if (runEnum == null){
            throw new SimpleException(ResultEnum.NOT_FOUND);
        }
        //获取文件key
        var sourceKey= root.get("fileIds").get(runEnum.getSource()).asText();
        var outputKey = root.get("fileIds").get(runEnum.getOutput()).asText();
        //运行
        param.setFileId(outputKey);
        var dataMap = getDataMap(runEnum.getRunArgs(), null, null,runEnum.getOutput(),false,param);
        //运行
        List<SandBoxResult> respList;
        if (caseList == null || caseList.size()==0){
            //不需要多个标准输入
            var json = objMapper.writeValueAsString(dataMap);
            var resp = OkHttpUtil.postData(sandboxAddress + "/run",json,null);
            var respJson = JSONUtil.parseArray(resp);
            respList = respJson.toList(SandBoxResult.class);
        }else {
            //需要标准输入 //构造paramList
            var paramArr = new RunParam[caseList.size()];
            var i = 0;
            for (ProblemCase problemCase : caseList) {
                param.setStdin(problemCase.getStdin());
                var newParam = new RunParam();
                BeanUtil.copyProperties(param,newParam);
                paramArr[i++] = newParam;
            }
            var map = getDataMap(runEnum.getRunArgs(), null, null,runEnum.getOutput(),false,paramArr);
            var json = objMapper.writeValueAsString(map);
            var resp = OkHttpUtil.postData(sandboxAddress + "/run",json,null);
            System.err.println("运行返回json："+resp);
            var respJson = JSONUtil.parseArray(resp);
            respList = respJson.toList(SandBoxResult.class);
        }
        OkHttpUtil.delete(sandboxAddress + "/file/" + sourceKey);
        OkHttpUtil.delete(sandboxAddress + "/file/" + outputKey);
        return respList;
    }



}
