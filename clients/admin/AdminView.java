package clients.admin;

import models.StockItem;

import java.util.Collection;
import java.util.Scanner;

public class AdminView {
    private AdminController controller;

    public void setController(AdminController controller) {
        this.controller = controller;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View Stock");
            System.out.println("2. Edit Stock");
            System.out.println("3. Filter Stock");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> controller.displayStock();
                case 2 -> controller.editStock(scanner);
                case 3 -> controller.filterStock(scanner);
                case 4 -> {
                    System.out.println("Exiting Admin Panel...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void displayStock(Collection<StockItem> items) {
        System.out.println("\nStock List:");
        System.out.println("ID | Category | Company | Stock");
        System.out.println("--------------------------------");
        for (StockItem item : items) {
            System.out.printf("%s | %s | %s | %d%n",
                    item.getId(),
                    item.getCategory(),
                    item.getCompany(),
                    item.getStock());
        }
    }
}
