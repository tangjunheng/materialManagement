package com.material.controller.admin;

import com.material.constant.JwtClaimsConstant;
import com.material.dto.admin.WorkerLoginDTO;
import com.material.dto.admin.WorkerRegisterDTO;
import com.material.entity.Worker;
import com.material.properties.JwtProperties;
import com.material.result.Result;
import com.material.service.admin.WorkerService;
import com.material.utils.JwtUtil;
import com.material.vo.admin.WorkerLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/worker")
@Slf4j
@Tag(name = "工作人员相关接口")
public class WorkerController {

    @Resource
    private WorkerService workerService;

    //jwt配置
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 新增员工
     * @param workerRegisterDTO
     * @return
     */
    @PostMapping("/register")
    @Operation(
            description = "新增员工",
            summary = "新增员工"
    )
    public Result registerWorker(@RequestBody WorkerRegisterDTO workerRegisterDTO) {
        log.info("新增员工：{}", workerRegisterDTO);
        workerService.register(workerRegisterDTO);
        return Result.success();
    }

    /**
     * 员工登录
     * @param workerLoginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(
            description = "员工登录",
            summary = "员工登录"
    )
    public Result<WorkerLoginVO> login(@RequestBody WorkerLoginDTO workerLoginDTO) {
        log.info("员工登录：{}", workerLoginDTO);

        WorkerLoginVO workerLoginVO = workerService.login(workerLoginDTO);

        return Result.success(workerLoginVO);
    }

    /**
     * 员工退出登录
     * @return
     */
    @PostMapping("/logout")
    @Operation(
            description = "员工退出登录",
            summary = "员工退出登录"

    )
    public Result<String> logout(){
        return Result.success();
    }




}
