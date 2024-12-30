package clients.packing;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The standalone Packing Client
 */
public class PackingClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String stockURL = getParameters().getRaw().size() < 1
                ? Names.STOCK_RW // Default location
                : getParameters().getRaw().get(0);

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);

        displayGUI(primaryStage, mrf);
    }

    private void displayGUI(Stage stage, MiddleFactory mf) {
        PackingModel model = new PackingModel(mf);
        PackingView view = new PackingView(); // Use no-argument constructor
        PackingController controller = new PackingController(model, view);

        model.addObserver(view); // Add view as an observer
        view.setController(controller);

        Scene scene = new Scene(view.getRootNode(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Packing Client (MVC RMI)");
        stage.show();
    }
}
