package com.fx.client.gui.uicomponents.admin.logManagement;

import com.fx.client.model.LoginLogDataModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.LoginLogFeign;
import com.fx.client.utils.Pinyin4jUtil;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.util.DateUtils;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.core.concurrent.ProcessChain;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: LoginLogController
 * @author: liwen
 * @date: 2021/1/4 14:06
 */
@ViewController("/fxml/admin/logManagement/login_log.fxml")
public class LoginLogController {

    @FXML
    private VBox contentPane;

    @FXML
    private TextField loginNameTextfield;
    @FXML
    private TextField addrTextfield;
    @FXML
    private ComboBox statusComboBox;

    @FXML
    private TableView<SysLoginInfor> tableView;
    @FXML
    private Pagination pagination;
    @FXML
    private JFXSpinner spinner;
    @FXML
    @ActionTrigger("rest")
    private Button restButton;
    @FXML
    @ActionTrigger("search")
    private Button searchButton;
    @FXML
    private JFXDatePicker startDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;

    @FXML
    private TableColumn<SysLoginInfor, String> infoIdColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> loginNameColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> ipaddrColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> statusColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> msgColumn;
    @FXML
    private TableColumn<SysLoginInfor, Date> loginTimeColumn;

    @Inject
    private LoginLogDataModel loginLogDataModel;

    @PostConstruct
    private void init() {
        spinner.setVisible(false);
        statusComboBox.getSelectionModel().select(0);
        infoIdColumn.setCellValueFactory(new PropertyValueFactory<>("infoId"));
        loginNameColumn.setCellValueFactory(new PropertyValueFactory<>("loginName"));
        ipaddrColumn.setCellValueFactory(new PropertyValueFactory<>("ipaddr"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(col -> {
            TableCell<SysLoginInfor, String> cell = new TableCell<SysLoginInfor, String>() {
                private Label label = new Label("");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {

                        if (item.equals("0")) {
                            label.setId("status-success-label");
                            label.setText("成功");
                        } else {
                            label.setId("status-error-label");
                            label.setText("失败");
                        }
                        setGraphic(label);
                    }
                }
            };
            return cell;
        });
        msgColumn.setCellValueFactory(new PropertyValueFactory<>("msg"));
        loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        loginTimeColumn.setCellFactory(col -> {

            TableCell<SysLoginInfor, Date> cell = new TableCell<SysLoginInfor, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty && item != null) {
                        setText(DateUtils.formatDate(item, DateUtils.DATETIME_FORMAT));
                    }
                }
            };
            return cell;
        });

        FilteredList<SysLoginInfor> filteredData = new FilteredList<>(loginLogDataModel.getSysLoginInfors(), p -> true);
        tableView.setItems(filteredData);
        loginNameTextfield.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData.setPredicate(elementProp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String val = Pinyin4jUtil.toPinYinLowercase(newVal);
                return Pinyin4jUtil.toPinYinLowercase(elementProp.getLoginName()).contains(val);
            });
        });

        loginLogDataModel.selectedPersonIndexProperty().bind(tableView.getSelectionModel().selectedIndexProperty());
        pagination.pageCountProperty().bind(loginLogDataModel.pageCountProperty());
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                showPage(param + 1);
                return tableView;
            }
        });


    }


    private void showPage(Integer page) {


        Map<String, Object> queryMap = getQueryMap();
        queryMap.put("page", page);
        queryMap.put("limit", 20);
        query(queryMap);

    }

    private Map<String, Object> getQueryMap() {
        String loginName = loginNameTextfield.getText();
        String ipaddr = addrTextfield.getText();
        String status = statusComboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (statusComboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "0" : "1");
        LocalDate beginDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("page", 0);
        queryMap.put("limit", 20);
        queryMap.put("loginName", loginName);
        queryMap.put("ipaddr", ipaddr);
        queryMap.put("status", status);
        queryMap.put("startTime", beginDate != null ? beginDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null);
        queryMap.put("endTime", endDate != null ? endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null);
        return queryMap;
    }

    private void query(Map<String, Object> queryMap) {

        ProcessChain.create()
                .addRunnableInPlatformThread(() -> {
                    loginLogDataModel.getSysLoginInfors().clear();
                    spinner.setVisible(true);
                    contentPane.setDisable(true);
                })
                .addSupplierInExecutor(
                        () -> Request.connector(LoginLogFeign.class).getSysLoginInfor(queryMap)
                )
                .addConsumerInPlatformThread(result -> {

                    List<SysLoginInfor> gateLogs = result.getDatas();
                    loginLogDataModel.setPageCount((int) result.getTotal());
                    loginLogDataModel.getSysLoginInfors().addAll(gateLogs);

                })
                .withFinal(() -> {
                    spinner.setVisible(false);
                    contentPane.setDisable(false);
                })
                .onException(e -> e.printStackTrace())
                .run();
    }

    @ActionMethod("search")
    private void search() {
        pagination.currentPageIndexProperty().setValue(0);
        query(getQueryMap());
    }

    @ActionMethod("rest")
    private void rest() {
        loginNameTextfield.setText("");
        addrTextfield.setText("");
        statusComboBox.getSelectionModel().select(0);
        startDatePicker.valueProperty().set(null);
        endDatePicker.valueProperty().set(null);
        searchButton.fire();
    }
}
