package com.tphelps.backend.controller;

import com.tphelps.backend.dtos.ChangePasswordRequest;
import com.tphelps.backend.dtos.DeleteAccountRequest;
import com.tphelps.backend.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AccountController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Allow a user to change their password while logged in
     * @param request - the change password request containing old and new passwords
     * @return - ok on success, else unauthorized
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            try {
                customUserDetailsService.changePassword(
                        (UserDetails) authentication.getPrincipal(),
                        request.oldPassword(),
                        request.newPassword());

                return ResponseEntity.ok("Password Changed Successfully");

            }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Delete an account using the users password entered on the front end
     * @param request - the delete request containing the password to match
     * @return - <code>200 on success</code>, <code>401 if not authenticated</code>, <code>400 if bad request</code>
     */
    @PostMapping("delete")
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteAccountRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            try{
                customUserDetailsService.deleteAccount(
                        (UserDetails) authentication.getPrincipal(),
                        request.password()
                );

                return ResponseEntity.ok().build();
            }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
