package com.ms.msscbreweryclient.web.client;

import com.ms.msscbreweryclient.web.model.BeerDto;
import com.ms.msscbreweryclient.web.model.CustomerDto;
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

    public final String CUSTOMER_PATH_V1 = "/api/v1/customer";

    private String apihost;


    private final RestTemplate restTemplate;

    /**
     * Constructor. Inject a rest template builder into the client.
     * @param restTemplate template builder
     */
    public BreweryClient(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    /**
     * Get
     * @param uuid beer id
     * @return BeerDto
     */
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

    /**
     * Cet
     * @param uuid client id
     * @return customer
     */
    public CustomerDto getClientById(UUID uuid) {
        return this.restTemplate.getForObject(apihost + CUSTOMER_PATH_V1 + "/" + uuid.toString(), CustomerDto.class);
    }

    public void deleteCustomer(UUID uuid) {
        this.restTemplate.delete(apihost + CUSTOMER_PATH_V1 + "/" + uuid.toString());
    }

    public URI createCustomer(CustomerDto customer) {
        return this.restTemplate.postForLocation(apihost + CUSTOMER_PATH_V1, customer);
    }

    public void  updateCustomer(UUID uuid, CustomerDto customer) {
        this.restTemplate.put(apihost + CUSTOMER_PATH_V1 + "/" + uuid.toString(), customer);
    }


    /** ------
     */
    public void setApihost(String apihost) {
        this.apihost = apihost;
    }

}
