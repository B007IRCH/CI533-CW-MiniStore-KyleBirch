package clients.customer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Observable;
import java.util.Observer;

public class CustomerView implements Observer {
  private final VBox rootNode;
  private final Label pageTitle;
  private final Label theAction;
  private final TextField theInput;
  private final TextArea theOutput;
  private final Button theBtCheck;
  private final Button theBtClear;

  public CustomerView() {
    rootNode = new VBox(10);
    pageTitle = new Label("Search products");
    theAction = new Label();
    theInput = new TextField();
    theOutput = new TextArea();
    theBtCheck = new Button("Check");
    theBtClear = new Button("Clear");

    rootNode.getChildren().addAll(pageTitle, theAction, theInput, theOutput, theBtCheck, theBtClear);
  }

  public VBox getRootNode() {
    return rootNode;
  }

  public void setController(CustomerController controller) {
    theBtCheck.setOnAction(e -> controller.doCheck(theInput.getText()));
    theBtClear.setOnAction(e -> controller.doClear());
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof String) {
      theAction.setText((String) arg);
      theOutput.appendText((String) arg + "\n");
    }
  }
}
