package com.haker.usermanagementframework.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haker on 3/30/17.
 */

public class User extends RealmObject {
    @PrimaryKey
    private long id;

    private String name;

    private String email;

    private String phone;

    private String address;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
