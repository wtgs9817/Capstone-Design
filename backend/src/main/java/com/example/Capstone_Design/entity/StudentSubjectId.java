package com.example.Capstone_Design.entity;





import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentSubjectId implements Serializable {


    private String studentNumber;
    private String subjectName;



}
