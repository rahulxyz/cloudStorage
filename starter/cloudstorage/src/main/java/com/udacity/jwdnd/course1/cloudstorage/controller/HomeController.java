package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService,FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    public String HomeView(Authentication authentication, Model model){
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("noteList", this.noteService.getNotes(userId));
        model.addAttribute("credentialList", this.credentialService.getCredentials(userId));
        model.addAttribute("fileList", this.fileService.getFiles(userId));
        return "home";
    }
}
