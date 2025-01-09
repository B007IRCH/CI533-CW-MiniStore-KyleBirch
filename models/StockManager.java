package models;

import dbAccess.DBAccess;
import models.filters.StockFilter;

import java.sql.*;
import java.util.*;

/**
 * Manages stock data using a database.
 */
public class StockManager {
    private final DBAccess dbAccess;

    // Singleton instance
    private static StockManager instance;

    // Private constructor to initialize DBAccess and create table
    private StockManager(DBAccess dbAccess) {
        this.dbAccess = dbAccess;
        dbAccess.loadDriver();
        initializeDatabase();
    }

    public static StockManager getInstance(DBAccess dbAccess) {
        if (instance == null) {
            instance = new StockManager(dbAccess);
        }
        return instance;
    }

    // Create the StockItems table if it does not exist
    private void initializeDatabase() {
        try (Connection conn = dbAccess.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if the table exists
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "STOCKITEMS", null);

            // Create table only if it does not exist
            if (!tables.next()) {
                stmt.executeUpdate(
                        "CREATE TABLE StockItems (" +
                                "id VARCHAR(255) PRIMARY KEY, " +
                                "category VARCHAR(255), " +
                                "company VARCHAR(255), " +
                                "stock INT" +
                                ")"
                );
                System.out.println("Table 'StockItems' created successfully.");
            } else {
                System.out.println("Table 'StockItems' already exists.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }


    // Load stock data from the database
    public Collection<StockItem> getStockItems() {
        List<StockItem> stockItems = new ArrayList<>();
        try (Connection conn = dbAccess.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM StockItems")) {

            while (rs.next()) {
                stockItems.add(new StockItem(
                        rs.getString("id"),
                        rs.getString("category"),
                        rs.getString("company"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockItems;
    }

    // Update stock for an item
    public void updateStock(String itemId, int newStock) {
        try (Connection conn = dbAccess.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE StockItems SET stock = ? WHERE id = ?")) {

            ps.setInt(1, newStock);
            ps.setString(2, itemId);

            if (ps.executeUpdate() == 0) {
                System.out.println("Item ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new stock item
    public void addStockItem(StockItem item) {
        try (Connection conn = dbAccess.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO StockItems (id, category, company, stock) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, item.getId());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getCompany());
            ps.setInt(4, item.getStock());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Filter stock items based on a StockFilter
    public List<StockItem> filterStock(StockFilter filter) {
        List<StockItem> filteredStock = new ArrayList<>();
        for (StockItem item : getStockItems()) {
            if (filter.matches(item)) {
                filteredStock.add(item);
            }
        }
        return filteredStock;
    }
}
