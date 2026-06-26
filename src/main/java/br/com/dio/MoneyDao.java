package br.com.dio;

import java.math.BigDecimal;

// Guarda o valor do caixa.
public class MoneyDao {

    private BigDecimal money = BigDecimal.ZERO;

    public void add(BigDecimal value) {
        money = money.add(value);
    }

    public BigDecimal getMoney() {
        return money;
    }
}
