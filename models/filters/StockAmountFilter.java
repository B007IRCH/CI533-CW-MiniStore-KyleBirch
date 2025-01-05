package models.filters;

import models.StockItem;

public class StockAmountFilter implements StockFilter {
    private int minimumStock;

    public StockAmountFilter(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    @Override
    public boolean matches(StockItem item) {
        return item.getStock() >= minimumStock;
    }
}
