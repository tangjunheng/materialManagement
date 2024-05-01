package com.material.service.admin;


import com.material.dto.admin.WorkerLoginDTO;
import com.material.dto.admin.WorkerRegisterDTO;
import com.material.entity.Worker;

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
    Worker login(WorkerLoginDTO workerLoginDTO);
}
