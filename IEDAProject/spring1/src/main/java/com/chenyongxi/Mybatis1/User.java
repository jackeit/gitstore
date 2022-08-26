package com.chenyongxi.Mybatis1;

public class User {
    private int id;
    private int age;
    private int class_id;
    private String name;
    private String hobbies;
    private String className;

    public int getId() {
        return id;
    }

    public String getClassName() {return className;}

    public void setClassName(String className) {this.className = className;}

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
    //有构造函数的话会报错??

    public User(int age,String name,String hobbies,int class_id){

            this.age = age;
            this.name = name;
            this.hobbies = hobbies;
            this.class_id = class_id;

    }
}
