package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.dto.CRecordDTO;
import com.crowoj.api.dto.CompetitionDTO;
import com.crowoj.api.dto.ProblemDTO;
import com.crowoj.api.dto.CinfoDTO;
import com.crowoj.api.entity.Competition;
import com.crowoj.api.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Competition)表数据库访问层
 *
 * @author crow
 * @since 2022-03-18 14:20:32
 */
public interface CompetitionDao extends BaseMapper<Competition> {

    Page<CompetitionDTO> search(String search, Integer status, Integer type,Long uid, Page<CompetitionDTO> page);

    List<ProblemDTO> getProblemListByCid(Long uid, Long cid);

    CinfoDTO getCinfoDTO(Long uid, Long cid);

    @Select("""
            SELECT DISTINCT
                                         p.pid,
                             						p.uid,
                             						p.title,
                             						p.`level`,
                             						p.description,
                             						p.time_limit,
                             						p.memory_limit,
                             						p.problem_type,
                             						(SELECT COUNT(us.id) FROM user_submit AS us WHERE us.problem_id = p.pid AND us.submit_type = 1) AS try_count,
                             						(SELECT COUNT(us.id) FROM user_submit AS us WHERE us.problem_id = p.pid AND us.submit_type = 1 AND us.`status` = 0) AS accepted_count,
                                         IFNULL( us.`status`,- 1 ) AS is_ac
                                     FROM
                                         problem AS p
                                             LEFT JOIN user_submit AS us ON p.pid = us.problem_id
                                             AND us.user_id = #{uid}
                                             AND us.`status` = 0
                                             AND us.submit_type = 1,
                                         competition_problem AS cp,
                                         competition as c
                                     WHERE
                                         p.pid = cp.pid
                                       AND
                                         cp.cid = #{cid}
                                       And
                                         c.id = cp.cid
                             					AND p.pid = #{pid}
            """)
    ProblemDTO getCompetitionProblemInfo(Long uid,Long cid, Long pid);

    @Select(
            """
            SELECT
            	r.*,
            	u.nickname,
            	us.score
            FROM
            	competition_record AS r,
            	`user` AS u,
            	user_submit AS us
            WHERE
            	r.uid = u.uid
            	AND r.cid = #{cid}
            	AND us.user_id = u.uid
            	AND us.cid = r.cid
            	AND us.id = r.submit_id
                                    
                    """
    )
    List<CRecordDTO> getRecordList(Long cid);

    @Select("""
            SELECT u.*
            FROM competition_register AS cr,`user` AS u
            where cr.uid = u.uid
            AND cr.cid = #{cid}
            """)
    List<User> getUserListByCid(Long cid);
}

