package com.fx.client.model;

import com.fx.server.entity.SysJob;
import com.fx.server.vo.UserVO;
import io.datafx.controller.ViewController;
import io.datafx.controller.injection.scopes.FlowScoped;
import io.datafx.controller.injection.scopes.ViewScoped;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ViewScoped
public class SysJobDataModel {
    private int counter = 0;
    private ListProperty<SysJob> sysJobs;

    private IntegerProperty selectedPersonIndex = new SimpleIntegerProperty(-1);

    public ListProperty<SysJob> getSysJobs() {
        if (sysJobs == null) {
            ObservableList<SysJob> innerList = FXCollections.observableArrayList();
            sysJobs = new SimpleListProperty<>(innerList);
        }
        return sysJobs;
    }

    public int getSelectedPersonIndex() {
        return selectedPersonIndex.get();
    }

    public IntegerProperty selectedPersonIndexProperty() {
        return selectedPersonIndex;
    }

    public void setSelectedPersonIndex(int selectedPersonIndex) {
        this.selectedPersonIndex.set(selectedPersonIndex);
    }

    public SysJob selectSysJob() {
        return getSysJobs().get(selectedPersonIndex.getValue());

    }
}