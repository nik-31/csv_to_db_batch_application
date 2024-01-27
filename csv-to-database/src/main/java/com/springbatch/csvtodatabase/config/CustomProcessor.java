package com.springbatch.csvtodatabase.config;

import com.springbatch.csvtodatabase.model.Employee;
import org.springframework.batch.item.ItemProcessor;

public class CustomProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee item) {
        try{
            item.setFirstname(item.getFirstname().toUpperCase());
            item.setLastname(item.getLastname().toUpperCase());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return item;
    }
}
