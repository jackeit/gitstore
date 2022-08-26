package com.chenyongxi.Test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class User {
    @Autowired
    History history;
    @Autowired
    Favorite favorite;
    @Autowired
    Readme readme;

}
