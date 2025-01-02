package catalogue;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product in the catalog, holding information
 * about product number, description, price, and stock level.
 * @author  Kyle Sean
 * @version 2.1
 */
public class Product implements Serializable {
  private static final long serialVersionUID = 20092506L;
  private String theProductNum;       // Product number
  private String theDescription;      // Description of product
  private double thePrice;            // Price of product
  private int theQuantity;            // Stock level

  /**
   * Constructs a product with the given details.
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice Price of the product
   * @param aQuantity Stock level
   */
  public Product(String aProductNum, String aDescription,
                 double aPrice, int aQuantity) {
    theProductNum = aProductNum;     // Product number
    theDescription = aDescription;  // Description of product
    thePrice = aPrice;              // Price of product
    theQuantity = aQuantity;        // Stock level
  }

  /**
   * Returns the product number.
   * @return Product number
   */
  public String getProductNum() {
    return theProductNum;
  }

  /**
   * Sets the product number.
   * @param aProductNum Product number
   */
  public void setProductNum(String aProductNum) {
    this.theProductNum = aProductNum;
  }

  /**
   * Returns the description of the product.
   * @return Product description
   */
  public String getDescription() {
    return theDescription;
  }

  /**
   * Sets the description of the product.
   * @param aDescription Product description
   */
  public void setDescription(String aDescription) {
    this.theDescription = aDescription;
  }

  /**
   * Returns the price of the product.
   * @return Product price
   */
  public double getPrice() {
    return thePrice;
  }

  /**
   * Sets the price of the product.
   * @param aPrice Product price
   */
  public void setPrice(double aPrice) {
    this.thePrice = aPrice;
  }

  /**
   * Returns the stock level of the product.
   * @return Product quantity
   */
  public int getQuantity() {
    return theQuantity;
  }

  /**
   * Sets the stock level of the product.
   * @param aQuantity Product quantity
   */
  public void setQuantity(int aQuantity) {
    this.theQuantity = aQuantity;
  }

  /**
   * Returns a string representation of the product.
   * @return String representation of the product
   */
  @Override
  public String toString() {
    return "Product{" +
            "ProductNum='" + theProductNum + '\'' +
            ", Description='" + theDescription + '\'' +
            ", Price=" + thePrice +
            ", Quantity=" + theQuantity +
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
    return Objects.equals(theProductNum, product.theProductNum);
  }

  /**
   * Generates a hash code for the product based on its product number.
   * @return Hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(theProductNum);
  }
}
