package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public void uploadFile(HttpServletResponse response, Authentication authentication, Model model, @RequestParam("fileUpload") MultipartFile file) throws IOException {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        File preExisitingFile = fileService.getFile(file.getOriginalFilename());

        if(preExisitingFile == null) {
            fileService.insertFile(new File(
                    null,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    userId,
                    file.getBytes()
            ));
        }else{
            model.addAttribute("error","Filename already exists!");
        }

        response.sendRedirect("/home");
    }


    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> getFile(Integer fileId) {
        File file = fileService.downloadFile(fileId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +  file.getFileName() + "\"" ).body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/delete")
    public void deleteFile(HttpServletResponse response,Model model, Integer fileId) throws IOException {
        fileService.deleteFile(fileId);

        response.sendRedirect("/result");
    }
}
