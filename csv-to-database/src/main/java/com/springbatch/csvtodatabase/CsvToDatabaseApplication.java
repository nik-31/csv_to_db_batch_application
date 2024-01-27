package com.springbatch.csvtodatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvToDatabaseApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(CsvToDatabaseApplication.class, args)));
	}

}
