package com.material.service.admin;


import com.material.dto.admin.WorkerLoginDTO;
import com.material.dto.admin.WorkerRegisterDTO;
import com.material.entity.Worker;
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
}
