package models;

public class StockItem {
    private String id;
    private String category;
    private String company;
    private int stock;

    public StockItem(String id, String category, String company, int stock) {
        this.id = id;
        this.category = category;
        this.company = company;
        this.stock = stock;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getCompany() {
        return company;
    }

    public int getStock() {
        return stock;
    }

    // Setter
    public void setStock(int stock) {
        this.stock = stock;
    }

    // Convert the item to a CSV row format
    public String toCSV() {
        return id + "," + category + "," + company + "," + stock;
    }

    // Convert the item to a SQL INSERT query
    public String toSQLInsert() {
        return String.format(
                "INSERT INTO StockItems (id, category, company, stock) VALUES ('%s', '%s', '%s', %d)",
                id, category, company, stock
        );
    }

    // Update stock SQL query
    public String toSQLUpdate() {
        return String.format(
                "UPDATE StockItems SET category='%s', company='%s', stock=%d WHERE id='%s'",
                category, company, stock, id
        );
    }

    // Overriding toString for better readability
    @Override
    public String toString() {
        return String.format(
                "StockItem{id='%s', category='%s', company='%s', stock=%d}",
                id, category, company, stock
        );
    }
}
