package models.filters;

import models.StockItem;

public interface StockFilter {
    boolean matches(StockItem item);
}
