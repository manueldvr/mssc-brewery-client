package com.ms.msscbreweryclient.web.client;

import com.ms.msscbreweryclient.web.model.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Component
@ConfigurationProperties(value = "sfg.brewery", ignoreUnknownFields = false)
public class BreweryClient {

    public final String BEER_PATH_V1 = "/api/v1/beer";

    private String apihost;


    private final RestTemplate restTemplate;

    /**
     * Constructor. Inject a rest template builder into the client.
     * @param restTemplate
     */
    public BreweryClient(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    public BeerDto getBeerById(UUID uuid) {
        return this.restTemplate.getForObject(apihost + BEER_PATH_V1 + "/" + uuid.toString(), BeerDto.class);
    }

    public URI saveNewBeer(BeerDto beer){
        return  this.restTemplate.postForLocation(apihost + BEER_PATH_V1, beer);
    }

    public void updateBeerById(UUID uuid, BeerDto beer) {
        this.restTemplate.put(apihost + BEER_PATH_V1 + "/" + uuid.toString(), beer);
    }

    public void deleteBeer(UUID uuid){
        this.restTemplate.delete(apihost + BEER_PATH_V1 + "/" + uuid.toString());
    }

    /***/
    public void setApihost(String apihost) {
        this.apihost = apihost;
    }
}
