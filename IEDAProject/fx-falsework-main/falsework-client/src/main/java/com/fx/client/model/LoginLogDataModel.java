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
public class LoginLogDataModel {
    private int counter = 0;
    private ListProperty<SysLoginInfor> sysLoginInfors;

    private IntegerProperty pageCount;
    private IntegerProperty selectedPersonIndex;


    public ListProperty<SysLoginInfor> getSysLoginInfors() {
        if (sysLoginInfors == null) {
            ObservableList<SysLoginInfor> innerList = FXCollections.observableArrayList();
            sysLoginInfors = new SimpleListProperty<>(innerList);
        }
        return sysLoginInfors;
    }

    public int getSelectedPersonIndex() {
        return selectedPersonIndexProperty().get();
    }

    public void setSelectedPersonIndex(int selectedPersonIndex) {
        this.selectedPersonIndex.set(selectedPersonIndex);
    }

    public IntegerProperty selectedPersonIndexProperty() {
        if (selectedPersonIndex == null) {
            selectedPersonIndex = new SimpleIntegerProperty();
        }
        return selectedPersonIndex;
    }

    public int getPageCount() {
        return pageCount.get();
    }

    public IntegerProperty pageCountProperty() {
        if (pageCount == null) {
            pageCount = new SimpleIntegerProperty();
        }
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount.set(pageCount);
    }
}