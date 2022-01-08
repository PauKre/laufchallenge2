package com.example.application.data.service;

import com.example.application.data.entity.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SampleRunService extends CrudService<Run, Integer> {

    private SampleRunRepository repository;

    public SampleRunService(@Autowired SampleRunRepository repository) {
        this.repository = repository;
    }

    @Override
    protected SampleRunRepository getRepository() {
        return repository;
    }

}
