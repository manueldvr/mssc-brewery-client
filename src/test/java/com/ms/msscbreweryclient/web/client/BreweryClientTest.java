package com.ms.msscbreweryclient.web.client;

import com.ms.msscbreweryclient.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BreweryClientTest {

    @Autowired
    BreweryClient client;


    @Test
    void getBeerById() {
        BeerDto beer = this.client.getBeerById(UUID.randomUUID());
        assertNotNull(beer);
    }

    @Test
    void saveBeer(){
        BeerDto beer = BeerDto.builder().id(UUID.randomUUID()).beerName("Quilmes").build();
        URI uri = client.saveNewBeer(beer);
        assertNotNull(uri);
        System.out.println("URI: " + uri.toString());
    }

    @Test
    void updateBeer(){
        BeerDto beer = BeerDto.builder().id(UUID.randomUUID()).beerName("Quilmes").build();
        client.updateBeerById(UUID.randomUUID(), beer);
    }

    @Test
    void deleteBeer(){
        client.deleteBeer(UUID.randomUUID());
    }

    @Test
    void setApihost() {
    }
}