package com.wdcloud.validate.common;

import com.wdcloud.validate.spi.IValidationObject;
import com.wdcloud.validate.targettest.PersonTest;
import org.springframework.stereotype.Component;

/**
 * @author baiyu
 * @date 2018/5/8.
 */
@Component
public class ValidationObjectTest implements IValidationObject {
    @Override
    public Object getValidateObject(Object[] args, Class<?> clazz) {
        for (Object o : args) {
            if (o instanceof PersonTest) {
//                DataEditInfo dataEditInfo = (DataEditInfo) arg;
//                Object o = JSON.parseObject(dataEditInfo.beanJson, clazz);
                return o;
            }
        }
        return null;
    }
}
