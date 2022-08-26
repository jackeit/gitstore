package com.fx.client.gui.uicomponents.admin;

import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/admin/system/system.fxml", title = "平台管理")
public class Browser extends StackPane {

    @FXML
    private WebView browser;

    @PostConstruct
    public void init() {

        // load the web page
        browser.getEngine().load("http://192.168.1.82:8089/dist");

    }


}