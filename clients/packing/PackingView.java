package clients.packing;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class PackingView {
  private final VBox rootNode;
  private final Label pageTitle;
  private final Label theAction;
  private final TextArea theOutput;
  private final Button theBtPack;

  public PackingView() {
    rootNode = new VBox(10);
    pageTitle = new Label("Packing Bought Order");
    theAction = new Label();
    theOutput = new TextArea();
    theBtPack = new Button("Packed");

    rootNode.getChildren().addAll(pageTitle, theAction, theOutput, theBtPack);
  }

  public VBox getRootNode() {
    return rootNode;
  }

  public void setController(PackingController controller) {
    theBtPack.setOnAction(e -> controller.doPacked());
  }
}