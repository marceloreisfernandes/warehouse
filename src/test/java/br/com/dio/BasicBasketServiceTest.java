package br.com.dio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicBasketServiceTest {

    private BasicBasketDao basketDao;
    private MoneyDao moneyDao;
    private BasicBasketService service;

    @BeforeEach
    void setUp() {
        basketDao = new BasicBasketDao();
        moneyDao = new MoneyDao();
        service = new BasicBasketService(basketDao, moneyDao);
    }

    // Helper to keep the tests short and readable.
    private Box boxWith(long amount, String price, LocalDate validate) {
        return new Box(amount, validate, new BigDecimal(price));
    }

    @Test
    void receivingABoxShouldAddTheSameAmountOfBasketsToStock() {
        var box = boxWith(10, "100", LocalDate.now().plusDays(30));

        var received = service.receive(box);

        assertEquals(10, received.size());
        assertEquals(10, basketDao.findAll().size());
    }

    @Test
    void receivingABoxShouldPriceEachBasketAsUnitPricePlus20Percent() {
        // unit price = 100 / 10 = 10 ; final = 10 + 20% = 12.00
        var box = boxWith(10, "100", LocalDate.now().plusDays(30));

        var received = service.receive(box);

        var price = received.get(0).price();
        assertEquals(0, price.compareTo(new BigDecimal("12.00")));
    }

    @Test
    void checkStockShouldReturnTotalAndExpiredAmount() {
        service.receive(boxWith(2, "20", LocalDate.now().plusDays(10))); // valid
        service.receive(boxWith(1, "10", LocalDate.now().minusDays(1))); // expired

        var info = service.checkStock();

        assertEquals(3, info.total());
        assertEquals(1, info.expired());
    }

    @Test
    void sellingShouldReturnTheTotalAndUpdateTheCash() {
        service.receive(boxWith(10, "100", LocalDate.now().plusDays(30))); // each basket = 12.00

        var value = service.sell(2);

        assertEquals(0, value.compareTo(new BigDecimal("24.00")));
        assertEquals(0, moneyDao.getMoney().compareTo(new BigDecimal("24.00")));
    }

    @Test
    void sellingShouldRemoveSoldBasketsFromStock() {
        service.receive(boxWith(10, "100", LocalDate.now().plusDays(30)));

        service.sell(2);

        assertEquals(8, basketDao.findAll().size());
    }

    @Test
    void removingExpiredShouldDropExpiredAndKeepValidBaskets() {
        service.receive(boxWith(2, "20", LocalDate.now().plusDays(10))); // valid
        service.receive(boxWith(3, "30", LocalDate.now().minusDays(1))); // expired

        var discarded = service.removeExpired();

        assertEquals(3, discarded.size());              // 3 expired baskets were discarded
        assertEquals(2, basketDao.findAll().size());    // only the 2 valid ones remain
        assertEquals(0, service.checkStock().expired()); // nothing expired left in stock
    }
}
