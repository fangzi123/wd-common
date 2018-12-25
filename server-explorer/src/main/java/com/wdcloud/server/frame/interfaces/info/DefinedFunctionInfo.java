package com.wdcloud.server.frame.interfaces.info;

/**
 * @author andy
 * @date 2017/1/9.
 */
public class DefinedFunctionInfo {
    public final String resourceName;
    public final String functionName;

    public DefinedFunctionInfo() {
        this.resourceName = null;
        this.functionName = null;
    }

    public DefinedFunctionInfo(String resourceName, String functionName) {
        this.resourceName = resourceName;
        this.functionName = functionName;
    }
}
