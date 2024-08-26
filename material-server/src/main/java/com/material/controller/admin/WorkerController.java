package com.material.controller.admin;

import com.material.dto.admin.*;
import com.material.entity.Worker;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.admin.WorkerService;
import com.material.vo.admin.WorkerLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/worker")
@Slf4j
@Tag(name = "B端-工作人员相关接口")
public class WorkerController {

    @Resource
    private WorkerService workerService;


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
        // TODO 因为Token是无状态的，无法重置过期时间，JWT失效的唯一途径就是等待时间过期，
        // TODO 所以可以让前端将Token直接丢弃。
        // TODO 可以使用白名单或黑名单的方式让JWT失效，黑名单的具体操作就说将注销的JWT放入redis中，
        // TODO 并且设置过期时间为JWT的过期时间，请求资源时判断该JWT是否在redis中，如果存在则拒绝访问。

        return Result.success();
    }

    /**
     * 员工分页查询
     * @param workerPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(
            description = "员工分页查询",
            summary = "员工分页查询"
    )
    public Result<PageResult> page(WorkerPageQueryDTO workerPageQueryDTO){
        log.info("员工分页查询，参数为：{}", workerPageQueryDTO);
        PageResult pageResult = workerService.pageQuery(workerPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(
            description = "启动禁用员工账号",
            summary = "启动禁用员工账号"
    )
    public Result startOrStop(Long id, @PathVariable Integer status){
        log.info("启用禁用员工账号：{},{}",id,status);
        workerService.startOrStop(id,status);
        // 如果修改的行数为0，返回错误信息

        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/find/{id}")
    @Operation(
            description = "根据员工id查询员工信息",
            summary = "根据员工id查询员工信息"
    )
    public Result<Worker> getById(@PathVariable Long id){
        Worker worker = workerService.getById(id);
        if(worker==null){
            return Result.error("该账号不存在！");
        }
        return Result.success(worker);
    }

    /**
     * 编辑员工信息
     * @param workerDTO
     * @return
     */
    @PutMapping("/update")
    @Operation(
            description = "编辑员工信息",
            summary = "编辑员工信息"
    )
    public Result update(@RequestBody WorkerDTO workerDTO){
        log.info("编辑员工信息：{}", workerDTO);
        workerService.update(workerDTO);
        return Result.success();
    }

    @PutMapping("/editPassword")
    @Operation(
            description = "修改员工密码",
            summary = "修改员工密码"
    )
    public Result editPassword(@RequestBody WorkerEditPasswordDTO workerEditPasswordDTO){
        log.info("修改员工密码：{}", workerEditPasswordDTO);
        workerService.editPassword(workerEditPasswordDTO);
        return Result.success();
    }

}
