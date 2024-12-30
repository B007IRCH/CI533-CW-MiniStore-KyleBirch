package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Starts all the clients (user interface) as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Kyle Seam Birch University of Brighton
 * @version year-2024
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("MiniStore Application");

    TabPane tabPane = new TabPane();

    // Customer Client Tab
    Tab customerTab = new Tab("Customer");
    BorderPane customerPane = new BorderPane();
    startCustomerGUI_MVC(customerPane);
    customerTab.setContent(customerPane);

    // Cashier Client Tab
    Tab cashierTab = new Tab("Cashier");
    BorderPane cashierPane = new BorderPane();
    startCashierGUI_MVC(cashierPane);
    cashierTab.setContent(cashierPane);

    // Packing Client Tab
    Tab packingTab = new Tab("Packing");
    BorderPane packingPane = new BorderPane();
    startPackingGUI_MVC(packingPane);
    packingTab.setContent(packingPane);

    // BackDoor Client Tab
    Tab backDoorTab = new Tab("BackDoor");
    BorderPane backDoorPane = new BorderPane();
    startBackDoorGUI_MVC(backDoorPane);
    backDoorTab.setContent(backDoorPane);

    tabPane.getTabs().addAll(customerTab, cashierTab, packingTab, backDoorTab);

    Scene scene = new Scene(tabPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Start the Customer client GUI
   * @param pane A pane to attach the Customer GUI
   */
  public void startCustomerGUI_MVC(BorderPane pane) {
    MiddleFactory mlf = new LocalMiddleFactory();
    CustomerModel model = new CustomerModel(mlf);
    CustomerView view = new CustomerView();
    CustomerController cont = new CustomerController(model, view);
    view.setController(cont);
    model.addObserver(view);
    pane.setCenter(view.getRootNode());
  }

  /**
   * Start the Cashier client GUI
   * @param pane A pane to attach the Cashier GUI
   */
  public void startCashierGUI_MVC(BorderPane pane) {
    MiddleFactory mlf = new LocalMiddleFactory();
    CashierModel model = new CashierModel(mlf);
    CashierView view = new CashierView();
    CashierController cont = new CashierController(model, view);
    view.setController(cont);
    model.addObserver(view);
    model.askForUpdate();
    pane.setCenter(view.getRootNode());
  }

  /**
   * Start the Packing client GUI
   * @param pane A pane to attach the Packing GUI
   */
  public void startPackingGUI_MVC(BorderPane pane) {
    MiddleFactory mlf = new LocalMiddleFactory();
    PackingModel model = new PackingModel(mlf);
    PackingView view = new PackingView();
    PackingController cont = new PackingController(model, view);
    view.setController(cont);
    model.addObserver(view);
    pane.setCenter(view.getRootNode());
  }

  /**
   * Start the BackDoor client GUI
   * @param pane A pane to attach the BackDoor GUI
   */
  public void startBackDoorGUI_MVC(BorderPane pane) {
    MiddleFactory mlf = new LocalMiddleFactory();
    BackDoorModel model = new BackDoorModel(mlf);
    BackDoorView view = new BackDoorView();
    BackDoorController cont = new BackDoorController(model, view);
    view.setController(cont);
    model.addObserver(view);
    pane.setCenter(view.getRootNode());
  }
}