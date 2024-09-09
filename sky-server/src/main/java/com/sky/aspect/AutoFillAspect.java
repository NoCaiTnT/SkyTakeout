package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点：对哪些类的哪些方法进行拦截
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     * @param joinPoint 连接点（被拦截方法的信息）
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取拦截到的数据块操作类型：insert or update
        // 更新操作不用改变 创建时间 和 创建者
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取操作类型
        OperationType operationType = autoFill.value();

        // 获得拦截到的方法的参数，即实体对象，这样才能对它赋值
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        // 判空
        if (args == null || args.length == 0) {
            return;
        }
        // 获取实体，默认是第一个参数
        Object entity = args[0];

        // 准备需要赋值的数据
        Long currentID = BaseContext.getCurrentId();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 根据操作类型，通过反射赋值
        if (operationType == OperationType.INSERT) {
            try {
                // 通过反射获取实体类的方法
                Method setCreateTime= entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser= entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime= entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser= entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 调用上述给实体赋值
                setCreateTime.invoke(entity, currentDateTime);
                setCreateUser.invoke(entity, currentID);
                setUpdateTime.invoke(entity, currentDateTime);
                setUpdateUser.invoke(entity, currentID);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                // 通过反射获取实体类的方法
                Method setUpdateTime= entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser= entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 调用上述给实体赋值
                setUpdateTime.invoke(entity, currentDateTime);
                setUpdateUser.invoke(entity, currentID);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
