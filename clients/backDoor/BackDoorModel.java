package clients.backDoor;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;

import java.util.Observable;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel extends Observable {
  private Basket theBasket;
  private StockReadWriter theStock;

  public BackDoorModel(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReadWriter(); // Database access
    } catch (Exception e) {
      DEBUG.error("BackDoorModel.constructor\n%s", e.getMessage());
    }
    theBasket = makeBasket(); // Initialize basket
  }

  public void doQuery(String productNum) {
    String theAction = "";
    try {
      if (theStock.exists(productNum)) {
        Product pr = theStock.getDetails(productNum);
        theAction = String.format("%s : %7.2f (%2d)", pr.getDescription(), pr.getPrice(), pr.getQuantity());
      } else {
        theAction = "Unknown product number " + productNum;
      }
    } catch (StockException e) {
      theAction = e.getMessage();
    }
    setChanged();
    notifyObservers(theAction);
  }

  public void doRStock(String productNum, String quantity) {
    String theAction = "";
    try {
      int amount = Integer.parseInt(quantity.trim());
      if (amount < 0) throw new NumberFormatException();
      if (theStock.exists(productNum)) {
        theStock.addStock(productNum, amount);
        Product pr = theStock.getDetails(productNum);
        theBasket.add(pr);
        theAction = String.format("Restocked %s with %d units", pr.getDescription(), amount);
      } else {
        theAction = "Unknown product number " + productNum;
      }
    } catch (NumberFormatException e) {
      theAction = "Invalid quantity";
    } catch (StockException e) {
      theAction = e.getMessage();
    }
    setChanged();
    notifyObservers(theAction);
  }

  public void doClear() {
    theBasket.clear();
    setChanged();
    notifyObservers("Basket cleared. Enter Product Number.");
  }

  protected Basket makeBasket() {
    return new Basket();
  }
}
