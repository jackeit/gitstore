package com.fx.client.model;

import com.fx.server.entity.log.GateLog;
import io.datafx.controller.injection.scopes.FlowScoped;
import io.datafx.controller.injection.scopes.ViewScoped;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ViewScoped
public class LogDataModel {
    private int counter = 0;
    private ListProperty<GateLog> gateLogs;

    private IntegerProperty pageCount;
    private IntegerProperty selectedPersonIndex;


    public ListProperty<GateLog> getGateLogs() {
        if (gateLogs == null) {
            ObservableList<GateLog> innerList = FXCollections.observableArrayList();
            gateLogs = new SimpleListProperty<>(innerList);
        }
        return gateLogs;
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