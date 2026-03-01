package com.nazarukiv.gitview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDto implements Serializable {
    private String name;
    private Double percentage;
    private String color;
}