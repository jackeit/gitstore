package com.xyz.mapper;

import com.xyz.mybatistest.UserClassName;

import java.util.HashMap;
import java.util.List;

public interface UserClassMapper {
    List<UserClassName> getUserClasses();
    HashMap<Integer, String> getclassHashMap();
}
