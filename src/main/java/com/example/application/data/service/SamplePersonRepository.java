package com.example.application.data.service;

import com.example.application.data.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<Run, Integer> {

}