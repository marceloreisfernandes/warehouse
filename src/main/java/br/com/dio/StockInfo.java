package br.com.dio;

// Informação simples sobre o estoque: total de cestas e quantas estão vencidas.
public record StockInfo(int total, long expired) {
}
