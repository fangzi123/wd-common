package com.wdcloud.validate.targettest;

import com.wdcloud.validate.annotation.ValidationParam;
import com.wdcloud.validate.groups.GroupAdd;
import com.wdcloud.validate.groups.GroupDelete;
import com.wdcloud.validate.groups.GroupModify;
import org.springframework.stereotype.Component;

/**
 * @author baiyu
 * @date 2018/5/8.
 */
@Component
public class PersonAopTest {
    /*
    * 需要在IDataEditComponent的子类中，add/modify/delete方法上加ValidationParam注解
    * */
    @ValidationParam(clazz = PersonTest.class)
    public Object personOneTest(PersonTest p) {
        return null;
    }

    @ValidationParam(clazz = PersonTest.class, groups = {GroupAdd.class, GroupModify.class, GroupDelete.class})
    public Object personTwoTest(PersonTest p) {
        return null;
    }

    @ValidationParam(clazz = PersonTest.class, groups = {GroupAdd.class, GroupModify.class})
    public Object personThreeTest(PersonTest p) {
        return null;
    }
}
