package com.banking_api.banking_api.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.banking_api.banking_api.domain.account.Account;
import com.banking_api.banking_api.domain.account.Earnings;
import com.banking_api.banking_api.dtos.*;
import com.banking_api.banking_api.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final DepositService depositService;

    private final UserService userService;

    public AccountService(AccountRepository repository, @Lazy DepositService depositService, UserService userService) {
        this.repository = repository;
        this.depositService = depositService;
        this.userService = userService;
    }

    public Account createAccount(AccountDTO dto) throws EntityNotFoundException {

        Account account = new Account();
        account.setAccountNumber(dto.accountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCreationDate(LocalDate.now());
        account.setType(dto.type());
        account.setActive(true);
        account.setUser(userService.findUserById(dto.user()));
        account.setLastDepositDate(depositService.getLastDepositDate());

        repository.save(account);
        return account;
    }


    public void delete(AccountDeleteDto id) throws EntityNotFoundException {
        if (!repository.existsById(id.id())) {
            throw new EntityNotFoundException("Conta não existe");
        } else {
            repository.deactivateAccountById(id.id());
        }
    }


    public Page<AccountListDTO> getAllActiveAccounts(Pageable page) {
        var accounts = repository.findAllByActiveTrue(page);
        return accounts.map(a -> new AccountListDTO(a.getAccountNumber(), a.getType(), a.isActive(), a.getUser().getName()));
    }

    public AccountDTO findById(Long id) throws EntityNotFoundException {
        var account = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));

        return convertToAccountDTO(account);
    }

    public Account findByAccountId(Long id) throws EntityNotFoundException {

        var account = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id da conta não enoontrado"));
        return account;
    }

    private AccountDTO convertToAccountDTO(Account a) {
        return new AccountDTO(a.getAccountNumber(), a.getBalance(), a.getType(), a.getCreationDate(), a.getLastDepositDate(), a.isActive(), a.getUser().getId());
    }

    public List<AccountDTO> findByUserId(Long userId) {
        var accountByUserId = repository.findByUserId(userId);
        return accountByUserId.stream()
                .map(this::convertToAccountDTO)
                .collect(Collectors.toList());

    }

    public void save(Account account) {
        repository.save(account);
    }

    //Chama a cada 1 minuto
    // @Scheduled(cron = "0 * * ? * *")
@Scheduled(cron = "0 0 0 * * ?")
    public void earningsGenerate() {
       var accounts = repository.findAccountsActiveAndPoupanca ();
       if (accounts == null|| accounts.isEmpty()) { throw new EntityNotFoundException("Não há contas Poupança ativas com rendimentos pendentes"); }
       else {
           accounts.forEach(this::updateBalanceWithEarnings);
       }

    }



//Método extra pra poder usar o "reference method" no método "erningsGenerate()"
    public void updateBalanceWithEarnings(Account account) {
        BigDecimal newBalance = calculateBalancePlusEarnings(account);
        account.setBalance(newBalance);
        repository.save(account);
    }


        private BigDecimal calculateBalancePlusEarnings(Account account) {
            BigDecimal earningsAmount = new BigDecimal("0.01"); // Defina o valor dos ganhos aqui
            BigDecimal oldBalance = account.getBalance();
            BigDecimal increase = oldBalance.multiply(earningsAmount);
            return oldBalance.add(increase);
        }



    }







