package com.fx.client;

import com.fx.client.gui.uicomponents.login.LoginController;
import com.fx.client.store.ApplicatonStore;
import com.fx.client.websocket.WSClient;
import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.netflix.config.ConfigurationManager;
import io.datafx.controller.context.ApplicationContext;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;


/**
 * @description: 应用启动类
 * @className: AppStartup
 * @author: liwen
 * @date: 2019-08-26 16:24
 */
public class AppStartup extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    private ApplicationContext applicationContext = ApplicationContext.getInstance();

    @Override
    public void init() throws Exception {

        InputStream in = AppStartup.class.getClassLoader().getResourceAsStream("fonts/msyh.ttf");
        Font font1 = Font.loadFont(in, 12);

//        System.err.println(font1.getName() + "\t" + font1.getFamily());
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");
//


    }

    @Override
    public void start(Stage stage) throws Exception {


        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(AppStartup.class.getResourceAsStream("/fonts/icon_font/iconfont.svg"),
                        ApplicatonStore.ICON_FONT_KEY);

            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        Flow contentFlow = new Flow(LoginController.class);
        AnimatedFlowContainer container = new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT);
        flowContext = new ViewFlowContext();
        final FlowHandler contentFlowHandler = contentFlow.createHandler(flowContext);
        applicationContext.register(stage, Stage.class);
        applicationContext.register("ContentFlowHandler", contentFlowHandler);
        contentFlowHandler.start(container);

        JFXDecorator wfxDecorator = new JFXDecorator(stage, container.getView());
        wfxDecorator.setCustomMaximize(true);
        wfxDecorator.setGraphic(new SVGGlyph(""));

        Scene scene = new Scene(wfxDecorator, 1000, 750);
        stage.setTitle("JavaFX Welcome");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        /*全局样式*/
        scene.getStylesheets().addAll(JFoenixResources.load("/css/app-fonts.css").toExternalForm(), AppStartup.class.getResource("/css/login.css").toExternalForm(), AppStartup.class.getResource("/css/app-light.css").toExternalForm());

    }

    @Override
    public void stop() throws Exception {
        WSClient.getInstance().close();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
