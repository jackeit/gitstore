package com.fx.client.gui.uicomponents.monitor.quartz;

import com.fx.client.gui.uicomponents.main.MainController;
import com.fx.client.model.SysJobDataModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.SysJobFeign;
import com.fx.client.store.ApplicatonStore;
import com.fx.client.utils.Pinyin4jUtil;
import com.fx.server.entity.SysJob;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.ViewController;
import io.datafx.controller.context.ApplicationContext;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.core.concurrent.ProcessChain;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * @description:
 * @className: JobController
 * @author: liwen
 * @date: 2020/12/25 09:28
 */
@ViewController(value = "/fxml/monitor/quartz/job.fxml", title = "定时任务")
public class SysJobController {

    @FXML
    private JFXDialog dialog;
    @FXML
    private Label dialogTitle;
    @FXML
    private JFXTextField jobNameTextField;
    @FXML
    private JFXComboBox jobGroupNameComboBox;
    @FXML
    private JFXTextField jobClassTextField;
    @FXML
    private JFXTextField jobTimeTextField;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXTextField parameterTextField;
    @FXML
    private JFXTextArea detailedTextArea;
    @FXML
    private JFXComboBox jobGroupComboBox;
    @FXML
    private JFXComboBox jobStatusCommboBox;
    @FXML
    @ActionTrigger("updateJob")
    private JFXButton updateButton;
    @FXML
    @ActionTrigger("showJobLog")
    private JFXButton jobLogButton;
    @FXML
    @ActionTrigger("saveJob")
    private JFXButton saveButton;
    @FXML
    @ActionTrigger("closeDialog")
    private JFXButton cancelButton;

    @FXML
    private StackPane rootPane;
    @FXML
    private GridPane jobDetailPane;
    @FXML
    private HBox operatButPane;

    @FXML
    @ActionTrigger("showAddJobDialog")
    private JFXButton addJobButton;

    @FXML
    @ActionTrigger("restSearch")
    private JFXButton resetButton;
    @FXML
    @ActionTrigger("search")
    private JFXButton searchButton;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<SysJob, Integer> jobIdColumn;
    @FXML
    private TableColumn<SysJob, String> jobNameColumn;
    @FXML
    private TableColumn<SysJob, String> jobGroupNameColumn;
    @FXML
    private TableColumn<SysJob, Class> jobClassColumn;
    @FXML
    private TableColumn<SysJob, String> jobTimeColumn;
    @FXML
    private TableColumn<SysJob, Map> jobDataColumn;
    @FXML
    private TableColumn<SysJob, String> jobStatusColumn;
    @FXML
    private TableColumn<SysJob, String> operatingColumn;
    private JFXSnackbar snackbar;

    @Inject
    private SysJobDataModel sysJobDataModel;

    private FilteredList<SysJob> filteredData;

    @PostConstruct
    private void init() {

        updateButton.visibleProperty().bind(saveButton.visibleProperty().not());
        updateButton.managedProperty().bind(updateButton.visibleProperty());
        saveButton.managedProperty().bind(saveButton.visibleProperty());
        snackbar = new JFXSnackbar(rootPane);
        snackbar.setPrefWidth(500);
        operatingColumn.setCellFactory(col -> {
            TableCell<SysJob, String> cell = new TableCell<SysJob, String>() {

                private final ToggleButton editBut = new ToggleButton();
                private final ToggleButton detailsBut = new ToggleButton();
                private final ToggleButton playBut = new ToggleButton();
                private final ToggleButton logBut = new ToggleButton();
                private final ToggleButton delBut = new ToggleButton();
                private HBox hBox = new HBox(editBut, detailsBut, playBut, logBut, delBut);

                {
                    editBut.setTooltip(new Tooltip("编辑"));
                    detailsBut.setTooltip(new Tooltip("查看详细"));
                    playBut.setTooltip(new Tooltip("立即执行一次"));
                    logBut.setTooltip(new Tooltip("调度日志"));
                    delBut.setTooltip(new Tooltip("删除"));

                    editBut.getStyleClass().add("left-pill");
                    detailsBut.getStyleClass().add("center-pill");
                    playBut.getStyleClass().add("center-pill");
                    logBut.getStyleClass().add("center-pill");
                    delBut.getStyleClass().add("right-pill");

                    try {
                        editBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".create-outline"));
                        detailsBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".chakan-copy"));
                        playBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".zhihing"));
                        logBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".diaodurizhi-copy"));
                        delBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".trash-outline"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editBut.setOnAction(event -> {
                        tableView.getSelectionModel().select(getIndex());
                        showEditJobDialog();
                    });
                    delBut.setOnAction(event -> {
                        tableView.getSelectionModel().select(getIndex());
                        deleteJob();
                    });
                    detailsBut.setOnAction(event -> {
                        tableView.getSelectionModel().select(getIndex());
                        showJobDialog();
                    });
                    playBut.setOnAction(event -> {
                        tableView.getSelectionModel().select(getIndex());
                        runJob();
                    });

                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {

                        hBox.setSpacing(0);
                        hBox.setAlignment(Pos.CENTER);
                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        });
        jobIdColumn.setCellFactory(col -> {
            TableCell<SysJob, Integer> cell = new TableCell<SysJob, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        setText((getIndex() + 1) + "");
                    }
                }
            };
            return cell;
        });
        jobNameColumn.setCellValueFactory(new PropertyValueFactory<>("jobName"));
        jobGroupNameColumn.setCellValueFactory(new PropertyValueFactory<>("jobGroupName"));
        jobClassColumn.setCellValueFactory(new PropertyValueFactory<>("jobClass"));
        jobTimeColumn.setCellValueFactory(new PropertyValueFactory<>("jobTime"));
        jobDataColumn.setCellValueFactory(new PropertyValueFactory<>("parames"));
        jobStatusColumn.setCellValueFactory(new PropertyValueFactory<>("jobStatus"));
        jobStatusColumn.setCellFactory(col -> {
            TableCell<SysJob, String> cell = new TableCell<SysJob, String>() {
                private final JFXToggleButton statusButton = new JFXToggleButton();

                {
                    statusButton.setOnAction(event -> {
                        tableView.getSelectionModel().select(getIndex());

                        if (statusButton.isSelected()) {
                            resumeJob();
                        } else {
                            pauseJob();
                        }
                    });

                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        statusButton.setSelected(item.equals("NORMAL") ? true : false);
                        setGraphic(statusButton);
                    }
                }
            };
            return cell;
        });

        sysJobDataModel.selectedPersonIndexProperty().bind(tableView.getSelectionModel().selectedIndexProperty());
        filteredData = new FilteredList<>(sysJobDataModel.getSysJobs(), p -> true);
        tableView.setItems(filteredData);
        searchField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData();
        });
        jobGroupComboBox.getSelectionModel().select(0);
        jobGroupComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            filteredData();
        });
        jobStatusCommboBox.getSelectionModel().select(0);
        jobStatusCommboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            filteredData();
        });

        query();
    }

    private void filteredData() {
        String jobName = searchField.getText();
        String jobGroup = jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "默认" : "系统");
        String jobStatus = jobStatusCommboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobStatusCommboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "NORMAL" : "PAUSED");
        filteredData.setPredicate(elementProp -> {

            boolean a = false;
            boolean b = false;
            boolean c = false;
            if (StringUtils.isBlank(jobName)) {
                a = true;
            } else {
                a = Pinyin4jUtil.toPinYinLowercase(elementProp.getJobName()).contains(Pinyin4jUtil.toPinYinLowercase(jobName));
            }
            if (StringUtils.isBlank(jobGroup)) {
                b = true;
            } else {
                b = elementProp.getJobGroupName().toLowerCase().equalsIgnoreCase(jobGroup);
            }
            if (StringUtils.isBlank(jobStatus)) {
                c = true;
            } else {
                c = elementProp.getJobStatus().toLowerCase().equalsIgnoreCase(jobStatus);
            }


            return a && b && c;
        });
    }

    @ActionMethod("search")
    private void query() {
        String jobName = searchField.getText();
        String jobGroup = jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobGroupComboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "默认" : "系统");
        String jobStatus = jobStatusCommboBox.getSelectionModel().selectedIndexProperty().getValue() == 0 ? null : (jobStatusCommboBox.getSelectionModel().selectedIndexProperty().getValue() == 1 ? "NORMAL" : "PAUSED");


        Map<String, String> map = new HashMap();
        map.put("jobName", jobName);
        map.put("jobGroup", jobGroup);
        map.put("jobStatus", jobStatus);
        ProcessChain.create().addRunnableInPlatformThread(() -> {
            sysJobDataModel.getSysJobs().clear();
        }).addSupplierInExecutor(
                () -> Request.connector(SysJobFeign.class).getSysJobAll(map)
        ).addConsumerInPlatformThread(
                rel -> sysJobDataModel.getSysJobs().addAll(rel)
        ).run();
    }

    @ActionMethod("showAddJobDialog")
    private void showAddJobDialog() {
        saveButton.setVisible(true);
        dialogTitle.setText("新增定时任务");
        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);
        operatButPane.setVisible(true);

        dialog.show(rootPane);

    }

    private void showEditJobDialog() {
        SysJob sysJob = sysJobDataModel.selectSysJob();
        jobNameTextField.setText(sysJob.getJobName());
        jobGroupNameComboBox.getSelectionModel().select(sysJob.getJobGroupName());
        jobTimeTextField.setText(sysJob.getJobTime());
        jobClassTextField.setText(sysJob.getJobClass());
        detailedTextArea.setText(sysJob.getDescription());
        parameterTextField.setText(sysJob.getParames());
        operatButPane.setVisible(true);
        saveButton.setVisible(false);
        dialogTitle.setText("编辑定时任务");
        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);
        dialog.show(rootPane);

    }

    private void showJobDialog() {
        SysJob sysJob = sysJobDataModel.selectSysJob();
        jobNameTextField.setText(sysJob.getJobName());
        jobGroupNameComboBox.getSelectionModel().select(sysJob.getJobGroupName());
        jobTimeTextField.setText(sysJob.getJobTime());
        jobClassTextField.setText(sysJob.getJobClass());
        detailedTextArea.setText(sysJob.getDescription());
        parameterTextField.setText(sysJob.getParames());
        operatButPane.setVisible(false);
        dialogTitle.setText("任务详情");
        dialog.setTransitionType(JFXDialog.DialogTransition.TOP);
        dialog.show(rootPane);

    }


    @ActionMethod("closeDialog")
    private void closeDialog() {
        dialog.close();
    }

    @ActionMethod("saveJob")
    private void saveJob() {
        SysJob sysJob = new SysJob();
        sysJob.setJobName(jobNameTextField.getText());
        sysJob.setJobGroupName(jobGroupNameComboBox.getSelectionModel().getSelectedIndex() == 0 ? "默认" : "系统");
        sysJob.setJobTime(jobTimeTextField.getText());
        sysJob.setJobClass(jobClassTextField.getText());
        String parames = parameterTextField.getText();
        sysJob.setParames(parames);

        ProcessChain.create().addSupplierInExecutor(
                () -> Request.connector(SysJobFeign.class).addJob(sysJob)
        ).addConsumerInPlatformThread(rel -> {
            if (rel == 0) {
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("添加成功！ ")));
            }
            query();
        }).withFinal(() -> dialog.close()).run();

    }

    @ActionMethod("updateJob")
    private void updateJob() {
        SysJob sysJob = new SysJob();
        sysJob.setJobName(jobNameTextField.getText());
        sysJob.setJobGroupName(jobGroupNameComboBox.getSelectionModel().getSelectedIndex() == 0 ? "默认" : "系统");
        sysJob.setJobTime(jobTimeTextField.getText());
        sysJob.setJobClass(jobClassTextField.getText());
        sysJob.setDescription(detailedTextArea.getText());
        sysJob.setParames(parameterTextField.getText());

        ProcessChain.create().addSupplierInExecutor(
                () -> Request.connector(SysJobFeign.class).updateJob(sysJob)
        ).addConsumerInPlatformThread(rel -> {
            if (rel == 0) {
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("更新成功！ ")));
            }
        }).withFinal(() -> dialog.close()).run();

    }

    @ActionMethod("showJobLog")
    private void showJobLog() {
        MainController mainController = ApplicationContext.getInstance().getRegisteredObject(MainController.class);
        try {
            mainController.addTab("调度日志", SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".diaodurizhi-copy"), SysJobLogController.class,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ActionMethod("restSearch")
    private void restSearch() {
        searchField.setText("");
        jobStatusCommboBox.getSelectionModel().select(0);
        jobGroupComboBox.getSelectionModel().select(0);
        query();
    }


    private void runJob() {
        ProcessChain.create()
                .addSupplierInExecutor(() -> Request.connector(SysJobFeign.class).runJob(sysJobDataModel.selectSysJob().getJobName(), sysJobDataModel.selectSysJob().getJobGroupName()))
                .addConsumerInPlatformThread(rel -> {
                    if (rel == 0) {
                        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("执行成功！ ")));
                    }
                }).withFinal(() -> {

        }).onException(e -> e.printStackTrace())
                .run();

    }

    private void pauseJob() {
        ProcessChain.create()
                .addSupplierInExecutor(() -> Request.connector(SysJobFeign.class).pauseJob(sysJobDataModel.selectSysJob().getJobName(), sysJobDataModel.selectSysJob().getJobGroupName()))
                .addConsumerInPlatformThread(rel -> {
                    if (rel == 0) {
                        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("执行成功！ ")));
                    }
                }).withFinal(() -> {

        }).onException(e -> e.printStackTrace())
                .run();

    }

    private void resumeJob() {
        ProcessChain.create()
                .addSupplierInExecutor(() -> Request.connector(SysJobFeign.class).resumeJob(sysJobDataModel.selectSysJob().getJobName(), sysJobDataModel.selectSysJob().getJobGroupName()))
                .addConsumerInPlatformThread(rel -> {
                    if (rel == 0) {
                        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("执行成功！ ")));
                    }
                }).withFinal(() -> {

        }).onException(e -> e.printStackTrace())
                .run();

    }

    private void deleteJob() {

        JFXAlert alert = new JFXAlert((Stage) rootPane.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("消息提示"));
        layout.setBody(new Label("确实删除【" + sysJobDataModel.selectSysJob().getJobName() + "】吗？"));
        JFXButton closeButton = new JFXButton("取消");
        closeButton.setOnAction(event -> alert.hideWithAnimation());
        JFXButton determineButton = new JFXButton("确定");
        determineButton.setOnAction(event -> {
            alert.hideWithAnimation();
            ProcessChain.create()
                    .addSupplierInExecutor(() -> Request.connector(SysJobFeign.class).deleteJob(sysJobDataModel.selectSysJob().getJobName(), sysJobDataModel.selectSysJob().getJobGroupName()))
                    .addConsumerInPlatformThread(rel -> {
                        if (rel == 0) {
                            query();
                        }
                    }).withFinal(() -> {

            }).onException(e -> e.printStackTrace())
                    .run();
        });
        layout.setActions(closeButton, determineButton);
        alert.setContent(layout);
        alert.show();

    }
}
