package com.chengyongxi.springmvc.Entity;

public class User {
    private String name;
    private int age;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    @Override
    public String toString(){
        return String.format("{name:%s,age:%d,address:%s,email:%s",
                this.name,this.age,this.getAddress().getLocation(),
                this.getAddress().getEmail());
    }
}
