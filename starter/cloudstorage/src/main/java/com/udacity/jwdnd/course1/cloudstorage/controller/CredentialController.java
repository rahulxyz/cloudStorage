package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateCredential(Authentication authentication, Model model, @ModelAttribute Credential credential) {
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        credential.setUserId(userId);

        if (credential.getCredentialId() != null && credential.getCredentialId() > 0){
            try {
                credentialService.updateCredential(credential);
                model.addAttribute("success", true);
                model.addAttribute("message", "Credential updated!");
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Cannot update credential " + e.getMessage());
            }
        }else{
            try {
                credentialService.createCredential(credential);
                model.addAttribute("success", true);
                model.addAttribute("message", "New credential added!");
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Cannot find credential " + e.getMessage());
            }

        }
        return "result";
    }

    @PostMapping("/delete/{credentialId}")
    public String deleteCredential(Authentication authentication, Model model, @PathVariable Integer credentialId){
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        try {
            credentialService.deleteCredential(credentialId, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "Credential removed!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Cannot find credential " + e.getMessage());
        }

        return "result";
    }
}
