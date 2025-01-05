package catalogue;

import javafx.beans.property.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product in the catalog, holding information
 * about product number, description, price, stock level, category, and company.
 * Supports JavaFX properties for GUI bindings.
 */
public class Product implements Serializable {
  private static final long serialVersionUID = 20092506L;

  private final StringProperty productNum;       // Product number
  private final StringProperty description;      // Description of product
  private final DoubleProperty price;            // Price of product
  private final IntegerProperty quantity;        // Stock level
  private final StringProperty category;         // Category of the product
  private final StringProperty company;          // Company manufacturing/selling the product

  /**
   * Constructs a product with the given details.
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice Price of the product
   * @param aQuantity Stock level
   * @param aCategory Category of the product
   * @param aCompany Company manufacturing/selling the product
   */
  public Product(String aProductNum, String aDescription, double aPrice, int aQuantity, String aCategory, String aCompany) {
    this.productNum = new SimpleStringProperty(aProductNum);
    this.description = new SimpleStringProperty(aDescription);
    this.price = new SimpleDoubleProperty(aPrice);
    this.quantity = new SimpleIntegerProperty(aQuantity);
    this.category = new SimpleStringProperty(aCategory);
    this.company = new SimpleStringProperty(aCompany);
  }

  public String getProductNum() { return productNum.get(); }
  public void setProductNum(String aProductNum) { this.productNum.set(aProductNum); }
  public StringProperty productNumProperty() { return productNum; }

  public String getDescription() { return description.get(); }
  public void setDescription(String aDescription) { this.description.set(aDescription); }
  public StringProperty descriptionProperty() { return description; }

  public double getPrice() { return price.get(); }
  public void setPrice(double aPrice) { this.price.set(aPrice); }
  public DoubleProperty priceProperty() { return price; }

  public int getQuantity() { return quantity.get(); }
  public void setQuantity(int aQuantity) { this.quantity.set(aQuantity); }
  public IntegerProperty quantityProperty() { return quantity; }

  public String getCategory() { return category.get(); }
  public void setCategory(String aCategory) { this.category.set(aCategory); }
  public StringProperty categoryProperty() { return category; }

  public String getCompany() { return company.get(); }
  public void setCompany(String aCompany) { this.company.set(aCompany); }
  public StringProperty companyProperty() { return company; }

  @Override
  public String toString() {
    return "Product{" +
            "ProductNum='" + productNum.get() + '\'' +
            ", Description='" + description.get() + '\'' +
            ", Price=" + price.get() +
            ", Quantity=" + quantity.get() +
            ", Category='" + category.get() + '\'' +
            ", Company='" + company.get() + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(productNum.get(), product.productNum.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(productNum.get());
  }
}