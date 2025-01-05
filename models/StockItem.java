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

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Convert the item to a CSV row format
    public String toCSV() {
        return id + "," + category + "," + company + "," + stock;
    }
}
