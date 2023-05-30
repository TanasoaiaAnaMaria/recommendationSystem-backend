package com.usv.recommendationSystem.service;

import com.usv.recommendationSystem.classifiers.Classifier_KNN;
import com.usv.recommendationSystem.dto.PersoanaDto;
import com.usv.recommendationSystem.entity.Persoana;
import com.usv.recommendationSystem.exceptii.CrudOperationException;
import com.usv.recommendationSystem.learningsets.SupervisedLearningSet;
import com.usv.recommendationSystem.repository.PersoanaRepository;
import com.usv.recommendationSystem.utils.FileUtils1;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class PersoanaService {
    @Value("${google.maps.api.key}")
    private String apiKey;
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
                                .imagine(pers.getImagine()!=null?azureBlobAdapter.getFileURL(pers.getImagine()):"")
                                .nume(pers.getNume())
                                .prenume(pers.getPrenume())
                                .email(pers.getEmail())
                                .parola(pers.getParola())
                                .preferinte(pers.getPreferinte())
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

    public Persoana getPersoanaAutentificare(String email, String parola){
        Optional<Persoana> optionalPersoana = persoanaRepository.findByEmail(email);
        if (optionalPersoana.isPresent()) {
            Persoana persoana = optionalPersoana.get();
            if (persoana.getParola().equals(parola)) {
                return Persoana.builder()
                        .id(persoana.getId())
                        .imagine(persoana.getImagine() != null ? azureBlobAdapter.getFileURL(persoana.getImagine()) : "")
                        .nume(persoana.getNume())
                        .prenume(persoana.getPrenume())
                        .email(persoana.getEmail())
                        .parola(persoana.getParola())
                        .preferinte(persoana.getPreferinte())
                        .build();
            }
        }
        return null;

    }

    public Persoana addPersoanaInregistrare (PersoanaDto persoanaDto) throws IOException {

        Persoana persoana=Persoana.builder()
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
        persoana.setPreferinte(persoanaDto.getPreferinte());

        persoanaRepository.save(persoana);
        return persoana;
    }

   public Persoana updatePersoanaFaraPoza(UUID id, PersoanaDto persoanaDto) throws IOException {
        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
            throw new CrudOperationException(MESAJ_DE_EROARE);
        });

        persoana.setNume(persoanaDto.getNume());
        persoana.setPrenume(persoanaDto.getPrenume());
        persoana.setEmail(persoanaDto.getEmail());
        persoana.setParola(persoanaDto.getParola());
        persoana.setPreferinte(persoanaDto.getPreferinte());

        persoanaRepository.save(persoana);
        return persoana;
    }

    public void deletePersoana(UUID id){
        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
            throw new CrudOperationException(MESAJ_DE_EROARE);
        });

        if(persoana.getImagine()!=null)
            azureBlobAdapter.deleteBlob(persoana.getImagine());
        persoanaRepository.delete(persoana);
    }

    public String predictie(UUID id, double latitudine, double longitudine) {

        String filePath = id + ".txt";
        double userLatitude = 47.6468952302915;
        double userLongitude = 26.2429388802915;

        String closestPlace = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            closestPlace = null;
            double minDistance = Double.MAX_VALUE;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");
                double placeLatitude = Double.parseDouble(parts[0].trim());
                double placeLongitude = Double.parseDouble(parts[1].trim());

                double distance = calculateDistance(userLatitude, userLongitude, placeLatitude, placeLongitude);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestPlace = parts[2].trim();
                }
            }

            System.out.println("Closest place: " + closestPlace);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return closestPlace;

//        Persoana persoana=persoanaRepository.findById(id).orElseThrow(()->{
//            throw new CrudOperationException(MESAJ_DE_EROARE);
//        });
//
//        FileUtils1.setinputFileValuesSeparator(", ");
//        SupervisedLearningSet userSet = new SupervisedLearningSet(id + ".txt");
//
//        System.out.println(userSet);
//
//        int rowCount=0;
//        try {
//            rowCount = (int) Files.lines(Path.of(id + ".txt")).count();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(rowCount);
//
//        Classifier_KNN knnClassifier = new Classifier_KNN(rowCount); //in cerinta respectiva se cere k=5; alternativ se alege o valoare random care sa nu depaseasca numarul de clase sau se calculeaza cu rule of thumb sqrt(N)
//        knnClassifier.train(userSet);
//        System.out.println("________________________________");
//        System.out.println(userSet);
//        //Implement Predict and display class numeric ID in Classifier_KNN class
//        int identifiedClassIDKNN = knnClassifier.predict(new double[]{latitudine, longitudine});
//        System.out.println("The identified ID of the pattern z using the KNN Classifier is: " + identifiedClassIDKNN);
//        //Display class name
//        System.out.println("The name of the class using the KNN Classifier is: " + userSet.getClassNames()[identifiedClassIDKNN]);
//
//        return userSet.getClassNames()[identifiedClassIDKNN];
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }


}


