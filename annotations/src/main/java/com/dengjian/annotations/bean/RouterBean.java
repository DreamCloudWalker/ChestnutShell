package com.dengjian.annotations.bean;

import javax.lang.model.element.Element;

public class RouterBean {
    public enum TypeEnum {
        ACTIVITY
    }

    private TypeEnum typeEnum;
    private Element element;
    private Class<?> myClass;
    private String path;
    private String group;

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public String getPath() {
        return path;
    }

    public String getGroup() {
        return group;
    }
}
