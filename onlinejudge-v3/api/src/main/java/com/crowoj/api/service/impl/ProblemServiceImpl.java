package com.crowoj.api.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.dao.ProblemDao;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.entity.Problem;
import com.crowoj.api.entity.ProblemCase;
import com.crowoj.api.param.AddProblemParam;
import com.crowoj.api.service.ProblemCaseService;
import com.crowoj.api.service.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author crow
 * @create 2022/2/20 22:26
 * @description
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemDao,Problem> implements ProblemService {


    @Resource
    private ProblemCaseService problemCaseService;

    @Override
    public ProblemDTO getProblemById(Long pid, Long uid,ProblemType problemType) {
        return getBaseMapper().getProblemVO(pid,uid, problemType.getProblemType());
    }

    @Override
    public Page<ProblemDTO> search(Long uid, String search,ProblemType problemType , Page<ProblemDTO> page) {
        Integer problemCode = null;
        if (problemType != null){
            problemCode = problemType.getProblemType();
        }
        return getBaseMapper().getProblemList(uid,search,problemCode,page);
    }


    @Transactional
    @Override
    public Boolean saveOrUpdateProblem(ActivityUser activityUser, AddProblemParam param) {
        var problem = new Problem();
        BeanUtil.copyProperties(param,problem);
        if (problem.getUid() == null){
            problem.setUid(activityUser.getUser().getUid());
        }
        if (problem.getPid() != null){
            var pcQw = new QueryWrapper<ProblemCase>();
            pcQw.lambda().eq(ProblemCase::getPid,problem.getPid());
            problemCaseService.remove(pcQw);
        }
        var saveOrUpdateP = saveOrUpdate(problem);
        var caseList = param.getTestCase();
        var newCaseList = new ArrayList<ProblemCase>();
        caseList.forEach(i -> {
            var pCase = new ProblemCase().setPid(problem.getPid())
                    .setStdin(i.getStdin())
                    .setStdout(i.getStdout());
            newCaseList.add(pCase);
        });
        var saveC = problemCaseService.saveBatch(newCaseList);

        return saveOrUpdateP && saveC;
    }

    @Override
    public AddProblemParam getProblemInfo(ActivityUser activityUser, Long pid) {
        var problem = getById(pid);
        var param = new AddProblemParam();
        BeanUtil.copyProperties(problem,param);
        var pcQw = new QueryWrapper<ProblemCase>();
        pcQw.lambda().eq(ProblemCase::getPid,problem.getPid());
        var caseList = problemCaseService.list(pcQw);
        return param.setTestCase(caseList);
    }

    @Override
    public List<ProblemDTO> getSearchProblemList(String search) {
        return getBaseMapper().getSearchProblemList(search);
    }
}
