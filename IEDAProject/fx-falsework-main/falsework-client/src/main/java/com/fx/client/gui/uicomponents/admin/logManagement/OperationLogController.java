package com.fx.client.gui.uicomponents.admin.logManagement;

import com.fx.client.gui.feature.FeatureResourceConsumer;
import com.fx.client.model.LogDataModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.LogFeign;
import com.fx.client.request.feign.admin.UserFeign;
import com.fx.client.utils.Pinyin4jUtil;
import com.fx.server.entity.log.GateLog;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.core.concurrent.ProcessChain;
import io.datafx.eventsystem.Event;
import io.datafx.eventsystem.OnEvent;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: UserManagementController
 * @author: liwen
 * @date: 2020/3/4 21:05
 */
@ViewController(value = "/fxml/admin/logManagement/operation_log.fxml", title = "操作日志")
public class OperationLogController {

    public static final String CONTENT_PANE = "ContentPane";


    @FXML
    private StackPane root;
    @FXML
    private VBox centPane;
    @FXML
    private StackPane spinnerPane;
    @FXML
    private TextField searchField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField accountTextField;
    @FXML
    private TextField pwdTextField;
    @FXML
    private TextArea descTextArea;
    @FXML
    private ComboBox genderCombobox;
    @FXML
    @ActionTrigger("search")
    private Button searchBut;

    @FXML
    private Pagination pagination;


    @FXML
    private TableView<GateLog> tableView;
    @FXML
    private TableColumn<GateLog, String> serialNumberColumn;
    @FXML
    private TableColumn<GateLog, String> idColumn;
    @FXML
    private TableColumn<GateLog, String> menuColumn;
    @FXML
    private TableColumn<GateLog, String> optColumn;
    @FXML
    private TableColumn<GateLog, String> urlColumn;
    @FXML
    private TableColumn<GateLog, String> optTimeColumn;
    @FXML
    private TableColumn<GateLog, String> optUserIdColumn;
    @FXML
    private TableColumn<GateLog, String> optUserColumn;
    @FXML
    private TableColumn<GateLog, String> optIpColumn;
    @FXML
    private TableColumn<GateLog, String> requetDataColumn;

    @ActionHandler
    private FlowActionHandler actionHandler;

    @Inject
    private LogDataModel logDataModel;

    @Inject
    private FeatureResourceConsumer featureResourceConsumer;

    @PostConstruct
    public void init() {
        featureResourceConsumer.consumeResource(this);
        spinnerPane.setVisible(false);
        serialNumberColumn.setCellFactory((col) -> {
            TableCell<GateLog, String> cell = new TableCell<GateLog, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menu"));
        optColumn.setCellValueFactory(new PropertyValueFactory<>("opt"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("uri"));
        optTimeColumn.setCellValueFactory(new PropertyValueFactory<>("crtTime"));
        optUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("crtUser"));
        optUserColumn.setCellValueFactory(new PropertyValueFactory<>("crtName"));
        optIpColumn.setCellValueFactory(new PropertyValueFactory<>("crtHost"));
        requetDataColumn.setCellValueFactory(new PropertyValueFactory<>("body"));


        FilteredList<GateLog> filteredData = new FilteredList<>(logDataModel.getGateLogs(), p -> true);
        tableView.setItems(filteredData);
        searchField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData.setPredicate(elementProp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String val = Pinyin4jUtil.toPinYinLowercase(newVal);
                return Pinyin4jUtil.toPinYinLowercase(elementProp.getMenu()).contains(val)
                        || Pinyin4jUtil.toPinYinLowercase(elementProp.getCrtName()).contains(val)
                        || elementProp.getCrtHost().toLowerCase().contains(val);
            });
        });

        logDataModel.selectedPersonIndexProperty().bind(tableView.getSelectionModel().selectedIndexProperty());
        pagination.pageCountProperty().bind(logDataModel.pageCountProperty());
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                showPage(param + 1);
                return tableView;
            }
        });


    }


    private void delete() {
        GateLog gateLog = logDataModel.getGateLogs().get(logDataModel.getSelectedPersonIndex());

        JFXAlert alert = new JFXAlert((Stage) root.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("消息提示"));
        layout.setBody(new Label("确实删除【" + gateLog.getId() + "】吗？"));
        JFXButton closeButton = new JFXButton("取消");
        closeButton.setOnAction(event -> alert.hideWithAnimation());
        JFXButton determineButton = new JFXButton("确定");
        determineButton.setOnAction(event -> {
            alert.hideWithAnimation();
            ProcessChain.create()
                    .addSupplierInExecutor(() -> Request.connector(UserFeign.class).delete(gateLog.getId()))
                    .addConsumerInPlatformThread(result -> {
                        if (result.isRel()) {
                            logDataModel.getGateLogs().remove(logDataModel.getSelectedPersonIndex());
                        }
                    }).onException(e -> e.printStackTrace()).run();
        });
        layout.setActions(closeButton, determineButton);
        alert.setContent(layout);
        alert.show();

    }


    private void showPage(Integer page) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("keyId", searchField.getText());
        queryMap.put("page", page);
        query(queryMap);

    }

    @ActionMethod("search")
    private void search() {
        pagination.currentPageIndexProperty().setValue(0);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("keyId", searchField.getText());
        query(queryMap);

    }

    private void query(Map<String, Object> queryMap) {

        ProcessChain.create()
                .addRunnableInPlatformThread(() -> {
                    spinnerPane.setVisible(true);
                    centPane.setDisable(true);
                })
                .addSupplierInExecutor(() -> Request.connector(LogFeign.class).getPageList(queryMap)
                )
                .addConsumerInPlatformThread(result -> {
                    logDataModel.getGateLogs().clear();
                    List<GateLog> gateLogs = result.getDatas();
                    logDataModel.setPageCount((int) result.getTotal());

                    for (GateLog gateLog : gateLogs) {
                        logDataModel.getGateLogs().add(gateLog);
                    }
                })
                .withFinal(() -> {
                    spinnerPane.setVisible(false);
                    centPane.setDisable(false);
                })
                .onException(e -> e.printStackTrace())
                .run();
    }


    @PreDestroy
    private void destroy() {
        System.err.println("destroy " + this);
    }


    @OnEvent("refresh")
    private void onRefresh(Event<String> e) {
        System.err.println(this.getClass() + "\t" + e.getContent());

        search();

    }
}
