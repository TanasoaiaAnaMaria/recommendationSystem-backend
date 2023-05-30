package com.usv.recommendationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersoanaDto {

    private String nume;

    private String prenume;

    private String email;

    private String parola;

    private String preferinte;
}
