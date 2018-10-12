package com.wdcloud.validate.targettest;

import com.wdcloud.validate.groups.GroupAdd;
import com.wdcloud.validate.groups.GroupDelete;
import com.wdcloud.validate.groups.GroupModify;
import com.wdcloud.validate.validateex.CaseMode;
import com.wdcloud.validate.validateex.CheckCase;
import org.hibernate.validator.constraints.Length;

/**
 * @author baiyu
 * @date 2018/5/8.
 */
public class PersonTest {
    /*
    * 自带可用注解请参考org.hibernate.validator.constraints包和javax.validation.constraints包下面的注解
    * */

    @Length(min = 6, max = 12)
    private String name;
    private Integer age;
    @Length(min = 6, max = 12, groups = GroupAdd.class)
    private String phoneNum;
    @Length(min = 6, max = 12, groups = {GroupAdd.class, GroupModify.class, GroupDelete.class})
    private String email;
    @CheckCase(value = CaseMode.LOWER, message = "性别字母需要小写", groups = {GroupAdd.class, GroupModify.class})
    private String sex;

    public PersonTest(String name, Integer age, String phoneNum, String email, String sex) {
        this.name = name;
        this.age = age;
        this.phoneNum = phoneNum;
        this.email = email;
        this.sex = sex;
    }

    public PersonTest(String name, Integer age, String phoneNum, String email) {
        this.name = name;
        this.age = age;
        this.phoneNum = phoneNum;
        this.email = email;
        this.sex = "m";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
