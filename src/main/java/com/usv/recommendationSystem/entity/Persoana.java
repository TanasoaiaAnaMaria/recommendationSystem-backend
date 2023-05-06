package com.usv.recommendationSystem.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Persoana {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String imagine;

    private String nume;

    private String prenume;

    private String email;

    private String parola;

}
