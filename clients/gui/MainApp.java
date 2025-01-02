package clients.gui;

import clients.customer.CartController;
import catalogue.Product;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    private CartController cartController = new CartController();

    @Override
    public void start(Stage primaryStage) {
        // Root layout
        BorderPane root = new BorderPane();

        // Top - Welcome Label
        Label welcomeLabel = new Label("Welcome to MiniStore");
        root.setTop(welcomeLabel);

        // Center - Cart Display
        VBox cartView = new VBox();
        cartView.setSpacing(10);
        ListView<String> cartListView = new ListView<>();
        Label totalLabel = new Label("Total: $0.00");

        // Add cart items to the cart view
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> {
            cartListView.getItems().clear();
            cartController.viewCart().forEach(item -> {
                cartListView.getItems().add(
                        item.getProduct().getDescription() + " x" + item.getQuantity() +
                                " - $" + item.getTotalPrice()
                );
            });
            totalLabel.setText("Total: $" + cartController.getTotalCost());
        });

        cartView.getChildren().addAll(cartListView, totalLabel, viewCartButton);
        root.setCenter(cartView);

        // Bottom - Add and Remove Actions
        HBox actions = new HBox();
        actions.setSpacing(10);

        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(productIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                // Simulate fetching a product (replace with actual logic)
                Product product = new Product(
                        String.valueOf(productId),
                        "Sample Product " + productId,
                        10.00,
                        100
                );
                cartController.addToCart(product, quantity);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added to cart!");
                alert.show();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please enter valid numbers.");
                alert.show();
            }
        });

        Button removeFromCartButton = new Button("Remove from Cart");
        removeFromCartButton.setOnAction(e -> {
            try {
                String productId = productIdField.getText();
                cartController.removeFromCart(productId);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product removed from cart!");
                alert.show();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please enter a valid Product ID.");
                alert.show();
            }
        });

        actions.getChildren().addAll(productIdField, quantityField, addToCartButton, removeFromCartButton);
        root.setBottom(actions);

        // Set scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("MiniStore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
