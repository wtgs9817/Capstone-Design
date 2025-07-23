package com.example.Capstone_Design.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GraduationCheckServiceTest {

    @Autowired
    GraduationCheckService graduationCheckService;

    @Test
    void totalSubjectScore() {
    }

    @Test
    void getSubjectScore() {
    }

    @Test
    void getSubjectCheckList() {
    }

    @Test
    void graduationSubject() {
    }

    @Test
    void graduationCheck() {
    }

    @Test
    void getMajorCode() {
    }

    @Test
    void studentSubjectSave() {
        List<String> list = new ArrayList<>();
        list.add("운영체제");
        list.add("C프로그래밍");
        graduationCheckService.studentSubjectSave("20175112",list);

    }

    @Test
    void getGraduationSubjectList() {
    }

    @Test
    void getSubjects() {
    }

    @Test
    void getGraduationCheckResults() {
    }
}