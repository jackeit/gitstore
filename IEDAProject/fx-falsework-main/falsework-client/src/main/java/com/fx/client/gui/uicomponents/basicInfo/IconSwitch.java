package com.fx.client.gui.uicomponents.basicInfo;

import com.fx.client.gui.uicomponents.basicInfo.skin.IconSwitchSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;

/**
 * @version： 0.0.1
 * @description:
 * @className: IconSwitch
 * @author: liwen
 * @date: 2021/2/22 10:21
 */
public class IconSwitch extends Control {

    // CSS pseudo classes
    private BooleanProperty selected;

    private final Label symbol;

    // ******************** Constructors **************************************
    public IconSwitch() {
        getStyleClass().addAll("icon-switch");
        symbol = new Label();
        symbol.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    // ******************** Methods *******************************************
    public final boolean isSelected() {
        return null == selected ? false : selected.get();
    }

    public final void setSelected(final boolean ON) {
        selectedProperty().set(ON);
    }

    public final BooleanProperty selectedProperty() {
        if (null == selected) {
            selected = new SimpleBooleanProperty();
        }
        return selected;
    }

    // ******************** Style related *************************************
    @Override
    protected Skin createDefaultSkin() {
        return new IconSwitchSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("/css/iconswitch.css").toExternalForm();
    }


    public Label getSymbol() {
        return symbol;
    }
}
