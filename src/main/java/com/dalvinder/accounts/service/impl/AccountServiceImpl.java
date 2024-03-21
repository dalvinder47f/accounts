package com.dalvinder.accounts.service.impl;

import com.dalvinder.accounts.constants.AccountsContants;
import com.dalvinder.accounts.dto.AccountsDto;
import com.dalvinder.accounts.mapper.AccountMapper;
import com.dalvinder.accounts.mapper.CustomerMapper;
import com.dalvinder.accounts.dto.CustomerDto;
import com.dalvinder.accounts.entity.Accounts;
import com.dalvinder.accounts.entity.Customer;
import com.dalvinder.accounts.exceptions.CustomerAlreadyExistException;
import com.dalvinder.accounts.repository.AccountsRepository;
import com.dalvinder.accounts.repository.CustomerRepository;
import com.dalvinder.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService{

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already exist with given mobile number");
        }
        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setCreatedAt(LocalDateTime.now());
        savedCustomer.setCreatedBy("Anonymous");
        accountsRepository.save(createNewAccount(savedCustomer));

    }

    @Override
    public CustomerDto fetchUser(String mobileNumber) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (!optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer not found with given mobile number : " + mobileNumber);
        }
        Optional<Accounts> optionalAccounts = accountsRepository.findByCustomerId(optionalCustomer.get().getCustomerId());
        if (!optionalAccounts.isPresent()) {
            throw new CustomerAlreadyExistException("Customer not found with given mobile number: " + mobileNumber);
        }
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(optionalCustomer.get(), new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(optionalAccounts.get(), new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateUser(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(customerDto.getAccountsDto().getAccountNumber()).
                    orElseThrow(() -> new CustomerAlreadyExistException("Customer not found with given account number: " + customerDto.getAccountsDto().getAccountNumber()));
             accounts.setUpdatedAt(LocalDateTime.now());
             accounts.setUpdatedBy("Anonymous");
            AccountMapper.toMapAccounts(customerDto.getAccountsDto(), accounts);
            accounts = accountsRepository.save(accounts);
            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new CustomerAlreadyExistException("Customer not found with given account number: " + customerDto.getAccountsDto().getAccountNumber()));
            CustomerMapper.mapToCustomer(customerDto, customer);
             customer.setUpdatedAt(LocalDateTime.now());
             customer.setUpdatedBy("Anonymous");
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteUser(String mobileNumber) {
            Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                    () -> new CustomerAlreadyExistException("Customer not found with given mobile number: " + mobileNumber));
            Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                    () -> new CustomerAlreadyExistException("Customer not found with given mobile number: " + mobileNumber));
            customerRepository.deleteById(customer.getCustomerId());
            accountsRepository.deleteByAccountNumber(accounts.getCustomerId());
        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomNumber = 100000000L + new Random().nextInt(90000000);
        accounts.setAccountNumber(randomNumber);
        accounts.setAccountType(AccountsContants.SAVING);
        accounts.setBranchAddress(AccountsContants.ADDRESS);
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy("Anonymous");

        return accounts;
    }
}
