package com.material.mapper.admin;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.material.dto.admin.WorkerPageQueryDTO;
import com.material.entity.Worker;
import com.material.result.PageResult;
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
     * 分页查询
     * @param workerPageQueryDTO
     * @return
     */
    Page<Worker> pageQuery(WorkerPageQueryDTO workerPageQueryDTO);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Select("select * from worker where id = #{id}")
    Worker getById(Long id);

    /**
     * 根据主键动态修改属性
     * @param worker
     */
    int update(Worker worker);

    void editPassword(Worker worker);
}
