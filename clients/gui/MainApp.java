package clients.gui;

import clients.customer.CartController;
import catalogue.Product;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    private CartController cartController = new CartController();
    private ObservableList<Product> productData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #f5f5f5, #ffffff);");

        // Top - Navigation Bar
        HBox navBar = new HBox();
        navBar.setSpacing(15);
        navBar.setStyle("-fx-background-color: #2A9D8F; -fx-padding: 15; -fx-alignment: center-left;");

        Label logoLabel = new Label("MiniStore");
        logoLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search products...");
        searchBar.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

        Button viewCartButton = new Button("View Cart");
        styleNavButton(viewCartButton);
        viewCartButton.setOnAction(e -> showCartModal());

        Button wishlistButton = new Button("Wishlist");
        styleNavButton(wishlistButton);

        Button adminButton = new Button("Admin");
        styleNavButton(adminButton);

        navBar.getChildren().addAll(logoLabel, searchBar, viewCartButton, wishlistButton, adminButton);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        root.setTop(navBar);

        // Center - Product Cards
        FlowPane productPane = new FlowPane();
        productPane.setPadding(new Insets(20));
        productPane.setHgap(20);
        productPane.setVgap(20);
        productPane.setAlignment(Pos.CENTER);
        productPane.setStyle("-fx-background-color: transparent;");

        for (Product product : loadProductData()) {
            VBox productCard = createProductCard(product);
            productPane.getChildren().add(productCard);
        }

        root.setCenter(productPane);

        // Footer
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #2A9D8F; -fx-padding: 10;");
        footer.setAlignment(Pos.CENTER);
        Label footerLabel = new Label("\u00A9 2025 MiniStore | Contact Us: support@ministore.com");
        footerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        footer.getChildren().add(footerLabel);
        root.setBottom(footer);

        // Set scene and stage
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("MiniStore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.2, 0, 0);");

        // Product Image
        ImageView productImage = new ImageView(new Image("file:sample.jpg")); // Replace with actual image path
        productImage.setFitWidth(150);
        productImage.setFitHeight(150);
        productImage.setPreserveRatio(true);

        // Product Name
        Label productName = new Label(product.getDescription());
        productName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Product Price
        Label productPrice = new Label("$" + product.getPrice());
        productPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: #2A9D8F;");

        // Add to Cart Button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-padding: 5 10; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        addToCartButton.setOnAction(e -> {
            cartController.addToCart(product, 1);
            showAlert(Alert.AlertType.INFORMATION, "Added to Cart", product.getDescription() + " added to the cart.");
        });

        card.getChildren().addAll(productImage, productName, productPrice, addToCartButton);

        return card;
    }

    private ObservableList<Product> loadProductData() {
        productData.add(new Product("P001", "Cat Toy", 9.99, 100));
        productData.add(new Product("P002", "Cat Bed", 49.99, 50));
        productData.add(new Product("P003", "Scratching Post", 29.99, 75));
        return productData;
    }

    private void styleNavButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #264653; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5;"));
    }

    private void showCartModal() {
        Stage cartStage = new Stage();
        cartStage.setTitle("Your Cart");

        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(20));
        cartBox.setStyle("-fx-background-color: white; -fx-padding: 20;");

        ListView<String> cartList = new ListView<>();
        cartController.viewCart().forEach(item -> {
            cartList.getItems().add(item.getProduct().getDescription() + " x" + item.getQuantity() + " - $" + String.format("%.2f", item.getTotalPrice()));
        });

        Label totalLabel = new Label("Total: $" + String.format("%.2f", cartController.getTotalCost()));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10 0;");

        Button removeButton = new Button("Remove Selected");
        removeButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px;");
        removeButton.setOnAction(e -> {
            String selectedItem = cartList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String productName = selectedItem.split(" x")[0].trim();
                cartController.removeByName(productName); // Ensure this method exists in CartController
                cartList.getItems().clear();
                cartController.viewCart().forEach(item -> {
                    cartList.getItems().add(item.getProduct().getDescription() + " x" + item.getQuantity() + " - $" + String.format("%.2f", item.getTotalPrice()));
                });
                totalLabel.setText("Total: $" + String.format("%.2f", cartController.getTotalCost()));
            }
        });

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> cartStage.close());

        cartBox.getChildren().addAll(cartList, totalLabel, removeButton, closeButton);
        Scene cartScene = new Scene(cartBox, 400, 400);
        cartStage.setScene(cartScene);
        cartStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
