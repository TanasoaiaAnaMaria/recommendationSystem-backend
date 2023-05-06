package com.usv.recommendationSystem.controller;


import com.usv.recommendationSystem.dto.PersoanaDto;
import com.usv.recommendationSystem.entity.Persoana;
import com.usv.recommendationSystem.service.PersoanaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/persoana")
public class PersoanaController {
    private final PersoanaService persoanaService;

    public PersoanaController(PersoanaService persoanaService) {
        this.persoanaService = persoanaService;
    }

    @GetMapping
    public ResponseEntity<List<Persoana>> getPersoane(){
        return ResponseEntity.ok(persoanaService.getPersoane());
    }

    @PostMapping
    public ResponseEntity<Persoana> addPersoana(@RequestParam("file") MultipartFile file, @ModelAttribute PersoanaDto persoanaDto) throws IOException {
        return ResponseEntity.ok(persoanaService.addPersoana(file,persoanaDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Persoana> getPersoanaDupaId(@PathVariable UUID id){
        return ResponseEntity.ok(persoanaService.getPersoanaDupaId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persoana> updatePersoana(@PathVariable UUID id, @ModelAttribute PersoanaDto persoanaDto, @RequestParam("file") Optional<MultipartFile> file) throws IOException {
//        if(file.isPresent())
            return ResponseEntity.ok(persoanaService.updatePersoana(id,persoanaDto,file.get()));
//        else
//            return ResponseEntity.ok(persoanaService.updatePersoanaFaraPoza(id,persoanaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersoana(@PathVariable UUID id){
        persoanaService.deletePersoana(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
