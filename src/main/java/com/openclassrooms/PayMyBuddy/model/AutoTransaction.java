package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "auto_transaction")
public class AutoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bank_account_id")
    private int bankAccountId;

    @Column(name = "account_id")
    private int accountId;

    private double amount;

}
