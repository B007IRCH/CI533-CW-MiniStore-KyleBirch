package clients.backDoor;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Observable;
import java.util.Observer;

public class BackDoorView implements Observer {
  private final VBox rootNode;
  private final Label pageTitle;
  private final Label theAction;
  private final TextField theInput;
  private final TextField theInputNo;
  private final TextArea theOutput;
  private final Button theBtClear;
  private final Button theBtRStock;
  private final Button theBtQuery;

  public BackDoorView() {
    rootNode = new VBox(10);
    pageTitle = new Label("Staff check and manage stock");
    theAction = new Label();
    theInput = new TextField();
    theInputNo = new TextField("0");
    theOutput = new TextArea();
    theOutput.setEditable(false);
    theBtClear = new Button("Clear");
    theBtRStock = new Button("Add");
    theBtQuery = new Button("Query");

    rootNode.getChildren().addAll(pageTitle, theAction, theInput, theInputNo, theOutput, theBtClear, theBtRStock, theBtQuery);
  }

  public VBox getRootNode() {
    return rootNode;
  }

  public void setController(BackDoorController controller) {
    theBtQuery.setOnAction(e -> controller.doQuery(theInput.getText()));
    theBtRStock.setOnAction(e -> controller.doRStock(theInput.getText(), theInputNo.getText()));
    theBtClear.setOnAction(e -> controller.doClear());
  }

  public void updateView(String action) {
    theAction.setText(action);
    theOutput.appendText(action + "\n");
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof String) {
      updateView((String) arg);
    }
  }
}
