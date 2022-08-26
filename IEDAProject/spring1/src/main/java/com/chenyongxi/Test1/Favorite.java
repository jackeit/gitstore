package com.chenyongxi.Test1;

import org.springframework.stereotype.Component;

@Component
public class Favorite {
    private int total = 2;
    private String[] names = {"baskitball","football"};

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
