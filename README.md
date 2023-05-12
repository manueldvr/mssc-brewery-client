# mssc-brewery-client

Microservice Client Demo project for Spring Boot

__index__

* Externalized hostname
* Spring RestTemplate


## Externalized hostname

how to setup an externalized hostname that will get injected by Spring Framework into my class for the hostname. So, when we want to deploy this
application out in the real world, we don't have it hard-coded.


1. 
Add property
> `sfg.brewery.apihost=http://localhost:8080`

2. 
Set up 
`@ConfigurationProperties` with `sfg.brewery`, that will works as a prefix.
And add a property to `BreweryClient`:

~~~java
@Component
@ConfigurationProperties(value = "sfg.brewery", ignoreUnknownFields = false)
public class BreweryClient {

    public final String BEER_PATH_V1 = "api/v1/beer/";

    private String apihost;
~~~


## Spring RestTemplate

Now, there's actually several different ways that you can get a hold of a RestTemplate. The recommended way as far as
when we're using Spring Boot. What we wanna do is __inject in the Rest TemplateBuilder__. What this allows us to do is configure that RestTemplate
builder so we might wanna like configure security on it or some type of HTTP client library. 

And you can also override that like everything else inside the Spring Boot and set custom properties on it. So, considered a best practice is gonna
hold the Builder and then create a Rest Template from that.

This way Iam using the standard environment Rest Template. So, you'll pick up anything that you've set up globally. If you were to create a RestTemplate locally, then you
are not going to pick up what it's been configured by Spring Boot. 
