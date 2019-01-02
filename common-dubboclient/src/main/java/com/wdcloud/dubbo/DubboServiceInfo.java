package com.wdcloud.dubbo;

/**
 * @author Andy
 * @date 17/6/27
 */
public class DubboServiceInfo<T> {

    private String group;
    private String version;
    private Class<?> aClass;
    private T t;

    public DubboServiceInfo(Class<?> aClass, T t) {
        this(null, null, aClass, t);
    }

    public DubboServiceInfo(String group, Class<?> aClass, T t) {
        this(group, null, aClass, t);
    }

    public DubboServiceInfo(String group, String version, Class<?> aClass, T t) {
        this.group = group;
        this.version = version;
        this.aClass = aClass;
        this.t = t;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DubboServiceInfo<?> that = (DubboServiceInfo<?>) o;

        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (aClass != null ? !aClass.equals(that.aClass) : that.aClass != null) return false;
        return t != null ? t.equals(that.t) : that.t == null;

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (aClass != null ? aClass.hashCode() : 0);
        result = 31 * result + (t != null ? t.hashCode() : 0);
        return result;
    }

    boolean equals(String group, String version, Class<?> aClass) {
        return Utils.stringEquals(group, this.getGroup())
                && Utils.stringEquals(version, this.version)
                && aClass == this.aClass;
    }
}
