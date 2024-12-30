package middle;

import java.util.ArrayList;
import java.util.List;

public class StockSubject {
    private List<StockObserver> observers = new ArrayList<>();
    private String product;
    private int stockLevel;

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void setStock(String product, int stockLevel) {
        this.product = product;
        this.stockLevel = stockLevel;
        notifyObservers();
    }

    private void notifyObservers() {
        for (StockObserver observer : observers) {
            observer.update(product, stockLevel);
        }
    }
}
