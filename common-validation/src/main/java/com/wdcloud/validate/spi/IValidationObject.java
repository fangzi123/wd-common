package com.wdcloud.validate.spi;

public interface IValidationObject {
    /**
     * get validate object.
     *
     * @param args
     * @param clazz
     * @return
     */
    Object getValidateObject(Object[] args, Class<?> clazz);
}
