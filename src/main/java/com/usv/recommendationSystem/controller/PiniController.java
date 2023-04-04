package com.usv.recommendationSystem.controller;

import com.usv.recommendationSystem.service.PiniService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pini")
public class PiniController {
    private final PiniService piniService;

    public PiniController(PiniService piniService) {
        this.piniService = piniService;
    }
}
