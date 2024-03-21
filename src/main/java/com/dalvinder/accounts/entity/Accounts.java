package com.dalvinder.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor // for creating constructor
@NoArgsConstructor // for creating empty constructor
public class Accounts extends BaseEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native")
    private Long customerId;

    @Id
    @Column(name = "account_number") // if you have separate key name in database
    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
