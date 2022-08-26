import com.intellij.openapi.util.IconLoader;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.InputStream;
import java.net.MalformedURLException;


public class TigerExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("SVG Display sample");

        // load the sample svg file
        InputStream svgFile = 
              getClass().getResourceAsStream("/test.svg");
        Icon icon = IconLoader.getIcon("/test.svg");

        SwingNode svgImage = new SwingNode();
        JLabel label=new JLabel(icon);
        label.setText("");
        svgImage.setContent(label);

        StackPane stackPane=new StackPane(svgImage);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}