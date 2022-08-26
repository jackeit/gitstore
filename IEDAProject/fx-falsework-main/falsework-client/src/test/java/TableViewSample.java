
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @Description:
 *
 * @param: 
 * @return: 
 * @auther: liwen
 * @date: 2020/11/6 6:29 下午
 */
public class TableViewSample extends Application {

    private final TableView table = new TableView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(300);
        stage.setHeight(500);

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        for (int i = 0; i < 50; i++) {
            TableColumn column = new TableColumn("C" + i);
            column.setCellValueFactory(new PropertyValueFactory<>("data"));
            table.getColumns().addAll(column);

        }

        ObservableList<DataVaule> innerList = FXCollections.observableArrayList();
        ListProperty<DataVaule> dataVaules = new SimpleListProperty<>(innerList);

        for (int i = 0; i < 1000; i++) {
            dataVaules.add(new DataVaule(i));
        }
        table.setTableMenuButtonVisible(true);
        table.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0, 4, 4, 4))));
        table.setItems(dataVaules);
        table.setPrefSize(500, 600);

        
        final VBox vbox = new VBox();
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        scene.setRoot(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public class DataVaule {
        private SimpleIntegerProperty data = new SimpleIntegerProperty();

        public DataVaule(int data) {
            this.data = new SimpleIntegerProperty(data);
        }

        public int getData() {
            return data.get();
        }

        public SimpleIntegerProperty dataProperty() {
            return data;
        }

        public void setData(int data) {
            this.data.set(data);
        }
    }
}