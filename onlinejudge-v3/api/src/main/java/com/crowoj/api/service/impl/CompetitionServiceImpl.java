package com.crowoj.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.enums.StatusEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dao.CompetitionDao;
import com.crowoj.api.dto.CinfoDTO;
import com.crowoj.api.dto.CompetitionDTO;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.entity.*;
import com.crowoj.api.param.AddCompetitionParam;
import com.crowoj.api.param.RegisterCompetitionParam;
import com.crowoj.api.param.UpdateCompetitionParam;
import com.crowoj.api.service.*;
import com.crowoj.api.vo.CompetitionRank;
import com.crowoj.api.vo.RankProblemStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * (Competition)表服务实现类
 *
 * @author crow
 * @since 2022-03-18 14:20:34
 */
@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionDao, Competition> implements CompetitionService {

    @Resource
    private CompetitionProblemService competitionProblemService;
    @Resource
    private CompetitionRegisterService competitionRegisterService;
    @Resource
    private CompetitionRecordService recordService;
    @Resource
    private UserSubmitService userSubmitService;

    @Transactional
    @Override
    public Boolean addCompetition(ActivityUser activityUser, AddCompetitionParam param) {
        var competition = new Competition().setUid(activityUser.getUser().getUid());
        BeanUtil.copyProperties(param,competition);
//        if (param.getAuth() != 0){
//            //判断密码是否重复
//            var qw = new QueryWrapper<Competition>();
//            qw.lambda().eq(Competition::getPassword,competition.getPassword());
//            if (count(qw) != 0){
//                throw new SimpleException(ResultEnum.BAD_REQUEST.getStatus(),"密码重复，建议使用生成的代码");
//            }
//        }
        save(competition);
        var cpList = new ArrayList<CompetitionProblem>();
        param.getProblemList().forEach( i ->{
            var cp = new CompetitionProblem().setCid(competition.getId()).setPid(i.getPid());
            cpList.add(cp);
        });
        competitionProblemService.saveBatch(cpList);
        return true;
    }

    @Override
    public Page<CompetitionDTO> search(String search, Integer status, Integer type,Long uid, Page<CompetitionDTO> page) {
        System.err.println("测试测试");
        return getBaseMapper().search(search,status,type,uid,page);
    }

    @Override
    public Boolean valid(Long cid, String pwd) {
        var qw = new QueryWrapper<Competition>();
        qw.lambda().eq(Competition::getId,cid).eq(Competition::getPassword,pwd);
        var count = count(qw);
        return count>0;
    }

    @Override
    public List<ProblemDTO> getProblemListByCid(ActivityUser activityUser,Long cid) {
        //查看比赛是否结束
        var c = getById(cid);
        if (c.getStartTime().getTime() > System.currentTimeMillis()){
            throw new SimpleException(ResultEnum.UNAUTHORIZED.getStatus(),"比赛没开始");
        }
        if (c.getEndTime().getTime() < System.currentTimeMillis()){
            return getBaseMapper().getProblemListByCid(activityUser.getUser().getUid(),cid);
        }

        //判断是否报名
        var qw = new QueryWrapper<CompetitionRegister>();
        qw.lambda().eq(CompetitionRegister::getCid,cid).eq(CompetitionRegister::getUid,activityUser.getUser().getUid());
        var count = competitionRegisterService.count(qw);
        if (count == 0)
            throw new SimpleException(ResultEnum.UNAUTHORIZED.getStatus(),"请先报名");

        return getBaseMapper().getProblemListByCid(activityUser.getUser().getUid(),cid);
    }

    @Transactional
    @Override
    public Boolean register(User user, RegisterCompetitionParam param) {
        var crQw = new QueryWrapper<CompetitionRegister>();
        crQw.lambda().eq(CompetitionRegister::getCid,param.getCid()).eq(CompetitionRegister::getUid,user.getUid());
        var count = competitionRegisterService.count(crQw);
        if (count != 0){
            throw new SimpleException(ResultEnum.BAD_REQUEST);
        }
        //判断比赛是否需要密码
        var c = getById(param.getCid());
        if (c.getAuth() != 0){
            if (!valid(c.getId(),param.getPwd())){
                throw new SimpleException(ResultEnum.BAD_REQUEST.getStatus(),"密码验证错误");
            }
        }
        //报名
        var cr = new CompetitionRegister().setCid(c.getId()).setUid(user.getUid());
        return competitionRegisterService.save(cr);
    }

    @Override
    public CinfoDTO getCinfoDTO(User user, Long cid) {
        return getBaseMapper().getCinfoDTO(user.getUid(),cid);
    }


    @Override
    public ProblemDTO getCompetitionProblemInfo(User user,Long cid, Long pid) {
        //判断是否报名 且比赛是否开始
        var crQw = new QueryWrapper<CompetitionRegister>()
                .lambda().eq(CompetitionRegister::getUid,user.getUid()).eq(CompetitionRegister::getCid,cid);
        var count = competitionRegisterService.count(crQw);
        //查看比赛是否结束
        var c = getById(cid);
        if (c.getStartTime().getTime() > System.currentTimeMillis()){
            throw new SimpleException(ResultEnum.UNAUTHORIZED.getStatus(),"比赛没开始");
        }
        if (c.getEndTime().getTime() < System.currentTimeMillis()){
            return getBaseMapper().getCompetitionProblemInfo(user.getUid(),cid,pid);
        }
        if (count == 0){
            throw new SimpleException(ResultEnum.UNAUTHORIZED.getStatus(),"尚未报名");
        }

        return getBaseMapper().getCompetitionProblemInfo(user.getUid(),cid,pid);
    }


    @Override
    public List<CompetitionRank> getRankList(Long cid) {
        //获取到比赛所有题目
        var pList = getBaseMapper().getProblemListByCid(-1L,cid);
        //获取到当前比赛的所有提交情况
        var recordList = getBaseMapper().getRecordList(cid);
        //获取当前比赛的所有用户
        var userList = getBaseMapper().getUserListByCid(cid);
        var rankList = new ArrayList<CompetitionRank>();
        userList.forEach(u -> {
            rankList.add(new CompetitionRank().setUid(u.getUid()).setNickname(u.getNickname()).setTotalScore(0.00));
        });
        rankList.forEach(r ->{
            var map = new HashMap<String, RankProblemStatus>();
            pList.forEach(p -> {
                //
                var rpStatus = new RankProblemStatus()
                        .setStatus(0)
                        .setScore(0.00);
                map.put("p"+p.getPid(),rpStatus);
            });
            r.setSubmissionInfo(map);
        });
        rankList.forEach(r->{
            recordList.forEach(record -> {
                if (record.getUid().equals(r.getUid())){
                    //对应用户的提交
                    var map = r.getSubmissionInfo();
                    var submit = userSubmitService.getById(record.getSubmitId());
                    var rpStatus = new RankProblemStatus();
                    if (submit.getStatus().equals(StatusEnum.Accepted)){
                        rpStatus.setStatus(1);
                    }else{
                        rpStatus.setStatus(2);
                    }
                    rpStatus.setScore(record.getScore());
                    map.put("p"+record.getPid(),rpStatus);
                    r.setSubmissionInfo(map);
                    r.setTotalScore(r.getTotalScore() + record.getScore());
                }
            });
        });
        rankList.sort((o1, o2) -> o2.getTotalScore().compareTo(o1.getTotalScore()));
        AtomicLong i = new AtomicLong(1L);
        rankList.forEach(r -> {
            r.setRank(i.getAndIncrement());
        });
        return rankList;
    }

    @Transactional
    @Override
    public Boolean deleteById(Long cid) {
        var rQw = new QueryWrapper<CompetitionRecord>()
                .lambda().eq(CompetitionRecord::getCid,cid);
        var crQw = new QueryWrapper<CompetitionRegister>()
                .lambda().eq(CompetitionRegister::getCid,cid);
        var usQw = new QueryWrapper<UserSubmit>()
                .lambda().eq(UserSubmit::getCid,cid);
        userSubmitService.remove(usQw);
        recordService.remove(rQw);
        competitionRegisterService.remove(crQw);
        return removeById(cid);
    }

    @Transactional
    @Override
    public Boolean updateCompetition(UpdateCompetitionParam param) {
        var competition= getById(param.getId());
        //判断比赛是否开始，ps:比赛开始之后后不可修改，
        if (competition.getStartTime().getTime()<=System.currentTimeMillis()){
            throw new SimpleException(ResultEnum.BAD_REQUEST.getStatus(),"比赛开始之后不可修改");
        }


        BeanUtil.copyProperties(param,competition);
        saveOrUpdate(competition);
        //删除所有题目在重新添加
        var cpQw = new QueryWrapper<CompetitionProblem>()
                .lambda().eq(CompetitionProblem::getCid,param.getId());
        competitionProblemService.remove(cpQw);

        var cpList = new ArrayList<CompetitionProblem>();
        param.getProblemList().forEach( i ->{
            var cp = new CompetitionProblem().setCid(competition.getId()).setPid(i.getPid());
            cpList.add(cp);
        });
        competitionProblemService.saveBatch(cpList);
        return true;
    }

    @Override
    public List<ProblemDTO> getProblemListByCid(Long cid) {
        return getBaseMapper().getProblemListByCid(-1L,cid);
    }
}

