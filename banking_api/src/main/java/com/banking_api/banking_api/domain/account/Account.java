package com.banking_api.banking_api.domain.account;
import com.banking_api.banking_api.domain.user.User;
import com.banking_api.banking_api.dtos.AccountDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(name ="account_number")
    private String accountNumber;

    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_deposit_date")
    private Date lastDepositDate;

    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Account(AccountDTO dto) {

        this.accountNumber = dto.accountNumber();
        this.balance = dto.balance();
        this.type = dto.type();
        this.creationDate = dto.creationDate();
        this.lastDepositDate = dto.lastDepositDate();
        this.active = dto.active();
        this.user = dto.user();


    }

}
