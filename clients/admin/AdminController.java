package clients.admin;

import models.StockManager;
import models.filters.*;

import java.util.Scanner;

public class AdminController {
    private final StockManager stockManager;
    private final AdminView adminView;

    public AdminController(AdminView adminView) {
        this.stockManager = StockManager.getInstance(); // Use the singleton StockManager
        this.adminView = adminView;
        adminView.setController(this); // Pass the controller to the view
    }

    public void launchAdminPanel() {
        adminView.displayMenu();
    }

    public void displayStock() {
        adminView.displayStock(stockManager.getStockItems());
    }

    public void editStock(Scanner scanner) {
        System.out.println("Enter Item ID:");
        String itemId = scanner.next();
        System.out.println("Enter new stock amount:");
        int newStock = scanner.nextInt();

        if (newStock < 0) {
            System.out.println("Stock amount cannot be negative!");
        } else {
            stockManager.updateStock(itemId, newStock);
            System.out.println("Stock updated successfully!");
        }
    }

    public void filterStock(Scanner scanner) {
        System.out.println("Filter by:");
        System.out.println("1. Category");
        System.out.println("2. Company");
        System.out.println("3. Minimum Stock");
        int choice = scanner.nextInt();

        StockFilter filter = switch (choice) {
            case 1 -> {
                System.out.println("Enter category:");
                yield new CategoryFilter(scanner.next());
            }
            case 2 -> {
                System.out.println("Enter company:");
                yield new CompanyFilter(scanner.next());
            }
            case 3 -> {
                System.out.println("Enter minimum stock:");
                yield new StockAmountFilter(scanner.nextInt());
            }
            default -> null;
        };

        if (filter != null) {
            adminView.displayStock(stockManager.filterStock(filter));
        } else {
            System.out.println("Invalid filter choice!");
        }
    }
}
