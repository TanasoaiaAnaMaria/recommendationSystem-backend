package com.usv.recommendationSystem.controller;


import com.usv.recommendationSystem.dto.PersoanaDto;
import com.usv.recommendationSystem.entity.Persoana;
import com.usv.recommendationSystem.service.PersoanaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @PostMapping("/inregistrare")
    public ResponseEntity<Persoana> addPersoanaInregistrare( @ModelAttribute PersoanaDto persoanaDto) throws IOException {
        return ResponseEntity.ok(persoanaService.addPersoanaInregistrare(persoanaDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persoana> getPersoanaDupaId(@PathVariable UUID id){
        return ResponseEntity.ok(persoanaService.getPersoanaDupaId(id));
    }

    @GetMapping("/autentificare")
    public ResponseEntity<Persoana> getPersoanaAutentificare(@RequestParam("email") String email, @RequestParam("parola") String parola){
        Persoana authenticatedPerson = persoanaService.getPersoanaAutentificare(email, parola);
        if (authenticatedPerson == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(authenticatedPerson);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persoana> updatePersoana(
            @ApiParam(value = "The ID of the person to update", required = true) @PathVariable UUID id,
            @ApiParam(value = "The details of the person to update", required = true) @ModelAttribute PersoanaDto persoanaDto,
            @ApiParam(value = "The file to upload", required = true) @RequestParam("file") Optional<MultipartFile> file) throws IOException {
        if(file.isPresent())
            return ResponseEntity.ok(persoanaService.updatePersoana(id,persoanaDto,file.get()));
        else
            return ResponseEntity.ok(persoanaService.updatePersoanaFaraPoza(id,persoanaDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersoana(@PathVariable UUID id){
        persoanaService.deletePersoana(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
