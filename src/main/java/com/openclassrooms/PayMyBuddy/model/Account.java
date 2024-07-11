package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table (name = "account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn (
            name = "user_id",
            referencedColumnName = "id"
    )
    private DBUser dBUser;

    private double balance;

}
