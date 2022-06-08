package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.JudgeCaseDao;
import com.crowoj.api.entity.JudgeCase;
import com.crowoj.api.service.JudgeCaseService;
import com.crowoj.api.vo.JudgeCaseVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (JudgeCase)表服务实现类
 *
 * @author crow
 * @since 2022-03-01 19:00:12
 */
@Service("judgeCaseService")
public class JudgeCaseServiceImpl extends ServiceImpl<JudgeCaseDao, JudgeCase> implements JudgeCaseService {

    @Override
    public List<JudgeCaseVO> getJudgeCaseVOList(Long sid, int type) {

        var list =  getBaseMapper().getJudgeCaseVOList(sid);
        if (list == null) list = new ArrayList<>();
        if (type != 1){
            list.forEach(i->{
                i.setStdin("").setStdout("");
            });
        }
        return list;
    }
}

