package com.springbatch.csvtodatabase.config;

import com.springbatch.csvtodatabase.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {
    @Bean
    public Job jobBean(JobRepository jobRepository,
                       Step step,
                       JobCompletionImpl listener){
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<Employee> reader,
                     ItemProcessor<Employee, Employee> processor,
                     ItemWriter<Employee> writer){
        return new StepBuilder("jobStep", jobRepository)
                .<Employee, Employee>chunk(20, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Employee> reader(){
        return new FlatFileItemReaderBuilder<Employee>()
                .name("itemReader")
                .resource(new ClassPathResource("salary_data.csv"))
                .delimited()
                .names("id","firstname","lastname","email","salary")
                .targetType(Employee.class)
                .build();
    }

    @Bean
    public ItemProcessor<Employee, Employee> itemProcessor(){
        return new CustomProcessor();
    }

    @Bean
    public ItemWriter<Employee> itemWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Employee>()
                .sql("insert into salary_data(id,firstname,lastname,email,salary) values(:id, :firstname, :lastname, :email, :salary)")
                .dataSource(dataSource)
                .beanMapped()
                .build();

    }
}
