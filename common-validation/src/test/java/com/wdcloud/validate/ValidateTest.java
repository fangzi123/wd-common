package com.wdcloud.validate;

import com.wdcloud.validate.targettest.PersonAopTest;
import com.wdcloud.validate.targettest.PersonTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidateTest {
    @Autowired
    PersonAopTest t;

    @Test
    public void oneTest() {
        System.out.println("oneTest");
        PersonTest p = new PersonTest("abc", 12, "1234567891212", "1@1.com");
        t.personOneTest(p);
    }

    @Test
    public void twoTest() {
        System.out.println("twoTest");
        PersonTest p = new PersonTest("abc", 12, "1234567891212", "123456@1aa.com");
        t.personTwoTest(p);
    }

    @Test
    public void threeTest() {
        System.out.println("threeTest");
        PersonTest p = new PersonTest("abc", 12, "12345678912", "1@1.com", "M");
        t.personThreeTest(p);
    }
}