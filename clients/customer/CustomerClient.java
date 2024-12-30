package clients.customer;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The standalone Customer Client
 */
public class CustomerClient extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    String stockURL = getParameters().getRaw().size() < 1
            ? Names.STOCK_R // Default location
            : getParameters().getRaw().get(0);

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);

    displayGUI(primaryStage, mrf);
  }

  private void displayGUI(Stage stage, MiddleFactory mf) {
    CustomerModel model = new CustomerModel(mf);
    CustomerView view = new CustomerView(); // No-argument constructor
    CustomerController controller = new CustomerController(model, view);

    model.addObserver(view); // Add observer to the model
    view.setController(controller);

    Scene scene = new Scene(view.getRootNode(), 600, 400);
    stage.setScene(scene);
    stage.setTitle("Customer Client (MVC RMI)");
    stage.show();
  }
}
