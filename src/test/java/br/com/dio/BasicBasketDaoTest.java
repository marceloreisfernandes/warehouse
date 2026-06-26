package br.com.dio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicBasketDaoTest {

    private BasicBasket basket(int dayOffset) {
        return new BasicBasket(LocalDate.now().plusDays(dayOffset), new BigDecimal("12.00"));
    }

    @Test
    void addAllThenFindAllShouldReturnStoredBaskets() {
        var dao = new BasicBasketDao();

        dao.addAll(List.of(basket(1), basket(2)));

        assertEquals(2, dao.findAll().size());
    }
}
