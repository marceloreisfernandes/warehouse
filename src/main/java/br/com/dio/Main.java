package br.com.dio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private final static BasicBasketDao basketDao = new BasicBasketDao();
    private final static MoneyDao moneyDao = new MoneyDao();
    private final static BasicBasketService service = new BasicBasketService(basketDao, moneyDao);

    public static void main(String[] args) {
        System.out.println("Bem vindo ao sistema de armazém");
        System.out.println("Selecione a opção desejada");
        var option = -1;
        while (true){
            System.out.println("1 - Verificar estoque de cesta básica");
            System.out.println("2 - Verificar caixa");
            System.out.println("3 - Receber Cestas");
            System.out.println("4 - Vender Cestas");
            System.out.println("5 - Remover itens vencidos");
            System.out.println("6 - Sair");
            option = scanner.nextInt();
            switch (option){
                case 1 -> checkStock();
                case 2 -> checkMoney();
                case 3 -> receiveItems();
                case 4 -> soldItems();
                case 5 -> removeItemsOutOfDate();
                case 6 -> System.exit(0);
            }
        }
    }

    private static void soldItems() {
        System.out.println("Quantas cestar serão vendidas");
        var amount = scanner.nextInt();
        var value = service.sell(amount);
        System.out.printf("O valor da venda é de %s \n", value);
    }

    private static void checkStock(){
        var info = service.checkStock();
        System.out.printf("Existem %s cestas em estoque, das quais %s estão fora do prazo de validade \n", info.total(), info.expired());
    }

    private static void checkMoney(){
        System.out.printf("O caixa no momento é de %s\n", moneyDao.getMoney());
    }

    private static void removeItemsOutOfDate(){
        var discarded = service.removeExpired();
        var lost = discarded.stream().map(BasicBasket::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.printf("Foram descartadas do estoque %s cestas vencidas, o prejuizo foi de %s \n", discarded.size(), lost);
    }

    private static void receiveItems(){
        System.out.println("Informe o valor da entrega");
        var price = scanner.nextBigDecimal();
        System.out.println("Informe a quantidade de cestas da entrega");
        var amount = scanner.nextLong();
        System.out.println("Informe a data de vencimento");
        var validate = scanner.next();
        var box = new Box(amount, parseDate(validate), price);
        var baskets = service.receive(box);
        System.out.printf("Foram adicionadas %s cestas ao estoque\n", baskets.size());
    }

    private static LocalDate parseDate(String input){
        var parts = input.split("/");
        var day = Integer.parseInt(parts[0]);
        var month = Integer.parseInt(parts[1]);
        var year = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }

}
