package br.com.dio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyDaoTest {

    @Test
    void newMoneyDaoShouldStartAtZero() {
        var moneyDao = new MoneyDao();

        assertEquals(0, moneyDao.getMoney().compareTo(BigDecimal.ZERO));
    }

    @Test
    void addShouldAccumulateValues() {
        var moneyDao = new MoneyDao();

        moneyDao.add(new BigDecimal("10.00"));
        moneyDao.add(new BigDecimal("5.50"));

        assertEquals(0, moneyDao.getMoney().compareTo(new BigDecimal("15.50")));
    }
}
