package com.fx.client.gui.uicomponents.monitor.onlineuser;

import com.fx.client.model.OnlineUserDataModel;
import com.fx.client.request.Request;
import com.fx.client.request.feign.admin.UserFeign;
import com.fx.client.store.ApplicatonStore;
import com.fx.client.utils.Pinyin4jUtil;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.util.DateUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.core.concurrent.ProcessChain;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Date;

/**
 * @description:
 * @className: OnlineUserController
 * @author: liwen
 * @date: 2021/1/1 13:24
 */
@ViewController(value = "/fxml/monitor/onlineuser/online_user.fxml", title = "在线用户")
public class OnlineUserController {
    @FXML
    private StackPane rootPane;
    @FXML
    private VBox contentPane;

    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField ipTextfiled;
    @FXML
    private TableView<SysLoginInfor> tableView;

    @FXML
    private JFXSpinner spinner;
    @FXML
    @ActionTrigger("rest")
    private Button restButton;
    @FXML
    @ActionTrigger("search")
    private Button searchButton;

    @FXML
    private TableColumn<SysLoginInfor, String> numberColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> sessionNumberColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> loginNameColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> ipaddrColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> sessionStatusColumn;
    @FXML
    private TableColumn<SysLoginInfor, Date> loginTimeColumn;
    @FXML
    private TableColumn<SysLoginInfor, String> optColumn;

    @Inject
    private OnlineUserDataModel onlineUserDataModel;

    private JFXSnackbar snackbar;

    @PostConstruct
    private void init() {
        spinner.setVisible(false);
        snackbar = new JFXSnackbar(rootPane);
        snackbar.setPrefWidth(500);
        numberColumn.setCellFactory((col) -> {
            TableCell<SysLoginInfor, String> cell = new TableCell<SysLoginInfor, String>() {
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
        sessionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("infoId"));
        loginNameColumn.setCellValueFactory(new PropertyValueFactory<>("loginName"));
        ipaddrColumn.setCellValueFactory(new PropertyValueFactory<>("ipaddr"));
        sessionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        sessionStatusColumn.setCellFactory(col -> {
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
                            label.setText("在线");
                        } else {
                            label.setId("status-error-label");
                            label.setText("离线");
                        }
                        setGraphic(label);
                    }
                }
            };
            return cell;
        });
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

        Callback<TableColumn<SysLoginInfor, String>, TableCell<SysLoginInfor, String>> cellFactory = new Callback<TableColumn<SysLoginInfor, String>, TableCell<SysLoginInfor, String>>() {
            @Override
            public TableCell<SysLoginInfor, String> call(TableColumn param) {

                final TableCell<SysLoginInfor, String> cell = new TableCell<SysLoginInfor, String>() {

                    private final JFXButton retreatBut = new JFXButton("强退");

                    {
                        retreatBut.setStyle("-fx-background-color: #e63c53");
                        try {
                            retreatBut.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".tuichu"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        retreatBut.setOnAction(event -> {

                            tableView.getSelectionModel().select(getIndex());
                            retreat();

                        });

                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(retreatBut);
                        }
                    }
                };
                return cell;
            }
        };

        optColumn.setCellFactory(cellFactory);

        FilteredList<SysLoginInfor> filteredData = new FilteredList<>(onlineUserDataModel.getOnlineUsers(), p -> true);
        tableView.setItems(filteredData);
        userNameTextField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData.setPredicate(elementProp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String val = Pinyin4jUtil.toPinYinLowercase(newVal);
                return Pinyin4jUtil.toPinYinLowercase(elementProp.getLoginName()).contains(val);
            });
        });
        ipTextfiled.textProperty().addListener((o, oldVal, newVal) -> {
            filteredData.setPredicate(elementProp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String val = Pinyin4jUtil.toPinYinLowercase(newVal);
                return Pinyin4jUtil.toPinYinLowercase(elementProp.getIpaddr()).contains(val);
            });
        });

        onlineUserDataModel.selectedIndexProperty().bind(tableView.getSelectionModel().selectedIndexProperty());


        query();

    }


    private void query() {

        ProcessChain.create()
                .addRunnableInPlatformThread(() -> {
                    onlineUserDataModel.getOnlineUsers().clear();
                    spinner.setVisible(true);
                    contentPane.setDisable(true);
                })
                .addSupplierInExecutor(
                        () -> Request.connector(UserFeign.class).getOnlineUsers()
                )
                .addConsumerInPlatformThread(result -> {

                    onlineUserDataModel.getOnlineUsers().addAll(result);

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
        query();
    }

    @ActionMethod("rest")
    private void rest() {
        userNameTextField.setText("");
        ipTextfiled.setText("");
        searchButton.fire();
    }

    /**
     * @Description:强退
     * @param: []
     * @return: void
     * @auther: liwen
     * @date: 2021/1/4 4:38 下午
     */
    private void retreat() {
        ProcessChain.create()
                .addSupplierInExecutor(
                        () -> Request.connector(UserFeign.class).retreat(onlineUserDataModel.getOnlineUsers().get(onlineUserDataModel.getSelectedIndex()).getLoginName())
                )
                .addConsumerInPlatformThread(result -> {

                    if (result.isRel() == true) {
                        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("强退成功！ ")));
                        query();
                    }

                })
                .withFinal(() -> {
                    spinner.setVisible(false);
                    contentPane.setDisable(false);
                })
                .onException(e -> {
                    e.printStackTrace();
                    snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("强退失败！ ")));

                })
                .run();
    }
}

