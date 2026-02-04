package com.example.backend.controllers;

import com.example.backend.entity.Account;
import com.example.backend.exceptions.AccountNotFoundException;
import com.example.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    public AccountService accountService;

    // Create
    // http://localhost:8080/accounts/create
    @PostMapping("/create")
    public Account create(@RequestBody Account acc) {
        return accountService.createAccount(acc);
    }

    //Retrieve By accountId
    //http://localhost:8080/accounts/accountId?id=1
    @GetMapping("/accountId")
    public Account getAccount(@RequestParam("id") Integer id) throws AccountNotFoundException {
        return accountService.getAccountById(id);
    }
}
