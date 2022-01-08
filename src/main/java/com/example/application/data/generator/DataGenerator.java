package com.example.application.data.generator;

import com.example.application.data.entity.Run;
import com.example.application.data.service.SamplePersonRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(SamplePersonRepository samplePersonRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (samplePersonRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<Run> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    Run.class, LocalDateTime.of(2022, 1, 7, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(Run::setId, DataType.ID);
            samplePersonRepositoryGenerator.setData(Run::setFirstName, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(Run::setLastName, DataType.LAST_NAME);
            samplePersonRepositoryGenerator.setData(Run::setEmail, DataType.EMAIL);
            samplePersonRepositoryGenerator.setData(Run::setPhone, DataType.PHONE_NUMBER);
            samplePersonRepositoryGenerator.setData(Run::setDateOfBirth, DataType.DATE_OF_BIRTH);
            samplePersonRepositoryGenerator.setData(Run::setOccupation, DataType.OCCUPATION);
            samplePersonRepositoryGenerator.setData(Run::setImportant, DataType.BOOLEAN_10_90);
            samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}