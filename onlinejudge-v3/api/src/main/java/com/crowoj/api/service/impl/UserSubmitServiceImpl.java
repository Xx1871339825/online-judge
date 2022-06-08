package com.crowoj.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.*;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.dao.UserSubmitDao;
import com.crowoj.api.dto.UserSubmitDTO;
import com.crowoj.api.entity.CompetitionRecord;
import com.crowoj.api.entity.JudgeCase;
import com.crowoj.api.entity.User;
import com.crowoj.api.entity.UserSubmit;
import com.crowoj.api.param.CompetitionSubmitParam;
import com.crowoj.api.param.SubmitParam;
import com.crowoj.api.service.*;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.SubmitInfoVO;
import com.crowoj.api.vo.SubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * (UserSubmit)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 21:40:30
 */
@Service("userSubmitService")
public class UserSubmitServiceImpl extends ServiceImpl<UserSubmitDao, UserSubmit> implements UserSubmitService {

    //服务发现
    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private CompetitionRecordService competitionRecordService;

    @Resource
    private JudgeCaseService judgeCaseService;

    @Resource
    private CompetitionService competitionService;

    @Resource
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @Override
    public SubmitVO submit(User user, SubmitParam param) {

        if (user.getBanFlag().equals(BanFlag.IS_BAN)){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("judgeServer");
        user.setSubmitCount(user.getSubmitCount()+1);
        userService.saveOrUpdateUser(user);
        if (instances == null || instances.size() == 0){
            // 没有判题机实例
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_NOT_JUDGE_SERVER);
        }
        //写入提交表
        var submit = new UserSubmit()
                .setUserId(user.getUid())
                .setSubmitTime(new Timestamp(System.currentTimeMillis()))
                .setCode(param.getCode())
                .setSubmitType(SubmitType.DEFAULT);
        var languageStr = param.getLanguage();
        LanguageEnum language = null;
        if (LanguageEnum.CPP.getLanguage().equals(languageStr)){
            System.err.println("cpp");
            language = LanguageEnum.CPP;
        }else if (LanguageEnum.JAVA.getLanguage().equals(languageStr)){
            language = LanguageEnum.JAVA;
            System.err.println("java");
        }

        if (language == null){
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_UNSUPPORTED_LANGUAGE);
        }
        submit.setProblemId(param.getPid())
                .setLanguage(language)
                .setStatus(StatusEnum.Pending);
        save(submit);
        //调用判题机
        //judge/{sid}
        var uri = instances.get(0).getUri().toString();

        var resultVO = restTemplate.postForObject(uri + "/sandbox/judge/" + submit.getId(), null, ResultVO.class);
        if (resultVO == null){
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_NOT_JUDGE_SERVER);
        }
        var submitVO = new SubmitVO();
        if (resultVO.getStatus() == 5555){
            submitVO.setStatus(StatusEnum.CompileError.getStatus())
                    .setStatusNo(StatusEnum.CompileError.getStatusNO());
        } else {
            var submitObj = resultVO.getData();
            var objJson = JSONUtil.toJsonStr(submitObj);
            var submit1 = JSONUtil.toBean(objJson,UserSubmit.class);
            var status = submit1.getStatus();
            if (status.equals(StatusEnum.Accepted)){
                user.setAcceptCount(user.getAcceptCount()+1);
                userService.saveOrUpdateUser(user);
            }
            submitVO.setStatusNo(status.getStatusNO())
                    .setStatus(status.getStatus());
        }
        return submitVO;
    }

    @Override
    public Page<UserSubmitDTO> getSubmitList(Page<?> page,Long pid,String nickname,Integer status,SubmitType submitType) {
        return getBaseMapper().getSubmitList(page,pid,nickname,status,submitType.getValue());
    }

    @Override
    public SubmitInfoVO getSubmitInfo(Long sid, int type) {
        var submit = getBaseMapper().getSubmitInfoBySid(sid);
        var submitInfoVO = new SubmitInfoVO();
        BeanUtil.copyProperties(submit,submitInfoVO);
        var judgeCaseList = judgeCaseService.getJudgeCaseVOList(sid,type);
        submitInfoVO.setJudgeCaseList(judgeCaseList);
        if (type!=1){
            submitInfoVO.setCode("");
        }
        return submitInfoVO;
    }

    @Transactional
    @Override
    public SubmitVO cSubmit(User user, CompetitionSubmitParam param) {
        if (user.getBanFlag().equals(BanFlag.IS_BAN)){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }

        var c = competitionService.getById(param.getCid());
        if (c.getEndTime().getTime() < System.currentTimeMillis()){
            throw new SimpleException(ResultEnum.UNAUTHORIZED.getStatus(),"比赛已结束，禁止提交");
        }


        List<ServiceInstance> instances = discoveryClient.getInstances("judgeServer");
        if (instances == null || instances.size() == 0){
            // 没有判题机实例
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_NOT_JUDGE_SERVER);
        }
        //写入提交表
        var submit = new UserSubmit()
                .setUserId(user.getUid())
                .setSubmitTime(new Timestamp(System.currentTimeMillis()))
                .setCode(param.getCode())
                .setCid(param.getCid())
                .setSubmitType(SubmitType.COMPETITION);
        var languageStr = param.getLanguage();
        LanguageEnum language = null;
        if (LanguageEnum.CPP.getLanguage().equals(languageStr)){
            System.err.println("cpp");
            language = LanguageEnum.CPP;
        }else if (LanguageEnum.JAVA.getLanguage().equals(languageStr)){
            language = LanguageEnum.JAVA;
            System.err.println("java");
        }

        if (language == null){
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_UNSUPPORTED_LANGUAGE);
        }
        submit.setProblemId(param.getPid())
                .setLanguage(language)
                .setStatus(StatusEnum.Pending);
        save(submit);
        //调用判题机
        //judge/{sid}
        var uri = instances.get(0).getUri().toString();
        var resultVO = restTemplate.postForObject(uri + "/sandbox/judge/" + submit.getId(), null, ResultVO.class);
        if (resultVO == null){
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_NOT_JUDGE_SERVER);
        }
        submit = getById(submit.getId());
        var submitVO = new SubmitVO();
        //记录
        var rQw = new QueryWrapper<CompetitionRecord>();
        rQw.lambda().eq(CompetitionRecord::getCid,param.getCid())
                .eq(CompetitionRecord::getUid,user.getUid())
                .eq(CompetitionRecord::getPid,param.getPid());
        var record = competitionRecordService.getOne(rQw);
        if (record == null){
            record = new CompetitionRecord()
                    .setCid(param.getCid())
                    .setUid(user.getUid())
                    .setPid(param.getPid())
                    .setStatus(-1);
        }

        if (resultVO.getStatus() == 5555){
            submitVO.setStatus(StatusEnum.CompileError.getStatus())
                    .setStatusNo(StatusEnum.CompileError.getStatusNO());
            //如果未通过则修改状态，已通过则不处理
            if (record.getStatus() != 0){
                //未通过,获取到提交的信息，并判断是否需要改修改record
                if (record.getSubmitId() == null){
                    //没有提交过
                    record.setStatus(StatusEnum.CompileError.getStatusNO())
                            .setSubmitId(submit.getId());
                }else {
                    //提交过了
                    //判断分数
                    var rSubmit = getById(record.getSubmitId());
                    if (rSubmit.getScore() < submit.getScore()){
                        record.setSubmitId(submit.getId())
                                .setStatus(submit.getStatus().getStatusNO());
                    }
                }
            }
        } else {
            var submitObj = resultVO.getData();
            var objJson = JSONUtil.toJsonStr(submitObj);
            var submit1 = JSONUtil.toBean(objJson,UserSubmit.class);
            var status = submit1.getStatus();
            submitVO.setStatusNo(status.getStatusNO())
                    .setStatus(status.getStatus());
            submitVO.setStatusNo(status.getStatusNO())
                    .setStatus(status.getStatus());
            if (record.getStatus() != 0){
                //
                if (record.getSubmitId() == null){
                    //没有提交过
                    record.setStatus(StatusEnum.CompileError.getStatusNO())
                            .setSubmitId(submit.getId());
                }else {
                    //提交过了
                    //判断分数
                    var rSubmit = getById(record.getSubmitId());
                    if (rSubmit.getScore() < submit.getScore()){
                        record.setSubmitId(submit.getId())
                                .setStatus(submit.getStatus().getStatusNO());
                    }
                }
                //record.setStatus(status.getStatusNO()).setSubmitId(submit.getId());
            }
        }
        competitionRecordService.saveOrUpdate(record);
        return submitVO;
    }

    @Override
    public Page<UserSubmitDTO> getCSubmitList(Long cid, Page<?> page, Long pid, String nickname, Integer status) {

        return getBaseMapper().getCSubmitList(cid, page, pid, nickname,status);
    }
}

