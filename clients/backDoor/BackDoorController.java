package clients.backDoor;

/**
 * The BackDoor Controller
 */
public class BackDoorController {
  private final BackDoorModel model;
  private final BackDoorView view;

  public BackDoorController(BackDoorModel model, BackDoorView view) {
    this.model = model;
    this.view = view;
  }

  public void doQuery(String pn) {
    model.doQuery(pn);
  }

  public void doRStock(String pn, String quantity) {
    model.doRStock(pn, quantity);
  }

  public void doClear() {
    model.doClear();
  }
}
