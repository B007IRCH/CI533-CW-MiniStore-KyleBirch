package models.filters;

import models.StockItem;

public class CategoryFilter implements StockFilter {
    private String category;

    public CategoryFilter(String category) {
        this.category = category;
    }

    @Override
    public boolean matches(StockItem item) {
        return item.getCategory().equalsIgnoreCase(category);
    }
}
