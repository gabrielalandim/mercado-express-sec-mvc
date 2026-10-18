package br.com.fiap.mercadoexpress.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Produto do Mercado Express.
 * Tabela no PostgreSQL: TDS_Sec_MVC_TB_Mercado
 * (o Hibernate grava o nome em minúsculas: tds_sec_mvc_tb_mercado)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TDS_Sec_MVC_TB_Mercado")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do produto")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Informe o tipo/categoria")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String tipo;

    @NotBlank(message = "Informe o setor")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String setor;

    @NotBlank(message = "Informe o tamanho/porção")
    @Size(max = 40)
    @Column(nullable = false, length = 40)
    private String tamanho;

    @NotNull(message = "Informe o preço de venda")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "Informe o preço de custo")
    @DecimalMin(value = "0.00", message = "O custo não pode ser negativo")
    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @NotNull(message = "Informe a quantidade em estoque")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    @Column(nullable = false)
    private Integer estoque;

    public Produto(String nome, String tipo, String setor, String tamanho,
                   BigDecimal preco, BigDecimal precoCusto, Integer estoque) {
        this.nome = nome;
        this.tipo = tipo;
        this.setor = setor;
        this.tamanho = tamanho;
        this.preco = preco;
        this.precoCusto = precoCusto;
        this.estoque = estoque;
    }
}
