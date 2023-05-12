package com.ms.msscbreweryclient.web.client;

import com.ms.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomerTest {

    @Autowired
    BreweryClient client;


    @Test
    void getCustomerById(){
        CustomerDto customer = this.client.getClientById(UUID.randomUUID());
        assertNotNull(customer);
    }

    @Test
    void deleteCustomerById(){
        this.client.deleteCustomer(UUID.randomUUID());
    }

    @Test
    void createCustomer(){
        URI uri = this.client.createCustomer(CustomerDto.builder().id(UUID.randomUUID()).name("My Name is").build());
        assertNotNull(uri);
        System.out.println("URI: " + uri);
    }

    @Test
    void updateCustomer(){
        this.client.updateCustomer( UUID.randomUUID(), CustomerDto.builder().id(UUID.randomUUID()).name("My Name is").build());
    }
}
