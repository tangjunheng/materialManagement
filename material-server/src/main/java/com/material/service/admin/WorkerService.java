package com.material.service.admin;


import com.material.dto.admin.*;
import com.material.entity.Worker;
import com.material.result.PageResult;
import com.material.vo.admin.WorkerLoginVO;

public interface WorkerService {

    /**
     * 新增工作人员
     */
    void register(WorkerRegisterDTO workerRegisterDTO);

    /**
     * 工作人员登录
     * @param workerLoginDTO
     * @return
     */
    WorkerLoginVO login(WorkerLoginDTO workerLoginDTO);

    /**
     * 员工分页查询
     * @param workerPageQueryDTO
     * @return
     */
    PageResult pageQuery(WorkerPageQueryDTO workerPageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Worker getById(Long id);

    void update(WorkerDTO workerDTO);

    void editPassword(WorkerEditPasswordDTO workerEditPasswordDTO);
}
