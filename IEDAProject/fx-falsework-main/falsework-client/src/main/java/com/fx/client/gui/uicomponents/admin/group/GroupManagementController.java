package com.fx.client.gui.uicomponents.admin.group;

import com.fx.client.gui.uicomponents.admin.group.components.GroupDetailController;
import com.fx.client.model.GroupDataModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.GroupTypeFeign;
import com.fx.server.vo.GroupTypeVO;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.core.concurrent.ProcessChain;
import io.datafx.eventsystem.Event;
import io.datafx.eventsystem.OnEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @description:
 * @className: GroupManagementController
 * @author: liwen
 * @date: 2020/7/22 15:09
 */
@ViewController(value = "/fxml/admin/group/group_management.fxml", title = "角色权限管理")
public class GroupManagementController {


    @FXML
    private JFXTabPane tabPane;

    @FXML
    private StackPane rootPane;
    @Inject
    private GroupDataModel groupDataModel;
    @FXML
    private JFXSpinner viewSpinner;

    @PostConstruct
    private void init() {

        TextField textField = new TextField();
        textField.setStyle("");
        viewSpinner.visibleProperty().bind(tabPane.disableProperty());
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
//                groupDataModel.setSelectedGroupType(newValue.);
            }
        });
        initData();

    }

    private void initData() {
        ProcessChain.create().addRunnableInPlatformThread(() -> {
            tabPane.getTabs().clear();
            tabPane.setDisable(true);
        })
                .addSupplierInExecutor(() -> Request.connector(GroupTypeFeign.class).getAllGroupTypes())
                .addConsumerInPlatformThread(rel -> {
                    for (GroupTypeVO groupType : rel) {
                        Tab tab = new Tab(groupType.getName());
                        Flow flow = new Flow(GroupDetailController.class);
                        ViewFlowContext viewFlowContext = new ViewFlowContext();
                        viewFlowContext.register("groupType", groupType.getId());
                        viewFlowContext.register("rootPane", rootPane);
                        try {
                            tab.setContent(flow.createHandler(viewFlowContext).start());
                        } catch (FlowException e) {
                            e.printStackTrace();
                        }
                        tabPane.getTabs().add(tab);
                    }
                }).onException(e -> e.printStackTrace()).withFinal(() -> {
            tabPane.setDisable(false);
        })
                .run();
    }

    @OnEvent("refresh")
    private void onRefresh(Event<String> e) {
        System.err.println(this.getClass() + "\t" + e.getContent());

        initData();

    }
}
