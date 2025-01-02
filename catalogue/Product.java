package catalogue;

import javafx.beans.property.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product in the catalog, holding information
 * about product number, description, price, and stock level.
 * Supports JavaFX properties for GUI bindings.
 * @author  Kyle Sean
 * @version 2.2
 */
public class Product implements Serializable {
  private static final long serialVersionUID = 20092506L;

  private final StringProperty productNum;       // Product number
  private final StringProperty description;      // Description of product
  private final DoubleProperty price;            // Price of product
  private final IntegerProperty quantity;        // Stock level

  /**
   * Constructs a product with the given details.
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice Price of the product
   * @param aQuantity Stock level
   */
  public Product(String aProductNum, String aDescription, double aPrice, int aQuantity) {
    this.productNum = new SimpleStringProperty(aProductNum);
    this.description = new SimpleStringProperty(aDescription);
    this.price = new SimpleDoubleProperty(aPrice);
    this.quantity = new SimpleIntegerProperty(aQuantity);
  }

  /**
   * Returns the product number.
   * @return Product number
   */
  public String getProductNum() {
    return productNum.get();
  }

  /**
   * Sets the product number.
   * @param aProductNum Product number
   */
  public void setProductNum(String aProductNum) {
    this.productNum.set(aProductNum);
  }

  /**
   * Returns the product number property (for JavaFX bindings).
   * @return StringProperty of product number
   */
  public StringProperty productNumProperty() {
    return productNum;
  }

  /**
   * Returns the description of the product.
   * @return Product description
   */
  public String getDescription() {
    return description.get();
  }

  /**
   * Sets the description of the product.
   * @param aDescription Product description
   */
  public void setDescription(String aDescription) {
    this.description.set(aDescription);
  }

  /**
   * Returns the description property (for JavaFX bindings).
   * @return StringProperty of product description
   */
  public StringProperty descriptionProperty() {
    return description;
  }

  /**
   * Returns the price of the product.
   * @return Product price
   */
  public double getPrice() {
    return price.get();
  }

  /**
   * Sets the price of the product.
   * @param aPrice Product price
   */
  public void setPrice(double aPrice) {
    this.price.set(aPrice);
  }

  /**
   * Returns the price property (for JavaFX bindings).
   * @return DoubleProperty of product price
   */
  public DoubleProperty priceProperty() {
    return price;
  }

  /**
   * Returns the stock level of the product.
   * @return Product quantity
   */
  public int getQuantity() {
    return quantity.get();
  }

  /**
   * Sets the stock level of the product.
   * @param aQuantity Product quantity
   */
  public void setQuantity(int aQuantity) {
    this.quantity.set(aQuantity);
  }

  /**
   * Returns the quantity property (for JavaFX bindings).
   * @return IntegerProperty of product quantity
   */
  public IntegerProperty quantityProperty() {
    return quantity;
  }

  /**
   * Returns a string representation of the product.
   * @return String representation of the product
   */
  @Override
  public String toString() {
    return "Product{" +
            "ProductNum='" + productNum.get() + '\'' +
            ", Description='" + description.get() + '\'' +
            ", Price=" + price.get() +
            ", Quantity=" + quantity.get() +
            '}';
  }

  /**
   * Compares this product to another object for equality.
   * Products are considered equal if their product numbers match.
   * @param o Object to compare
   * @return True if the product numbers are the same, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(productNum.get(), product.productNum.get());
  }

  /**
   * Generates a hash code for the product based on its product number.
   * @return Hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(productNum.get());
  }
}
