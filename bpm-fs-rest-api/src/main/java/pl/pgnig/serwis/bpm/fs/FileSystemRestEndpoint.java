/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.pgnig.serwis.bpm.fs.serv.FileSystemService;

/**
 *
 * @author jerzy.malyszko
 */
@RestController
public class FileSystemRestEndpoint {

    @Autowired
    private FileSystemService fsService;

    @GetMapping("/beep")
    String beep() {
        return "BEEP!";
    }

    @GetMapping("/pwd")
    public ResponseEntity<String> pwd() {
        String pwd = fsService.pwd();
        return ResponseEntity.ok(pwd);
    }

    @PostMapping("/mkdir/{dirName}")
    public ResponseEntity<String> mkdir(@PathVariable String dirName) {
        fsService.mkdir(dirName);
        return ResponseEntity.status(HttpStatus.CREATED).header("location", dirName).build();
    }

    @GetMapping(value = "/ll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ll() {
        final List<String> collect = fsService.ll().stream().sorted((p1,p2) -> Boolean.compare(Files.isDirectory(p1), Files.isDirectory(p2)) )
                .map(pth -> pth.toString()).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @PutMapping("/cd")
    public ResponseEntity cd() {
        fsService.cd(null);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cd/{dest}")
    public ResponseEntity cd(@PathVariable String dest) {
        fsService.cd(dest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/touch/{file}")
    public ResponseEntity touch(@PathVariable String file) {
        Path pth = fsService.touch(file);
        return ResponseEntity.created(pth.toUri()).build();
    }

    @PostMapping(value = "/touch", headers = {"content-type=multipart/*"})
    public ResponseEntity touch(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        Path touched = fsService.touch(originalFilename, file.getBytes());
        return ResponseEntity.created(touched.toUri()).build();
    }

    @GetMapping("/stat/{file}")
    public ResponseEntity stat(@PathVariable String file) {
        final Map<String, String> collect = fsService.stat(file).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, ent -> String.valueOf(ent.getValue())));
        return ResponseEntity.ok().body(collect);
    }

}
