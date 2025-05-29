package com.example.Capstone_Design.repository;

import com.example.Capstone_Design.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

    SubjectEntity findBySubjectName(String subjectName);
    List<SubjectEntity> findAllBySubjectNameIn(List<String> subjectNames);

}

