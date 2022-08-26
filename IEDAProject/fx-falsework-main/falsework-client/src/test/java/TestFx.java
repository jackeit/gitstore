/**
 * @description:
 * @className: TestFx
 * @author: liwen
 * @date: 2020/9/11 10:53
 */

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class TestFx extends Application {


    //正面视图
    public StackPane frontNode;
    //反面视图
    public StackPane backNode;
    //是否翻转
    boolean flipped = false;
    //翻转角度
    DoubleProperty time = new SimpleDoubleProperty(Math.PI / 2);
    //正面翻转特效
    PerspectiveTransform frontEffect = new PerspectiveTransform();
    //反面翻转特效
    PerspectiveTransform backEffect = new PerspectiveTransform();
    Timeline anim = new Timeline();
    Timeline anim1 = new Timeline();

    @Override
    public void start(Stage primaryStage) throws Exception {


        StackPane rootPane = new StackPane();

        rootPane.getChildren().addAll(create());
        Scene scene = new Scene(rootPane, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Welcome");
        primaryStage.show();
    }

    private StackPane create() {

        Button a=   new Button("正面视图");
        Button b=   new Button("反面视图");
        a.setOnAction(event -> {

            frontNode.setEffect(frontEffect);
            backNode.setEffect(backEffect);
            anim.play();
        });
        b.setOnAction(event -> {

            frontNode.setEffect(frontEffect);
            backNode.setEffect(backEffect);
            anim1.play();
        });
        frontNode = new StackPane();
        frontNode.getChildren().add(a);
        frontNode.setStyle("-fx-background-color: #11aa55");
        backNode = new StackPane();
        backNode.getChildren().add(b);

        backNode.setStyle("-fx-background-color: #88bb55");
        KeyFrame frame1 = new KeyFrame(Duration.ZERO, new KeyValue(time,
                Math.PI / 2, Interpolator.LINEAR));
        KeyFrame frame2 = new KeyFrame(Duration.seconds(1),
                new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        frontNode.setEffect(null);
                        backNode.setEffect(null);
                    }

                }, new KeyValue(time, -Math.PI / 2, Interpolator.LINEAR));

        KeyFrame frame3 = new KeyFrame(Duration.seconds(1), new EventHandler() {
            @Override
            public void handle(Event event) {
                frontNode.setEffect(null);
                backNode.setEffect(null);
            }

        }, new KeyValue(time,
                Math.PI / 2, Interpolator.LINEAR));
        KeyFrame frame4 = new KeyFrame(Duration.ZERO, new KeyValue(time, -Math.PI / 2, Interpolator.LINEAR));

        time.addListener((observable, oldValue, newValue) -> {
            setPT(frontEffect, time.get());
            setPT(backEffect, time.get() - Math.PI);
        });
        anim.getKeyFrames().addAll(frame1, frame2);

        anim1.getKeyFrames().addAll(frame4, frame3);


        backNode.visibleProperty().bind(
                Bindings.when(time.lessThan(0)).then(true).otherwise(false));
        frontNode.visibleProperty().bind(backNode.visibleProperty().not());


        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(460, 450);
        stackPane.setMaxSize(460, 450);
        stackPane.getChildren().addAll(backNode, frontNode);
        return stackPane;
    }

    private void setPT(PerspectiveTransform pt, double t) {
        double width = 450;
        double height = 450;
        double radius = width / 2;
        double back = height / 10;
        pt.setUlx(radius - Math.sin(t) * radius);
        pt.setUly(0 - Math.cos(t) * back);
        pt.setUrx(radius + Math.sin(t) * radius);
        pt.setUry(0 + Math.cos(t) * back);
        pt.setLrx(radius + Math.sin(t) * radius);
        pt.setLry(height - Math.cos(t) * back);
        pt.setLlx(radius - Math.sin(t) * radius);
        pt.setLly(height + Math.cos(t) * back);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
