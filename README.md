# Warehouse

Projeto Java simples baseado no exercício `warehouse` da Digital Innovation One, utilizado para praticar refatoração, separação de responsabilidades, SOLID e TDD.

O sistema simula um pequeno armazém de cestas básicas, com operações de estoque e caixa por meio de um menu interativo no console.

## Objetivo do exercício

O projeto original concentrava muitas responsabilidades na classe `Main`, incluindo:

* exibição do menu;
* leitura de dados do usuário;
* controle de estoque;
* controle de caixa;
* recebimento de cestas;
* venda de cestas;
* remoção de itens vencidos;
* regras de cálculo de preço.

A proposta deste exercício foi refatorar o código de forma incremental, mantendo a simplicidade do projeto e aproximando a estrutura de boas práticas de organização de código.

A refatoração foi feita com foco em:

* responsabilidade única;
* código mais legível;
* regras de negócio fora da classe `Main`;
* testes automatizados com JUnit 5;
* execução via Docker.

## Tecnologias utilizadas

* Java 21
* Gradle
* JUnit 5
* Docker
* Docker Compose

## Estrutura do projeto

```text
src
├── main
│   └── java
│       └── br
│           └── com
│               └── dio
│                   ├── BasicBasket.java
│                   ├── BasicBasketDao.java
│                   ├── BasicBasketService.java
│                   ├── Box.java
│                   ├── Main.java
│                   ├── MoneyDao.java
│                   └── StockInfo.java
└── test
    └── java
        └── br
            └── com
                └── dio
                    ├── BasicBasketDaoTest.java
                    ├── BasicBasketServiceTest.java
                    └── MoneyDaoTest.java
```

## Responsabilidades principais

### Main

Responsável apenas pela interação com o usuário:

* exibir o menu;
* ler dados pelo console;
* converter entradas simples;
* chamar os serviços;
* imprimir os resultados.

### BasicBasketService

Responsável pelas regras principais do estoque:

* receber caixas de cestas básicas;
* calcular o preço final das cestas;
* verificar informações do estoque;
* vender cestas;
* remover cestas vencidas.

### BasicBasketDao

Responsável por armazenar em memória a lista de cestas básicas.

### MoneyDao

Responsável por armazenar e atualizar o valor em caixa.

### StockInfo

Record usado para retornar informações resumidas do estoque:

* total de cestas;
* quantidade de cestas vencidas.

## Regras de negócio

Ao receber uma caixa de cestas básicas:

* o preço unitário é calculado por:

```text
preço da caixa / quantidade de cestas
```

* o preço final de cada cesta recebe acréscimo de 20%:

```text
preço unitário + 20%
```

Na venda:

* as cestas são vendidas considerando o menor preço primeiro;
* o total vendido é somado ao caixa;
* as cestas vendidas são removidas do estoque.

Na remoção de vencidos:

* as cestas vencidas são removidas do estoque;
* as cestas válidas permanecem.

## Bugs corrigidos durante a refatoração

Durante o processo de refatoração com TDD, foram encontrados e corrigidos dois problemas.

### Venda não removia itens do estoque

O código original calculava a venda e atualizava o caixa, mas as cestas vendidas continuavam no estoque.

Foi criado um teste para expor esse comportamento e depois a regra foi corrigida para remover exatamente a quantidade vendida.

### Remoção de vencidos estava invertida

O código original mantinha no estoque as cestas vencidas e descartava as válidas.

Foi criado um teste para expor o problema e depois a regra foi corrigida para remover apenas os itens vencidos.

## Como executar com Docker Compose

Execute o sistema em modo interativo:

```bash
docker compose run --rm warehouse
```

O menu será exibido no console:

```text
Bem vindo ao sistema de armazém
Selecione a opção desejada
1 - Verificar estoque de cesta básica
2 - Verificar caixa
3 - Receber Cestas
4 - Vender Cestas
5 - Remover itens vencidos
6 - Sair
```

## Como executar os testes

Com Docker Compose:

```bash
docker compose run --rm warehouse gradle test --no-daemon
```

Ou, se preferir usar o Gradle Wrapper local:

```bash
./gradlew test
```

## Smoke test do console

Para validar rapidamente se a aplicação inicia e encerra corretamente:

```bash
printf '6\n' | docker compose run --rm -T warehouse
```

## Como gerar a imagem Docker

```bash
docker build -t warehouse .
```

## Como executar a imagem Docker

```bash
docker run -it --rm warehouse
```

Também é possível fazer um smoke test direto na imagem:

```bash
printf '6\n' | docker run -i --rm warehouse
```

## Validação realizada

A refatoração foi validada com:

```bash
docker compose run --rm warehouse gradle test --no-daemon
```

Resultado:

```text
BUILD SUCCESSFUL
```

Também foi validada a execução do menu no console com:

```bash
printf '6\n' | docker compose run --rm -T warehouse
```

Resultado esperado:

* aplicação inicia;
* menu é exibido;
* opção `6` encerra o sistema;
* build finaliza com sucesso.

## Observação

Este projeto não tem o objetivo de aplicar uma arquitetura complexa. A intenção é manter o código simples e didático, demonstrando uma refatoração gradual a partir de um sistema pequeno de console.
