package com.dalvinder.accounts.controller;

import com.dalvinder.accounts.constants.AccountsContants;
import com.dalvinder.accounts.dto.CustomerDto;
import com.dalvinder.accounts.dto.ResponseDto;
import com.dalvinder.accounts.service.IAccountService;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AccountController {


    private IAccountService iAccountService;

    @GetMapping("/testing")
    public String test(){
        return "Test passed....";
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto){
        iAccountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsContants.STATUS_201, AccountsContants.MESSAGE_201));
    }

    @GetMapping("/fetchUser")
    public ResponseEntity<CustomerDto> fetchUser(@RequestParam String mobileNumber) {
       CustomerDto customerDto = iAccountService.fetchUser(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam String mobileNumber) {
       boolean isUserDeleted = iAccountService.deleteUser(mobileNumber);
       if(isUserDeleted)
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsContants.STATUS_201, "Account successfully deleted"));
       else return ResponseEntity
               .status(HttpStatus.NOT_FOUND)
               .body(new ResponseDto(AccountsContants.MESSAGE_500, "Account not fount"));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody CustomerDto customerDto){
        boolean isUpdated = iAccountService.updateUser(customerDto);
        if(isUpdated)
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsContants.STATUS_201, "Account updated successfully"));
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto(AccountsContants.MESSAGE_500, "Account not found with given accout number : "));
    }
}
