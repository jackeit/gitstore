package com.fx.client.model;

import com.fx.server.entity.log.SysLoginInfor;
import io.datafx.controller.injection.scopes.ViewScoped;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ViewScoped
public class OnlineUserDataModel {
    private ListProperty<SysLoginInfor> onlineUsers;
    private IntegerProperty selectedIndex;


    public ListProperty<SysLoginInfor> getOnlineUsers() {
        if (onlineUsers == null) {
            ObservableList<SysLoginInfor> innerList = FXCollections.observableArrayList();
            onlineUsers = new SimpleListProperty<>(innerList);
        }
        return onlineUsers;
    }


    public int getSelectedIndex() {
        return selectedIndexProperty().get();
    }

    public IntegerProperty selectedIndexProperty() {
        if (selectedIndex == null) {
            selectedIndex = new SimpleIntegerProperty();
        }
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        selectedIndexProperty().set(selectedIndex);
    }
}