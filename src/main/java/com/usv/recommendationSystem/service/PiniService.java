package com.usv.recommendationSystem.service;

import com.usv.recommendationSystem.repository.PiniRepository;
import org.springframework.stereotype.Service;

@Service
public class PiniService {
    private AzureBlobService azureBlobAdapter;

    private final PiniRepository piniRepository;

    public PiniService(AzureBlobService azureBlobAdapter, PiniRepository piniRepository) {
        this.azureBlobAdapter = azureBlobAdapter;
        this.piniRepository = piniRepository;
    }
}
