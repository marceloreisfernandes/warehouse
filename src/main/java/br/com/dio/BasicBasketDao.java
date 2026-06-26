package br.com.dio;

import java.util.ArrayList;
import java.util.List;

// Guarda o estoque de cestas em memória.
public class BasicBasketDao {

    private final List<BasicBasket> stock = new ArrayList<>();

    public void addAll(List<BasicBasket> baskets) {
        stock.addAll(baskets);
    }

    public List<BasicBasket> findAll() {
        return stock;
    }
}
