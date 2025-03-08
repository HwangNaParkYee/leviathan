package com.example.leviathan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Problem {

    @Id @GeneratedValue
    private int id;
    private String number;
    private String title;
    private String question;
    private String hint;

}
