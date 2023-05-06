package com.usv.recommendationSystem.service;

import com.usv.recommendationSystem.dto.PersoanaDto;
import com.usv.recommendationSystem.entity.Persoana;
import com.usv.recommendationSystem.exceptii.CrudOperationException;
import com.usv.recommendationSystem.repository.PersoanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PersoanaService {
    public static final String MESAJ_DE_EROARE = "Hello, welcome to the server";

    @Autowired
    private final AzureBlobService azureBlobAdapter;

    private final PersoanaRepository persoanaRepository;

    public PersoanaService(AzureBlobService azureBlobAdapter, PersoanaRepository persoanaRepository) {
        this.azureBlobAdapter = azureBlobAdapter;
        this.persoanaRepository = persoanaRepository;
    }

    @EntityGraph(value = "topic.all")
    public List<Persoana> getPersoane(){
        Iterable<Persoana> iterblePersoana=persoanaRepository.findAll();
        List<Persoana> persoane=new ArrayList<>();

        iterblePersoana.forEach(pers->
                persoane.add(Persoana.builder()
                                .id(pers.getId())
                                .imagine(pers.getImagine().length()!=0?azureBlobAdapter.getFileURL(pers.getImagine()):"")
                                .nume(pers.getNume())
                                .prenume(pers.getPrenume())
                                .email(pers.getEmail())
                                .parola(pers.getParola())
                                .build()));
        return persoane;
    }

    public Persoana getPersoanaDupaId(UUID id){
        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
            throw new CrudOperationException(MESAJ_DE_EROARE);
        });

        persoana.setImagine(persoana.getImagine().length()!=0?azureBlobAdapter.getFileURL(persoana.getImagine()):"");

        return persoana;
    }

    public Persoana addPersoana (MultipartFile file, PersoanaDto persoanaDto) throws IOException {
        String fileName="";
        if(!file.isEmpty())
            fileName = azureBlobAdapter.upload(file);
        Persoana persoana=Persoana.builder()
                .imagine(fileName)
                .nume(persoanaDto.getNume())
                .prenume(persoanaDto.getPrenume())
                .email(persoanaDto.getEmail())
                .parola(persoanaDto.getParola())
                .build();
        persoanaRepository.save(persoana);
        return persoana;
    }

    public Persoana updatePersoana(UUID id, PersoanaDto persoanaDto, MultipartFile file) throws IOException {
        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
            throw new CrudOperationException(MESAJ_DE_EROARE);
        });
        String fileName;
        if(!file.isEmpty()){
            azureBlobAdapter.deleteBlob(persoana.getImagine());
            fileName = azureBlobAdapter.upload(file);
        }
        else
            fileName=persoana.getImagine();
        persoana.setImagine(fileName);
        persoana.setNume(persoanaDto.getNume());
        persoana.setPrenume(persoanaDto.getPrenume());
        persoana.setEmail(persoanaDto.getEmail());
        persoana.setParola(persoanaDto.getParola());

        persoanaRepository.save(persoana);
        return persoana;
    }

//    public Persoana updatePersoanaFaraPoza(UUID id, PersoanaDto persoanaDto) {
//        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
//            throw new CrudOperationException(MESAJ_DE_EROARE);
//        });
//        persoana.setNume(persoanaDto.getNume());
//        persoana.setPrenume(persoanaDto.getPrenume());
//        persoana.setDataNasterii(persoanaDto.getDataNasterii());
//        persoana.setInaltime(persoanaDto.getInaltime());
//        persoana.setNationalitate(persoanaDto.getNationalitate());
//        persoana.setPersonal(persoanaDto.getPersonal());
//        persoana.setPost(persoanaDto.getPost());
//        persoana.setDescriere(persoanaDto.getDescriere());
//
//        persoanaRepository.save(persoana);
//        return persoana;
//    }

    public void deletePersoana(UUID id){
        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
            throw new CrudOperationException(MESAJ_DE_EROARE);
        });

        if(persoana.getImagine().length()!=0)
            azureBlobAdapter.deleteBlob(persoana.getImagine());
        persoanaRepository.delete(persoana);
    }
}
