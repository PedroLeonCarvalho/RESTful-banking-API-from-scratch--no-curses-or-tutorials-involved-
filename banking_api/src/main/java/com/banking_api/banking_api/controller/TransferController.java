package com.banking_api.banking_api.controller;

import com.banking_api.banking_api.domain.transactions.transfer.Transfer;
import com.banking_api.banking_api.domain.transactions.withdraw.Withdraw;
import com.banking_api.banking_api.dtos.TransferDTO;
import com.banking_api.banking_api.dtos.WithdrawDTO;
import com.banking_api.banking_api.service.TransferService;
import com.banking_api.banking_api.service.WithdrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {

        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> toTransfer (@RequestBody TransferDTO dto) throws Exception {
        var newTransfer = transferService.transfer(dto);
        return ResponseEntity.ok(newTransfer);
    }
}