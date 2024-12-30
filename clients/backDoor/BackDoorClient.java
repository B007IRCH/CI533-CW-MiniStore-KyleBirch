package clients.backDoor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

/**
 * The standalone BackDoor Client
 */
public class BackDoorClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String stockURL = getParameters().getRaw().size() < 1
                ? Names.STOCK_RW  // Default location
                : getParameters().getRaw().get(0); // Supplied location
        String orderURL = getParameters().getRaw().size() < 2
                ? Names.ORDER  // Default location
                : getParameters().getRaw().get(1); // Supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);

        // Display the GUI
        displayGUI(primaryStage, mrf);
    }

    private void displayGUI(Stage stage, MiddleFactory mf) {
        BackDoorModel model = new BackDoorModel(mf);
        BackDoorView view = new BackDoorView(); // Uses JavaFX VBox-based view
        BackDoorController controller = new BackDoorController(model, view);

        view.setController(controller); // Connect controller
        model.addObserver((observable, action) -> view.updateView((String) action)); // Update the view on model changes

        Scene scene = new Scene(view.getRootNode(), 600, 400); // Define window size
        stage.setTitle("BackDoor Client (MVC RMI)");
        stage.setScene(scene);
        stage.show();
    }
}
