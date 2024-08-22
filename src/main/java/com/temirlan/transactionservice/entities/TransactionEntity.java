package com.temirlan.transactionservice.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "account_from")
    private Long accountFrom;

    @NonNull
    @Column(name = "account_to")
    private Long accountTo;

    @NonNull
    @Size(min = 3, max = 3)
    @Column(name = "currency_shortname")
    private String currencyShortname;

    @NonNull
    @Column(name = "sum")
    private BigDecimal sum;


    @Column(name = "expense_category")
    private String expenseCategory;

    @Column(name = "datetime")
    private Instant datetime;

    @NonNull
    @Column(name = "limit_sum")
    private BigDecimal limitSum;

    @Column(name = "limit_datetime")
    private Instant limitDatetime;

    @Size(min = 3, max = 3)
    @Column(name = "limit_currency_shortname")
    private String limitCurrencyShortname;

    @Column(name = "limit_exceeded")
    private Boolean limitExceeded;

}
