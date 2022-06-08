package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.entity.Problem;
import com.crowoj.api.dto.ProblemDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author crow
 * @create 2022/2/20 22:27
 * @description
 */
public interface ProblemDao extends BaseMapper<Problem> {

    Page<ProblemDTO> getProblemList(Long uid,String search,Integer problemType,Page<?> page);
    ProblemDTO getProblemVO(Long pid, Long uid,Integer problemType);

    @Select("""
            SELECT DISTINCT
                   p.*,
                   u.nickname AS nickname
               FROM
                   problem AS p
                        LEFT JOIN ( problem_tag AS pt, tag AS t ) ON pt.problem_id = p.pid
                        AND pt.tag_id = t.tid
                        LEFT JOIN `user` AS u ON u.uid = p.uid
                   WHERE
                        p.pid LIKE concat('%',#{search},'%') OR
                        p.title LIKE concat('%',#{search},'%') OR
                        p.description LIKE concat('%',#{search},'%') OR
                        t.tag_name LIKE concat('%',#{search},'%') OR
                        u.nickname LIKE CONCAT('%',#{search},'%')
            """)
    List<ProblemDTO> getSearchProblemList(String search);


//    @Query(
//            value = """
//    SELECT DISTINCT
//            p.*
//        FROM
//            problem AS p
//                LEFT JOIN ( problem_tag AS pt, tag AS t ) ON pt.problem_id = p.id
//                AND pt.tag_id = t.id
//        WHERE
//            p.id LIKE concat('%',?1,'%') OR
//            p.title LIKE concat('%',?1,'%') OR
//            p.description LIKE concat('%',?1,'%') OR
//            t.tag_name LIKE concat('%',?1,'%')
//""",nativeQuery = true)

    /*        SELECT DISTINCT
            p.*,uc.is_accepted
        FROM
            problem AS p
                LEFT JOIN user_commit AS uc ON uc.problem_id = p.id
                AND uc.user_id = #{userId}
                LEFT JOIN ( problem_tag AS pt, tag AS t ) ON pt.problem_id = p.id
                AND pt.tag_id = t.id
        WHERE
            p.id LIKE concat('%',#{search},'%') OR
            p.title LIKE concat('%',#{search},'%') OR
            p.description LIKE concat('%',#{search},'%') OR
            p.origin LIKE concat('%',#{search},'%') OR
            t.tag_name LIKE concat('%',#{search},'%')*/

    /*        SELECT
            p.*,
            GROUP_CONCAT( t.tag_name ) AS tags
        FROM
            problem AS p
                LEFT JOIN ( problem_tag AS pt, tag AS t ) ON p.id = pt.problem_id
                AND pt.tag_id = t.id
        WHERE
            p.id = #{id}
        GROUP BY
            p.id*/

}
