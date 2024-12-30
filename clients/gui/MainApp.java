package clients.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        Label welcomeLabel = new Label("Welcome to MiniStore");
        root.getChildren().add(welcomeLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("MiniStore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}