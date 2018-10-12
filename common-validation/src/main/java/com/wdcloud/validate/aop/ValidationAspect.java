package com.wdcloud.validate.aop;

import com.wdcloud.validate.annotation.ValidationParam;
import com.wdcloud.validate.dto.ValidateResult;
import com.wdcloud.validate.spi.IValidationObject;
import com.wdcloud.validate.spi.IValidationProcessResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Aspect
@Component
public class ValidationAspect {
    @Autowired
    private ValidatorFactory validatorFactory;
    @Autowired
    private IValidationObject validationObject;
    @Autowired
    IValidationProcessResult validationProcessResult;

    @Pointcut("@annotation(com.wdcloud.validate.annotation.ValidationParam)")
    public void validationPointcut() {

    }

    @Around(value = "validationPointcut()")
    public Object validationAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        ValidationParam va = method.getAnnotation(ValidationParam.class);
        Class<?> vaClass = va.clazz();
        Class<?>[] groups = va.groups();
        Object[] args = pjp.getArgs();
        Object validateObject = validationObject.getValidateObject(args, vaClass);

        if (validateObject != null) {
            List<ValidateResult> vrList = doValidateObject(validateObject, groups);
            validationProcessResult.doProcessResult(vrList);
        }
        return pjp.proceed();
    }

    private List<ValidateResult> doValidateObject(Object vo, Class<?>[] groups) {
        Set<ConstraintViolation<Object>> res = validatorFactory.getValidator().validate(vo, groups);
        return convertToValidateResult(res);
    }

    private List<ValidateResult> convertToValidateResult(Set<ConstraintViolation<Object>> res) {
        List<ValidateResult> vrList = new ArrayList<ValidateResult>();
        if (res != null && res.size() > 0) {
            res.forEach(r -> {
                ValidateResult vr = new ValidateResult();
                vr.setMsgKey(r.getPropertyPath() != null ? r.getPropertyPath().toString() : "");
                vr.setMsgBody(r.getMessage());
                vrList.add(vr);
            });
        }
        return vrList;
    }
}
