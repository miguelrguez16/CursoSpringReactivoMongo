package com.example.prueba;


import com.example.prueba.batch.TransformItemProcessor;
import com.example.prueba.model.Coffee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        //This BatchConfigurer ignores any DataSource
    }


    @Bean
    public ItemReader<Coffee> reader() {
        FlatFileItemReader<Coffee> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("coffe-list.csv"));
        reader.setLineMapper(new DefaultLineMapper<Coffee>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"brand", "origin", "characteristics"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Coffee>() {{
                setTargetType(Coffee.class);
            }});
        }});
        return
                reader;
    }

    @Bean
    public ItemWriter<Coffee> writer() {
        FlatFileItemWriter<Coffee> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(new File("outputResource.csv")));

        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<Coffee>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Coffee>() {
                    {
                        setNames(new String[]{"brand", "origin", "characteristics"});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public ItemProcessor<Coffee, Coffee> processor() {
        return new TransformItemProcessor();
    }


    @Bean
    public Job tareaJob(JobBuilderFactory jobs, Step step1) {
        return jobs.get("tareaJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end().build();
    }

    // CONFIGURAR EL STEP
    // HACE USO DE read write y el procesor
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Coffee> coffeeItemReader, ItemWriter<Coffee> coffeeItemWriter, ItemProcessor<Coffee, Coffee> coffeeItemProcessor) {
        return stepBuilderFactory.get("step1").<Coffee, Coffee>chunk(1).
                reader(coffeeItemReader)
                .writer(coffeeItemWriter)
                .processor(coffeeItemProcessor).build();
    }

}
