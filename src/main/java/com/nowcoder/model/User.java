package com.nowcoder.model;

/**
 * @author kwin
 * @create 2019-12-23 15:16
 */
public class User {

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
