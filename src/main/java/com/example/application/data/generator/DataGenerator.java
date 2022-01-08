package com.example.application.data.generator;

import com.example.application.data.entity.Run;
import com.example.application.data.service.SampleRunRepository;
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
    public CommandLineRunner loadData(SampleRunRepository sampleRunRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (sampleRunRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<Run> sampleRunGenerator = new ExampleDataGenerator<>(
                    Run.class, LocalDateTime.of(2022, 1, 7, 0, 0, 0));
            sampleRunGenerator.setData(Run::setId, DataType.ID);
            sampleRunGenerator.setData(Run::setName, DataType.FIRST_NAME);
            sampleRunGenerator.setData(Run::setDistance, DataType.PRICE);
            sampleRunGenerator.setData(Run::setTime, DataType.PRICE);
            sampleRunRepository.saveAll(sampleRunGenerator.create(10, seed));

            logger.info("Generated demo data");
        };
    }

}