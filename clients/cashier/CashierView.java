package clients.cashier;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Observable;
import java.util.Observer;

public class CashierView implements Observer {
  private final VBox rootNode;
  private final Label pageTitle;
  private final Label theAction;
  private final TextField theInput;
  private final TextArea theOutput;
  private final Button theBtCheck;
  private final Button theBtBuy;
  private final Button theBtBought;

  public CashierView() {
    rootNode = new VBox(10);
    pageTitle = new Label("Thank You for Shopping at MiniStore");
    theAction = new Label();
    theInput = new TextField();
    theOutput = new TextArea();
    theBtCheck = new Button("Check");
    theBtBuy = new Button("Buy");
    theBtBought = new Button("Bought/Pay");

    rootNode.getChildren().addAll(pageTitle, theAction, theInput, theOutput, theBtCheck, theBtBuy, theBtBought);
  }

  public VBox getRootNode() {
    return rootNode;
  }

  public void setController(CashierController controller) {
    theBtCheck.setOnAction(e -> controller.doCheck(theInput.getText()));
    theBtBuy.setOnAction(e -> controller.doBuy());
    theBtBought.setOnAction(e -> controller.doBought());
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof String) {
      theAction.setText((String) arg);
      theOutput.appendText((String) arg + "\n");
    }
  }
}
