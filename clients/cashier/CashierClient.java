package clients.cashier;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

/**
 * The standalone Cashier Client.
 */
public class CashierClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String stockURL = getParameters().getRaw().size() < 1
                ? Names.STOCK_RW
                : getParameters().getRaw().get(0);
        String orderURL = getParameters().getRaw().size() < 2
                ? Names.ORDER
                : getParameters().getRaw().get(1);

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);

        displayGUI(primaryStage, mrf);
    }

    private void displayGUI(Stage stage, MiddleFactory mf) {
        CashierModel model = new CashierModel(mf);
        CashierView view = new CashierView();
        CashierController controller = new CashierController(model, view);

        model.addObserver(view); // Add view as an observer
        view.setController(controller);

        Scene scene = new Scene(view.getRootNode(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Cashier Client (MVC RMI)");
        stage.show();

        model.askForUpdate(); // Trigger an initial update
    }
}
