package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private UserService userService;
    private NoteService noteService;

    public NotesController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping
    public String createOrUpdateNote(Authentication authentication, Model model, @ModelAttribute Notes note){
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        note.setUserId(userId);

        if(note.getNoteId() != null && note.getNoteId() > 0) {
            try {
                noteService.updateNote(note);
                model.addAttribute("success", true);
                model.addAttribute("message", "Note updated!");
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Cannot update note " + e.getMessage());
            }
        }else{
            try {
                noteService.createNote(note);
                model.addAttribute("success", true);
                model.addAttribute("message", "New note added!");
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Cannot find note " + e.getMessage());
            }
        }

        return "result";
    }

    @PostMapping("/delete/{noteId}")
    public String deleteNote(Authentication authentication, Model model, @PathVariable Integer noteId){
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        try {
            noteService.deleteNote(noteId, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "Credential removed!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Cannot find credential " + e.getMessage());
        }

        return "result";
    }
}
