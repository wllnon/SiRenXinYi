package com.test.wllnon.sirenxinyi.pojo;

/**
 * Created by Administrator on 2016/4/21.
 */
public class User {
    private long id;
    private String name;
    private String signature;
    private String avatarUrl;
    private boolean isDoctor;

    public User(long id, String name, String signature, String avatarUrl, boolean isDoctor) {
        this.id = id;
        this.name = name;
        this.signature = signature;
        this.avatarUrl = avatarUrl;
        this.isDoctor = isDoctor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }
}
