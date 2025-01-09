package clients.gui;

import clients.customer.CartController;
import clients.customer.CartItem;
import clients.admin.AdminController;
import clients.admin.AdminView;
import catalogue.Product;
import dbAccess.DerbyAccess;
import dbAccess.DBAccess;
import dbAccess.SimpleCSVReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;

public class MainApp extends Application {
    private CartController cartController = new CartController();
    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private int productsToShow = 20; // Number of products to show per batch
    private FlowPane productPane = new FlowPane();
    private int offset = 0; // For lazy loading

    @Override
    public void start(Stage primaryStage) {
        showSplashScreen(() -> {
            Platform.runLater(() -> initializeMainApp(primaryStage));
        });
    }

    private void showSplashScreen(Runnable onFinish) {
        Stage splashStage = new Stage(StageStyle.UNDECORATED);

        VBox splashLayout = new VBox(10);
        splashLayout.setAlignment(Pos.CENTER);
        splashLayout.setPadding(new Insets(20));

        Label splashLabel = new Label("Welcome to MiniStore");
        splashLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        Label progressLabel = new Label("0%");
        progressLabel.setStyle("-fx-font-size: 16px;");

        splashLayout.getChildren().addAll(splashLabel, progressBar, progressLabel);

        Scene splashScene = new Scene(splashLayout, 400, 200);
        splashStage.setScene(splashScene);
        splashStage.show();

        splashStage.getProperties().put("progressBar", progressBar);
        splashStage.getProperties().put("progressLabel", progressLabel);

        new Thread(() -> {
            try {
                updateSplashProgress(splashStage, 0, "Initializing...");
                Thread.sleep(500); // Simulate startup delay

                updateSplashProgress(splashStage, 10, "Populating database...");
                SimpleCSVReader.populateDatabaseFromCSV();

                updateSplashProgress(splashStage, 40, "Loading categories...");
                loadCategories();

                updateSplashProgress(splashStage, 70, "Loading initial products...");
                lazyLoadProducts("", "All Categories");

                updateSplashProgress(splashStage, 100, "Starting application...");
                Platform.runLater(() -> {
                    splashStage.close();
                    onFinish.run();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateSplashProgress(Stage splashStage, double progress, String message) {
        Platform.runLater(() -> {
            ProgressBar progressBar = (ProgressBar) splashStage.getProperties().get("progressBar");
            Label progressLabel = (Label) splashStage.getProperties().get("progressLabel");

            progressBar.setProgress(progress / 100);
            progressLabel.setText((int) progress + "% - " + message);
        });
    }

    private void loadCategories() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:./dbAccess/catshop.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT category FROM products")) {

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                if (category != null && !category.isEmpty()) {
                    // No need to add categories directly to productData here
                }
            }

            System.out.println("Categories loaded successfully into the filter.");
        } catch (Exception e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }

    private void lazyLoadProducts(String searchQuery, String category) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:./dbAccess/catshop.db");
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM products WHERE description LIKE '%" + searchQuery + "%' ";
            if (!category.equals("All Categories")) {
                query += "AND category = '" + category + "' ";
            }
            query += "LIMIT " + productsToShow + " OFFSET " + offset;

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("product_id");
                String description = resultSet.getString("description");
                if (description == null || description.isEmpty()) {
                    description = "Unnamed Product"; // Fallback for missing descriptions
                }
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("stock");
                String productCategory = resultSet.getString("category");
                String company = resultSet.getString("Company");

                Product product = new Product(id, description, price, quantity, productCategory, company);
                productData.add(product);
                productPane.getChildren().add(createProductCard(product));
            }

            offset += productsToShow;

        } catch (Exception e) {
            System.err.println("Error loading data from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeMainApp(Stage primaryStage) {
        System.out.println("Application started. Initializing components...");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #f5f5f5, #ffffff);");

        HBox navBar = new HBox();
        navBar.setSpacing(15);
        navBar.setStyle("-fx-background-color: #2A9D8F; -fx-padding: 15; -fx-alignment: center-left;");

        Label logoLabel = new Label("MiniStore");
        logoLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search products...");
        searchBar.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10; -fx-border-radius: 5px;");
        searchButton.setOnAction(e -> reloadProducts(searchBar.getText(), "All Categories"));

        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        categoryFilter.getItems().add("All Categories");
        categoryFilter.setValue("All Categories");

        categoryFilter.setOnAction(e -> reloadProducts(searchBar.getText(), categoryFilter.getValue()));

        Button viewCartButton = new Button("View Cart");
        styleNavButton(viewCartButton);
        viewCartButton.setOnAction(e -> showCartModal());

        Button adminButton = new Button("Admin Panel");
        styleNavButton(adminButton);
        adminButton.setOnAction(e -> {
            AdminView adminView = new AdminView(); // Create AdminView instance
            DBAccess dbAccess = new DerbyAccess(); // Create DBAccess instance
            AdminController adminController = new AdminController(adminView, dbAccess); // Pass both AdminView and DBAccess
            adminController.launchAdminPanel(); // Launch the admin panel
        });

        ComboBox<String> sortFilter = new ComboBox<>();
        sortFilter.getItems().addAll(
                "Sort by Price (Low to High)",
                "Sort by Price (High to Low)",
                "Sort by Stock (Low to High)",
                "Sort by Stock (High to Low)"
        );
        sortFilter.setValue("Sort by Price (Low to High)");
        sortFilter.setOnAction(e -> applySortFilter(sortFilter.getValue()));

        navBar.getChildren().addAll(logoLabel, searchBar, searchButton, categoryFilter, sortFilter, viewCartButton, adminButton);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        VBox searchSection = new VBox(navBar);
        root.setTop(searchSection);

        productPane.setPadding(new Insets(20));
        productPane.setHgap(20);
        productPane.setVgap(20);
        productPane.setAlignment(Pos.CENTER);
        productPane.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(productPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        Button loadMoreButton = new Button("Load More");
        loadMoreButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        loadMoreButton.setOnAction(e -> lazyLoadProducts(searchBar.getText(), categoryFilter.getValue()));

        VBox centerBox = new VBox(scrollPane, loadMoreButton);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        root.setCenter(centerBox);

        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #2A9D8F; -fx-padding: 10;");
        footer.setAlignment(Pos.CENTER);
        Label footerLabel = new Label("© 2025 MiniStore | Contact Us: support@ministore.com");
        footerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        footer.getChildren().add(footerLabel);
        root.setBottom(footer);

        lazyLoadProducts("", "All Categories");

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("MiniStore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void reloadProducts(String searchQuery, String category) {
        productPane.getChildren().clear();
        productData.clear();
        offset = 0;
        lazyLoadProducts(searchQuery, category);
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.2, 0, 0);");

        // Product name/description
        Label productName = new Label(product.getDescription());
        productName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2A9D8F; text-align: center;");

        // Product price
        Label productPrice = new Label(String.format("$%.2f", product.getPrice()));
        productPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: #E76F51;");

        // Product category
        Label productCategory = new Label("Category: " + product.getCategory());
        productCategory.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Product company
        Label productCompany = new Label("By: " + product.getCompany());
        productCompany.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Add to Cart button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #2A9D8F; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        addToCartButton.setOnAction(e -> {
            cartController.addToCart(product, 1);
            showAlert(Alert.AlertType.INFORMATION, "Added to Cart", product.getDescription() + " added to the cart.");
        });

        // Hover functionality for low stock
        card.setOnMouseEntered(e -> {
            if (product.getQuantity() < 150) { // Using getQuantity() here
                Tooltip tooltip = new Tooltip("Only " + product.getQuantity() + " left!");
                Tooltip.install(card, tooltip);
            }
        });

        card.getChildren().addAll(productName, productPrice, productCategory, productCompany, addToCartButton);
        return card;
    }

    private void applySortFilter(String filter) {
        switch (filter) {
            case "Sort by Price (Low to High)":
                productData.sort(Comparator.comparingDouble(Product::getPrice));
                break;
            case "Sort by Price (High to Low)":
                productData.sort(Comparator.comparingDouble(Product::getPrice).reversed());
                break;
            case "Sort by Stock (Low to High)":
                productData.sort(Comparator.comparingInt(Product::getQuantity));
                break;
            case "Sort by Stock (High to Low)":
                productData.sort(Comparator.comparingInt(Product::getQuantity).reversed());
                break;
        }
        reloadProductPane();
    }

    private void reloadProductPane() {
        productPane.getChildren().clear();
        productData.forEach(product -> productPane.getChildren().add(createProductCard(product)));
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

        Label totalLabel = new Label("Total: $" + String.format("%.2f", cartController.getTotalCost()));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10 0;");
        totalLabel.setId("totalLabel");

        Button removeSelectedButton = new Button("Remove Selected");
        removeSelectedButton.setStyle("-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-size: 14px;");
        removeSelectedButton.setOnAction(e -> {
            CartItem selectedItem = cartList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartController.removeFromCart(selectedItem.getProduct().getProductNum());
                cartList.getItems().remove(selectedItem);

                // Update total label dynamically
                totalLabel.setText("Total: $" + String.format("%.2f", cartController.getTotalCost()));
                showAlert(Alert.AlertType.INFORMATION, "Item Removed", selectedItem.getProduct().getDescription() + " removed from the cart.");
            }
        });

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
