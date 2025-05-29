package com.example.Capstone_Design.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "major")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MajorEntity {

    @Id
    private String majorCode;

    private String major;


}
