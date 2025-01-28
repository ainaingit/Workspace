package com.example.workspace.entity;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;

    @Column(unique=false)
    private String firstName;

    @Column(unique=true)
    private String number;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client() {
    }

    public Client(Long id, String firstName, String number) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setNumber(number);
    }
}
