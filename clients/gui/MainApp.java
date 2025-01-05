package clients.gui;

import clients.customer.CartController;
import clients.customer.CartItem;
import catalogue.Product;
import dbAccess.SimpleCSVReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

public class MainApp extends Application {
    private CartController cartController = new CartController();
    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private int productsToShow = 20;
    private FlowPane productPane = new FlowPane(); // Class-level variable

    @Override
    public void start(Stage primaryStage) {
        // Debugging output
        System.out.println("Application started. Initializing components...");

        // Populate database from CSV
        System.out.println("Starting CSV population...");
        SimpleCSVReader.populateDatabaseFromCSV();

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

        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add default "All Categories" option
        categoryFilter.getItems().add("All Categories");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:./dbAccess/catshop.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT category FROM products")) {

            // Add each unique category from the database to the ComboBox
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                if (category != null && !category.isEmpty()) {
                    categoryFilter.getItems().add(category);
                }
            }

            System.out.println("Categories loaded successfully into the filter.");

        } catch (Exception e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }

        // Set default value
        categoryFilter.setValue("All Categories");

        categoryFilter.setOnAction(e -> {
            String selectedCategory = categoryFilter.getValue();
            FilteredList<Product> filteredProducts = new FilteredList<>(productData, product -> {
                if (selectedCategory.equals("All Categories")) {
                    return true;
                }
                return product.getCategory().equalsIgnoreCase(selectedCategory);
            });
            productPane.getChildren().clear();
            filteredProducts.stream().limit(productsToShow).forEach(product -> productPane.getChildren().add(createProductCard(product)));
        });

        Button viewCartButton = new Button("View Cart");
        styleNavButton(viewCartButton);
        viewCartButton.setOnAction(e -> showCartModal());

        Button wishlistButton = new Button("Wishlist");
        styleNavButton(wishlistButton);

        Button adminButton = new Button("Admin");
        styleNavButton(adminButton);

        navBar.getChildren().addAll(logoLabel, searchBar, categoryFilter, viewCartButton, wishlistButton, adminButton);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        VBox searchSection = new VBox(navBar);
        root.setTop(searchSection);

        // Configure productPane
        productPane.setPadding(new Insets(20));
        productPane.setHgap(20);
        productPane.setVgap(20);
        productPane.setAlignment(Pos.CENTER);
        productPane.setStyle("-fx-background-color: transparent;");

        ensureDatabaseSetup();
        loadProductDataFromDatabase();
        addProductsToPane(productPane, 0, productsToShow);

        // Wrap the product pane in a scroll pane
        ScrollPane scrollPane = new ScrollPane(productPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        // Button to load more products
        Button loadMoreButton = new Button("Load More");
        loadMoreButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        loadMoreButton.setOnAction(e -> {
            int currentSize = productPane.getChildren().size();
            addProductsToPane(productPane, currentSize, currentSize + productsToShow);
        });

        VBox centerBox = new VBox(scrollPane, loadMoreButton);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        root.setCenter(centerBox);

        // Footer
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #2A9D8F; -fx-padding: 10;");
        footer.setAlignment(Pos.CENTER);
        Label footerLabel = new Label("© 2025 MiniStore | Contact Us: support@ministore.com");
        footerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        footer.getChildren().add(footerLabel);
        root.setBottom(footer);

        // Add search functionality
        FilteredList<Product> filteredProducts = new FilteredList<>(productData, p -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);

            filteredProducts.setPredicate(product -> product.getDescription().toLowerCase(Locale.ROOT).contains(lowerCaseFilter));
            productPane.getChildren().clear();
            filteredProducts.stream().limit(productsToShow).forEach(product -> productPane.getChildren().add(createProductCard(product)));
        });

        // Set scene and stage
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("MiniStore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addProductsToPane(FlowPane productPane, int start, int end) {
        for (int i = start; i < end && i < productData.size(); i++) {
            VBox productCard = createProductCard(productData.get(i));
            productPane.getChildren().add(productCard);
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1px; " +
                "-fx-border-radius: 10px; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.2, 0, 0);");

        ImageView productImage = new ImageView(new Image("file:sample.jpg"));
        productImage.setFitWidth(150);
        productImage.setFitHeight(150);
        productImage.setPreserveRatio(true);

        Label productName = new Label(product.getDescription());
        productName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label productPrice = new Label("$" + product.getPrice());
        productPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: #2A9D8F;");

        Label productCategory = new Label("Category: " + product.getCategory());
        productCategory.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        Label productCompany = new Label("By: " + product.getCompany());
        productCompany.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-padding: 5 10; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        addToCartButton.setOnAction(e -> {
            cartController.addToCart(product, 1);
            showAlert(Alert.AlertType.INFORMATION, "Added to Cart", product.getDescription() + " added to the cart.");
        });

        card.getChildren().addAll(productImage, productName, productPrice, productCategory, productCompany, addToCartButton);
        return card;
    }

    private void ensureDatabaseSetup() {
        String databaseUrl = "jdbc:sqlite:./dbAccess/catshop.db";

        try (Connection connection = java.sql.DriverManager.getConnection(databaseUrl)) {
            Statement statement = connection.createStatement();

            // Create the products table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id TEXT PRIMARY KEY, " +
                    "description TEXT, " +
                    "price REAL, " +
                    "stock INTEGER, " +
                    "category TEXT, " +
                    "company TEXT)";
            statement.execute(createTableQuery);

        } catch (Exception e) {
            System.err.println("Error setting up database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProductDataFromDatabase() {
        String databaseUrl = "jdbc:sqlite:./dbAccess/catshop.db";

        try (Connection connection = java.sql.DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connected to SQLite database successfully!");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");

            while (resultSet.next()) {
                String id = resultSet.getString("product_id");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("stock");
                String category = resultSet.getString("category");
                String company = resultSet.getString("company");

                productData.add(new Product(id, description, price, quantity, category, company));
            }
            System.out.println("Products loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading data from database: " + e.getMessage());
            e.printStackTrace();
        }
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

        ListView<CartItem> cartList = new ListView<>();
        cartList.getItems().addAll(cartController.viewCart());
        cartList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getProduct().getDescription() + " x" + item.getQuantity() + " - $" + String.format("%.2f", item.getTotalPrice()));
                }
            }
        });

        Button removeSelectedButton = new Button("Remove Selected");
        removeSelectedButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px;");
        removeSelectedButton.setOnAction(e -> {
            CartItem selectedItem = cartList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartController.removeFromCart(selectedItem.getProduct().getProductNum());
                cartList.getItems().remove(selectedItem);
                showAlert(Alert.AlertType.INFORMATION, "Item Removed", selectedItem.getProduct().getDescription() + " removed from the cart.");
            }
        });

        Label totalLabel = new Label("Total: $" + String.format("%.2f", cartController.getTotalCost()));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10 0;");

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> cartStage.close());

        cartBox.getChildren().addAll(cartList, removeSelectedButton, totalLabel, closeButton);
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
