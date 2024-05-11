package com.material.service.admin.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.JwtClaimsConstant;
import com.material.constant.StatusConstant;
import com.material.context.BaseContext;
import com.material.dto.admin.*;
import com.material.entity.Worker;
import com.material.constant.MessageConstant;
import com.material.exception.*;
import com.material.mapper.admin.WorkerMapper;
import com.material.properties.JwtProperties;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.admin.WorkerService;
import com.material.utils.JwtUtil;
import com.material.vo.admin.WorkerLoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WorkerServiceImpl implements WorkerService {
    @Resource
    private WorkerMapper workerMapper;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 新增工作人员
     * @param workerRegisterDTO
     */
    @Override
    public void register(WorkerRegisterDTO workerRegisterDTO) {
        Worker worker = new Worker();

        // 获取需要设置的密码与随机盐
        String password = workerRegisterDTO.getPassword();
        String salt = workerRegisterDTO.getSalt();

        // 密码加盐用md5进行加密，放进DTO对象
        String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        workerRegisterDTO.setPassword(md5Password);

        // 对创建的worker对象赋值
        // 将DTO对象的属性值放进worker
        BeanUtils.copyProperties(workerRegisterDTO, worker);
        // 设置账号的状态，默认正常状态 1表示正常 0表示锁定
        worker.setStatus(StatusConstant.ENABLE);

//        以下代码已由AOP通知
//        // 设置当前记录的创建时间和修改时间
//        worker.setCreateTime(LocalDateTime.now());
//        worker.setUpdateTime(LocalDateTime.now());
//
//        // 设置当前记录创建人id和修改人id
//        worker.setCreateUser(BaseContext.getCurrentId());
//        worker.setUpdateUser(BaseContext.getCurrentId());

        workerMapper.insert(worker);

    }

    /**
     * 工作人员登录
     *
     * @param workerLoginDTO
     * @return
     */
    @Override
    public WorkerLoginVO login(WorkerLoginDTO workerLoginDTO) {
        String username = workerLoginDTO.getUsername();
        String password = workerLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Worker worker = workerMapper.getByUserName(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (worker == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex((password + worker.getSalt()).getBytes());
        if (!password.equals(worker.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (worker.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

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

        //3、返回实体对象
        return workerLoginVO;
    }

    /**
     * 分页查询，可以添加name内容来进行姓名查询
     * @param workerPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(WorkerPageQueryDTO workerPageQueryDTO) {
        // select * from employee limit 0,10
        //开始分页查询
        PageHelper.startPage(workerPageQueryDTO.getPage(), workerPageQueryDTO.getPageSize());

        Page<Worker> page = workerMapper.pageQuery(workerPageQueryDTO);

        long total = page.getTotal();
        List<Worker> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     */
    public void startOrStop(Long id, Integer status) {
        // 判断status是否是0或1，如果不是就报错
        if (!status.equals(StatusConstant.DISABLE) && !status.equals(StatusConstant.ENABLE)){
            throw new StatusErrorException(MessageConstant.STATUS_NOT_FOUND);
        }
        Worker worker = Worker.builder()
                .status(status)
                .id(id)
                .build();

        int count = workerMapper.update(worker);
        if (count == 0) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }

    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */
    @Override
    public Worker getById(Long id) {
        Worker worker = workerMapper.getById(id);
        if(worker==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 将密码用***隐藏起来
        worker.setPassword("****");
        return worker;
    }

    /**
     * 编辑员工信息
     * @param workerDTO
     */
    @Override
    public void update(WorkerDTO workerDTO) {
        Worker worker = new Worker();
        BeanUtils.copyProperties(workerDTO, worker);

//        以下代码已由AOP通知
//        worker.setUpdateTime(LocalDateTime.now());
//        worker.setUpdateUser(BaseContext.getCurrentId());

        int count = workerMapper.update(worker);
        if (count == 0) {
            throw new UpdateFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }

    // TODO 邮箱发送验证码修改密码
    /**
     * 修改密码
     * @param workerEditPasswordDTO
     */
    @Override
    public void editPassword(WorkerEditPasswordDTO workerEditPasswordDTO) {
        Worker worker = new Worker();
        // 获取需要设置的密码与随机盐
        String password = workerEditPasswordDTO.getPassword();
        String salt = workerEditPasswordDTO.getSalt();

        // 密码加盐用md5进行加密，放进DTO对象
        String md5Password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        workerEditPasswordDTO.setPassword(md5Password);


        // 对创建的worker对象赋值
        // 将DTO对象的属性值放进worker
        BeanUtils.copyProperties(workerEditPasswordDTO, worker);
        // 添加其它属性
        worker.setUpdateTime(LocalDateTime.now());
        worker.setUpdateUser(BaseContext.getCurrentId());
        int count = workerMapper.editPassword(worker);
        if (count == 0) {
            throw new UpdateFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }
}
