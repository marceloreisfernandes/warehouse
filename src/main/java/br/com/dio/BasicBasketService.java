package br.com.dio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

// Regras de negócio do armazém (receber, vender, conferir, remover vencidas).
public class BasicBasketService {

    private final BasicBasketDao basketDao;
    private final MoneyDao moneyDao;

    public BasicBasketService(BasicBasketDao basketDao, MoneyDao moneyDao) {
        this.basketDao = basketDao;
        this.moneyDao = moneyDao;
    }

    public List<BasicBasket> receive(Box box) {
        var unitPrice = box.price().divide(new BigDecimal(box.amount()), RoundingMode.CEILING);
        var finalPrice = unitPrice.add(unitPrice.multiply(new BigDecimal("0.20")));
        var baskets = Stream.generate(() -> new BasicBasket(box.validate(), finalPrice))
                .limit(box.amount())
                .toList();
        basketDao.addAll(baskets);
        return baskets;
    }

    public StockInfo checkStock() {
        var stock = basketDao.findAll();
        int total = stock.size();
        long expired = stock.stream().filter(this::isExpired).count();
        return new StockInfo(total, expired);
    }

    public BigDecimal sell(int amount) {
        var stock = basketDao.findAll();
        stock.sort(Comparator.comparing(BasicBasket::price));
        var toSold = stock.subList(0, amount);
        var value = toSold.stream().map(BasicBasket::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        toSold.clear(); // remove as cestas vendidas do estoque
        moneyDao.add(value);
        return value;
    }

    public List<BasicBasket> removeExpired() {
        var stock = basketDao.findAll();
        var expired = stock.stream().filter(this::isExpired).toList();
        stock.removeIf(this::isExpired); // descarta as cestas vencidas, mantém as válidas
        return expired;
    }

    private boolean isExpired(BasicBasket basket) {
        return basket.validate().isBefore(LocalDate.now());
    }
}
