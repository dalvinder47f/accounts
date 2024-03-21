package com.dalvinder.accounts.service;

import com.dalvinder.accounts.dto.CustomerDto;
import org.springframework.stereotype.Service;


public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchUser(String mobileNumber);

    boolean updateUser(CustomerDto customerDto);

    boolean deleteUser(String mobileNumber);
}
