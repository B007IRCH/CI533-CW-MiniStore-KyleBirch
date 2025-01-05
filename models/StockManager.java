package models;

import java.io.*;
import java.util.*;
import models.filters.StockFilter;

public class StockManager {
    private static final String STOCK_CSV = "Item_Database.csv"; // Path to your CSV file
    private final Map<String, StockItem> stockData = new HashMap<>();

    // Singleton instance
    private static StockManager instance;

    private StockManager() {
        loadStock();
    }

    public static StockManager getInstance() {
        if (instance == null) {
            instance = new StockManager();
        }
        return instance;
    }

    // Load stock data from the CSV file
    private void loadStock() {
        try (BufferedReader br = new BufferedReader(new FileReader(STOCK_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { // Adjust based on CSV structure
                    String id = parts[0];
                    String category = parts[1];
                    String company = parts[2];
                    int stock = Integer.parseInt(parts[3]);
                    stockData.put(id, new StockItem(id, category, company, stock));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save updated stock data back to the CSV file
    public void saveStock() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCK_CSV))) {
            for (StockItem item : stockData.values()) {
                bw.write(item.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all stock items
    public Collection<StockItem> getStockItems() {
        return stockData.values();
    }

    // Update stock for an item
    public void updateStock(String itemId, int newStock) {
        if (stockData.containsKey(itemId)) {
            StockItem item = stockData.get(itemId);
            item.setStock(newStock);
            saveStock();
        } else {
            System.out.println("Item ID not found.");
        }
    }

    // Filter stock using a StockFilter
    public List<StockItem> filterStock(StockFilter filter) {
        List<StockItem> filtered = new ArrayList<>();
        for (StockItem item : stockData.values()) {
            if (filter.matches(item)) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}
