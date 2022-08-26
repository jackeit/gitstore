package com.fx.client.gui.uicomponents.monitor.quartz;

import com.fx.client.model.SysJobLogModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.SysJobFeign;
import com.fx.client.utils.Pinyin4jUtil;
import com.fx.server.entity.log.SysJobLog;
import com.fx.server.util.DateUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
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
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: JobController
 * @author: liwen
 * @date: 2020/12/25 09:28
 */
@ViewController(value = "/fxml/monitor/quartz/job_log.fxml", title = "调度日志")
public class SysJobLogController {

    @FXML
    private StackPane root;
    @FXML
    private VBox centPane;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private TextField jobNameField;
    @FXML
    private JFXComboBox jobGroupComboBox;
    @FXML
    private JFXComboBox jobStatusComboBox;
    @FXML
    private JFXDatePicker beginDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;

    @FXML
    @ActionTrigger("search")
    private Button searchButton;
    @FXML
    @ActionTrigger("reset")
    private Button resetButton;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<SysJobLog> tableView;
    @FXML
    private TableColumn<SysJobLog, String> sysJobNumberColumn;
    @FXML
    private TableColumn<SysJobLog, String> jobNameColumn;
    @FXML
    private TableColumn<SysJobLog, String> jobGroupColumn;
    @FXML
    private TableColumn<SysJobLog, String> jobClassColumn;
    @FXML
    private TableColumn<SysJobLog, String> jobInfoColumn;
    @FXML
    private TableColumn<SysJobLog, String> jobStatusColumn;
    @FXML
    private TableColumn<SysJobLog, Date> createTimeColumn;
    @FXML
    private TableColumn<SysJobLog, String> optIpColumn;

    @Inject
    private SysJobLogModel logModel;

    private StringConverter<Date> dateConverter = new StringConverter<Date>() {
        @Override
        public String toString(Date date) {
            if (date == null) {
                return "";
            }
            return DateUtils.formatDate(date, DateUtils.DATETIME_FORMAT);
        }

        @Override
        public Date fromString(String string) {
            return DateUtils.parseDate(string);
        }
    };

    @PostConstruct
    public void init() {
        spinner.setVisible(false);
        jobGroupComboBox.getSelectionModel().select(0);
        jobStatusComboBox.getSelectionModel().select(0);

        sysJobNumberColumn.setCellValueFactory(new PropertyValueFactory<>("jobLogId"));
        jobNameColumn.setCellValueFactory(new PropertyValueFactory<>("jobName"));
        jobGroupColumn.setCellValueFactory(new PropertyValueFactory<>("jobGroup"));
        jobClassColumn.setCellValueFactory(new PropertyValueFactory<>("invokeTarget"));
        jobInfoColumn.setCellValueFactory(new PropertyValueFactory<>("jobMessage"));
        jobStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        jobStatusColumn.setCellFactory(col -> {
            TableCell<SysJobLog, String> cell = new TableCell<SysJobLog, String>() {
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
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        createTimeColumn.setCellFactory(col -> {

            TableCell<SysJobLog, Date> cell = new TableCell<SysJobLog, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty && item != null) {
                        setText(dateConverter.toString(item));
                    }
                }
            };
            return cell;
        });
        optIpColumn.setCellFactory(col -> {
            TableCell<SysJobLog, String> cell = new TableCell<SysJobLog, String>() {
                private JFXButton button = new JFXButton("详细");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        setGraphic(button);
                    }
                }
            };
            return cell;
        });

        FilteredList<SysJobLog> filteredData = new FilteredList<>(logModel.getSysJobLogs(), p -> true);
        tableView.setItems(filteredData);
        jobNameField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData.setPredicate(elementProp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String val = Pinyin4jUtil.toPinYinLowercase(newVal);
                return Pinyin4jUtil.toPinYinLowercase(elementProp.getJobName()).contains(val);

            });
        });

        logModel.selectedPersonIndexProperty().bind(tableView.getSelectionModel().selectedIndexProperty());
        pagination.pageCountProperty().bindBidirectional(logModel.pageCountProperty());
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
        String jobName = jobNameField.getText();
        String jobGroup = jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "默认" : "系统");
        String jobStatus = jobStatusComboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobStatusComboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "0" : "1");
        LocalDate beginDate = beginDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("page", 0);
        queryMap.put("limit", 20);
        queryMap.put("jobName", jobName);
        queryMap.put("jobName", jobName);
        queryMap.put("jobGroup", jobGroup);
        queryMap.put("status", jobStatus);
        queryMap.put("startTime", beginDate != null ? beginDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null);
        queryMap.put("endTime", endDate != null ? endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : null);
        return queryMap;
    }

    @ActionMethod("search")
    private void search() {
        query(getQueryMap());
    }

    @ActionMethod("reset")
    private void reset() {
        jobNameField.setText("");
        jobGroupComboBox.getSelectionModel().select(0);
        jobStatusComboBox.getSelectionModel().select(0);
        beginDatePicker.setValue(null);
        endDatePicker.setValue(null);
        searchButton.fire();
    }

    private void query(Map<String, Object> queryMap) {

        ProcessChain.create()
                .addRunnableInPlatformThread(() -> {
                    spinner.setVisible(true);
                    centPane.setDisable(true);
                })
                .addSupplierInExecutor(() -> Request.connector(SysJobFeign.class).getSysJobLogList(queryMap)
                )
                .addConsumerInPlatformThread(result -> {
                    logModel.getSysJobLogs().clear();
                    List<SysJobLog> gateLogs = result.getDatas();
                    logModel.setPageCount((int) result.getTotal());
                    logModel.getSysJobLogs().addAll(gateLogs);

                })
                .withFinal(() -> {
                    spinner.setVisible(false);
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
