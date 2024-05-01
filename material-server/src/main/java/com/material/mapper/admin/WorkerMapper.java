package com.material.mapper.admin;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.material.entity.Worker;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


public interface WorkerMapper extends BaseMapper {

    /**
     * 插入员工数据
     * @param worker
     */
    @Insert("insert into worker (name, username, password, salt, phone, sex, create_time, update_time, create_user, update_user,status) " +
            "values " +
            "(#{name},#{username},#{password},#{salt},#{phone},#{sex},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void insert(Worker worker);

    /**
     * 根据用户名查询员工
     * @param workerName
     * @return
     */
    @Select("select * from worker where username = #{username}")
    Worker getByUserName(String workerName);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Select("select * from worker where id = #{id}")
    Worker getById(String id);
}
