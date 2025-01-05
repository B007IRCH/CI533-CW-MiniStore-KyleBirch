package models.filters;

import models.StockItem;

public class CompanyFilter implements StockFilter {
    private String company;

    public CompanyFilter(String company) {
        this.company = company;
    }

    @Override
    public boolean matches(StockItem item) {
        return item.getCompany().equalsIgnoreCase(company);
    }
}
