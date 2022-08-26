package com.fx.client.gui.uicomponents.main;

import com.fx.client.AppStartup;
import com.fx.client.bean.MenuVoCell;
import com.fx.client.gui.feature.FeatureResourceConsumer;
import com.fx.client.gui.uicomponents.basicInfo.IconSwitch;
import com.fx.client.gui.uicomponents.home.HomeController;
import com.fx.client.gui.uicomponents.login.LoginController;
import com.fx.client.gui.uicomponents.main.components.UserInfoController;
import com.fx.client.store.ApplicatonStore;
import com.fx.client.websocket.WSClient;
import com.fx.server.vo.MenuVO;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.ViewController;
import io.datafx.controller.context.ApplicationContext;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.util.VetoException;
import io.datafx.core.concurrent.ProcessChain;
import io.datafx.eventsystem.Event;
import io.datafx.eventsystem.EventProducer;
import io.datafx.eventsystem.EventTrigger;
import io.datafx.eventsystem.OnEvent;
import javafx.animation.Transition;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @description:
 * @className: MainPresenter
 * @author: liwen
 * @date: 2019-09-02 15:33
 */

@ViewController("/fxml/main/main.fxml")
public class MainController {

    private ObjectProperty<MenuVO> selectItemProperty = new SimpleObjectProperty<>();
    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    private StackPane rootPane;
    private JFXListView<Object> navigationList;
    @FXML
    private Pane backgroundPicturePane;
    @FXML
    private HBox leftHbox;
    @FXML
    private HBox rightHbox;
    //导航按钮
    @FXML
    private JFXHamburger navigationButton;
    //主页按钮
    @FXML
    @ActionTrigger("goHome")
    private JFXButton homeButton;
    @ActionTrigger("showSkinPane")
    private IconSwitch styleNode;
    //刷新按钮
    @FXML
    @EventTrigger("refresh")
    private JFXButton refreshButton;

    //内容面板tabPane
    @FXML
    private JFXTabPane tabPane;


    private JFXPopup popOver;

    @FXML
    @ActionTrigger("userInfo")
    private JFXButton userButton;
    @FXML
    private JFXButton bellButton;


    @FXML
    private Label userLabel;

    @FXML
    private Label roleLabel;


    @FXML
    private JFXDrawersStack drawersStack;

    private JFXDrawer leftDrawer;

    @FXML
    @EventTrigger("refresh")
    private JFXDatePicker datePicker;
    @Inject
    private FeatureResourceConsumer featureResourceConsumer;


    private final HashMap<String, Tab> tabsMap = new HashMap<>();

    @PostConstruct
    public void init() throws FlowException {
        styleNode=new IconSwitch();
        styleNode.setPrefSize(50,32);
        rightHbox.getChildren().add(0,styleNode);
        navigationList=new JFXListView<>();
        navigationList.getStyleClass().add("navigation-list");
        leftDrawer = new JFXDrawer();
        leftDrawer.setId("navigation-menu");
        leftDrawer.setSidePane(navigationList);
        leftDrawer.setDefaultDrawerSize(240);
        leftDrawer.setResizeContent(true);
        leftDrawer.setOverLayVisible(false);
        leftDrawer.setResizableOnDrag(true);

        ApplicationContext.getInstance().register(this, MainController.class);
        userLabel.textProperty().bind(ApplicatonStore.nameProperty());
        roleLabel.textProperty().bind(ApplicatonStore.getRoles().asString());
        userButton.textProperty().bind(ApplicatonStore.nameProperty());
        try {
            homeButton.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".home-outline"));
            refreshButton.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".shuaxin1"));
            bellButton.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".cc-bell-o"));
            userButton.setGraphic(SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".ChevronDownCircle"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        datePicker.setEditable(false);
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshButton.fire();
        });
        leftDrawer.setSidePane(navigationList);
        leftDrawer.setOverLayVisible(false);
        leftDrawer.setResizeContent(true);
        leftDrawer.setResizableOnDrag(true);

        // create and setup search popover
        backgroundPicturePane.setMouseTransparent(true);

        leftDrawer.setOnDrawerOpening(e -> {
            final Transition animation = navigationButton.getAnimation();
            animation.setRate(1);
            animation.play();
        });
        leftDrawer.setOnDrawerClosing(e -> {
            final Transition animation = navigationButton.getAnimation();
            animation.setRate(-1);
            animation.play();
        });
        navigationButton.setOnMouseClicked(e -> {
            drawersStack.toggle(leftDrawer);
        });

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Object value = newValue.getUserData();
            for (Object item : navigationList.getItems()) {
                if (item instanceof MenuVO) {
                    if (item == value) {
                        navigationList.getSelectionModel().select(value);
                        break;
                    }

                } else if (item instanceof JFXListView) {
                    JFXListView<MenuVO> jfxListView = (JFXListView<MenuVO>) item;
                    for (MenuVO menuVO : jfxListView.getItems()) {
                        if (menuVO == value) {
                            navigationList.getSelectionModel().select(2);
                            jfxListView.getSelectionModel().select(menuVO);

                            break;
                        }
                    }
                }

            }

        });
        drawersStack.toggle(leftDrawer);
        drawersStack.setEffect(null);
        ApplicatonStore.styleProperty().bindBidirectional( styleNode.selectedProperty());
        styleNode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                showSkinPane();
            } catch (VetoException e) {
                e.printStackTrace();
            } catch (FlowException e) {
                e.printStackTrace();
            }
        });
        featureResourceConsumer.consumeResource(this);

        navigationList.setCellFactory(listView -> new JFXListCell<Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item instanceof MenuVO) {
                        MenuVO menuVO = (MenuVO) item;
                        try {
                            Label label = new Label(menuVO.getTitle(), SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + "." + menuVO.getIcon()));
                            setGraphic(label);
                            setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    setGraphic(null);
                }
            }
        });
        navigationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof MenuVO) {
                try {
                    addTab(((MenuVO) newValue).getTitle(), SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + "." + ((MenuVO) newValue).getIcon()), Class.forName(((MenuVO) newValue).getHref()), newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        navigationList.depthProperty().setValue(0);
        navigationList.expandedProperty().set(true);
        initData();
    }

    private JFXPopup getPopOver() {
        if (popOver == null) {

            try {
                popOver = new JFXPopup(new Flow(UserInfoController.class).start());
            } catch (FlowException e) {
                e.printStackTrace();
            }
        }
        return popOver;
    }

    @EventProducer("refresh")
    private String refresh() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


    private void initData() {

        ListProperty<MenuVoCell> listProperty = ApplicatonStore.getMenuVoCells();
        listProperty.sort((o1, o2) -> o1.getMenuVO().getOrderNum().compareTo(o2.getMenuVO().getOrderNum()));

        for (MenuVoCell menuVoCell : listProperty) {
            if (menuVoCell.getChildrenMenus().isEmpty()) {
                navigationList.getItems().add(menuVoCell.getMenuVO());

            } else {
                JFXListView<MenuVO> listView = new JFXListView<MenuVO>();
                listView.getStyleClass().add("sublist");
                Label label = null;
                try {
                    label = new Label(menuVoCell.getMenuVO().getTitle(), SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + "." + menuVoCell.getMenuVO().getIcon()));
                    label.getStyleClass().add("sublist-label");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                label.setTextAlignment(TextAlignment.LEFT);
                listView.setGroupnode(label);
                listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue instanceof MenuVO) {
                        try {
                            addTab(newValue.getTitle(), SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + "." + newValue.getIcon()), Class.forName(newValue.getHref()), newValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                listView.setCellFactory(lv -> new JFXListCell<MenuVO>() {
                    @Override
                    public void updateItem(MenuVO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item instanceof MenuVO) {
                                MenuVO menuVO = (MenuVO) item;
                                try {
                                    Label label = new Label(menuVO.getTitle(), SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + "." + menuVO.getIcon()));
                                    setText("");
                                    setGraphic(label);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                });
                ObservableList<MenuVO> childrenMenus = menuVoCell.getChildrenMenus();
                childrenMenus.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
                for (MenuVO menuVO : childrenMenus) {
                    listView.getItems().add(menuVO);
                }
                listView.setExpanded(true);
                navigationList.getItems().add(listView);

            }
        }

        navigationList.getSelectionModel().select(0);
    }


    /**
     * @Description:
     * @param: [controllerClass]
     * @return: void
     * @auther: liwen
     * @date: 2020/6/28 9:57 上午
     */
    public <T> void addTab(String title, Node icon, Class<T> controllerClass, Object userData) {
        addTab(title, icon, new Flow(controllerClass), userData);
    }

    /**
     * @Description:添加tab页
     * @param: [flow]
     * @return: void
     * @auther: liwen
     * @date: 2020/6/28 9:57 上午
     */
    public <T> void addTab(String title, Node icon, Flow flow, Object userData) {
        FlowHandler flowHandler = flow.createHandler();
        Tab tab = tabsMap.get(title);

        if (tab == null) {

            tab = new Tab(title);
            tab.setUserData(userData);
            tab.setGraphic(icon);

            try {
                StackPane node = flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT));
                node.getStyleClass().addAll("tab-content");
                node.setPadding(new Insets(15, 20, 15, 20));
                tab.setContent(node);
                featureResourceConsumer.consumeResource(flowHandler.getCurrentViewContext().getController());

            } catch (FlowException e) {
                e.printStackTrace();
            }
            tabPane.getTabs().add(tab);
            tabsMap.put(title, tab);
            tab.setOnClosed(event -> {
                tabsMap.remove(title);
                try {
                    flowHandler.getCurrentViewContext().destroy();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

        if ("主页".equals(title)) {
            tab.setClosable(false);
        }
        tabPane.getSelectionModel().select(tab);

    }

    @ActionMethod("goHome")
    private void goHome() {
        try {
            addTab("主页", SVGGlyphLoader.getIcoMoonGlyph(ApplicatonStore.ICON_FONT_KEY + ".home-outline"), HomeController.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ActionMethod("userInfo")
    private void userInfo() throws VetoException, FlowException {


        getPopOver().show(userButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, userButton.getHeight());
    }

    @ActionMethod("showSkinPane")
    private void showSkinPane() throws VetoException, FlowException {
        ProcessChain.create().addRunnableInPlatformThread(() -> {
            String style1 = AppStartup.class.getResource("/css/app-light.css").toExternalForm();
            String style2 = AppStartup.class.getResource("/css/app-dark.css").toExternalForm();
            if (styleNode.isSelected()) {


                styleNode.getScene().getStylesheets().removeAll(style1);
                styleNode.getScene().getStylesheets().addAll(style2);
            } else {

                styleNode.getScene().getStylesheets().removeAll(style2);
                styleNode.getScene().getStylesheets().addAll(style1);
            }
        }).run();

    }


    @PreDestroy
    public void destroy() {
    }

    /**
     * @Description:退出登录
     * @param: [event]
     * @return: void
     * @auther: liwen
     * @date: 2020/8/20 6:33 下午
     */
    @OnEvent("dropOut")
    private void signOut(Event<String> event) {
        getPopOver().hide();
        ApplicatonStore.clearPermissionInfo();
        try {
            actionHandler.navigate(LoginController.class);
        } catch (VetoException e) {
            e.printStackTrace();
        } catch (FlowException e) {
            e.printStackTrace();
        }
        WSClient.getInstance().close();


    }

}



