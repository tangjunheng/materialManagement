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


    @PostMapping("/login")
    public Result<WorkerLoginVO> login(@RequestBody WorkerLoginDTO workerLoginDTO) {
        log.info("员工登录：{}", workerLoginDTO);

        Worker worker = workerService.login(workerLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.WORK_ID, worker.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        WorkerLoginVO workerLoginVO = WorkerLoginVO.builder()
                .id(worker.getId())
                .userName(worker.getUsername())
                .name(worker.getName())
                .token(token)
                .build();

        return Result.success(workerLoginVO);
    }

    @PostMapping("/register")
    public Result registerWorker(@RequestBody WorkerRegisterDTO workerRegisterDTO) {
        log.info("新增工作人员：{}", workerRegisterDTO);
        workerService.register(workerRegisterDTO);
        return Result.success();
    }
}
