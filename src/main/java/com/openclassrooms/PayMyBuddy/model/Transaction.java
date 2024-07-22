package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "giver_account")
    private Long giverAccount;

    @Column(name = "receiver_account")
    private String receiverAccount;

    private String description;

    private double amount;

    private double fee;

}
