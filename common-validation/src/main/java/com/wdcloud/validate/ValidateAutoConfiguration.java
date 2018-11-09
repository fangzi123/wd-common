package com.wdcloud.validate;

import com.wdcloud.validate.spi.IValidationProcessResult;
import com.wdcloud.validate.spi.ValidationDefaultProcessResult;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@ComponentScan("com.wdcloud.validate")
public class ValidateAutoConfiguration {
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
//        localValidatorFactoryBean.setValidationMessageSource();
        return localValidatorFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean(IValidationProcessResult.class)
    public IValidationProcessResult validationProcessResult() {
        return new ValidationDefaultProcessResult();
    }
}
