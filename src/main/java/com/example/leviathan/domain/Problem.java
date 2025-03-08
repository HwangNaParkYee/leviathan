package com.example.leviathan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Problem {

    @Id @GeneratedValue
    private int id;
    private String number;
    private String title;
    private String question;
    private String hint;

}
