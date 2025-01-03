package clients.customer;

import catalogue.Cart;
import catalogue.Product;

import java.util.List;

public class CartController {
    private final Cart cart = new Cart();

    /**
     * Adds a product to the cart.
     * @param product The product to add.
     * @param quantity The quantity of the product.
     */
    public void addToCart(Product product, int quantity) {
        cart.addItem(product, quantity);
        System.out.println("Added " + quantity + " x " + product.getDescription() + " to the cart.");
    }

    /**
     * Removes a product from the cart by product number.
     * @param productNum The product number of the product to remove.
     */
    public void removeFromCart(String productNum) {
        cart.getItems().removeIf(item -> item.getProduct().getProductNum().equals(productNum));
        System.out.println("Removed product with product number " + productNum + " from the cart.");
    }

    /**
     * Removes a product from the cart by its name.
     * @param productName The name of the product to remove.
     */
    public void removeByName(String productName) {
        cart.getItems().removeIf(item -> item.getProduct().getDescription().equalsIgnoreCase(productName));
        System.out.println("Removed product with name " + productName + " from the cart.");
    }

    /**
     * Returns a list of all items in the cart.
     * @return A list of CartItem objects.
     */
    public List<CartItem> viewCart() {
        return cart.getItems();
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        cart.clearCart();
        System.out.println("Cart has been cleared.");
    }

    /**
     * Calculates and returns the total cost of items in the cart.
     * @return The total cost of items in the cart.
     */
    public double getTotalCost() {
        return cart.getTotalCost();
    }
}
