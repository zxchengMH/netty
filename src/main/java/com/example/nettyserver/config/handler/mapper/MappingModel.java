package com.example.nettyserver.config.handler.mapper;

/**
 * @ClassName MappingModel
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/25 17:24
 * @Version 1.0
 */
public class MappingModel {

    private String path;

    private String method;

    public MappingModel(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public int hashCode(){
        return  ("HTTP " + this.method.toUpperCase() + " " + this.path.toUpperCase()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MappingModel){
            MappingModel o1 = (MappingModel) o;
            return o1.getMethod().equalsIgnoreCase(this.method) && o1.path.equalsIgnoreCase(this.path);
        }
        return false;
    }
}
