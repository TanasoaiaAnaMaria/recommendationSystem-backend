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
public class Pini {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}
