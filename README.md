# mssc-brewery-client

###Microservice Client Demo project for Spring Boot

__index__
	
* Externalized hostname
* Spring RestTemplate
* HTTP Clients


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
	...
~~~


## Spring RestTemplate

Now, there's actually several different ways that you can get a hold of a RestTemplate. The recommended way as far as
when we're using Spring Boot. What we wanna do is __inject in the Rest TemplateBuilder__. What this allows us to do is configure that RestTemplate
builder so we might wanna like configure security on it or some type of HTTP client library. 

And you can also override that like everything else inside the Spring Boot and set custom properties on it. So, considered a best practice is gonna
hold the Builder and then create a Rest Template from that.

This way Iam using the standard environment Rest Template. So, you'll pick up anything that you've set up globally. If you were to create a RestTemplate locally, then you
are not going to pick up what it's been configured by Spring Boot. 

## HTTP Clients

### Communications Layers

* __HTTP - Application Layer__ ie how the client is communicating with the server, like examples writen.
* __TCP - Transmission Control Protocol__ Transport layer. <br>
How data is moved in packets between client and server.<br>
Server listens on a port (ie 80, 443), an ephemeral port is used on client to communicate back.<br>
Data is divided up into packets, transmitted, then re-assembled.<br>
* __IP - Internet Protocol__ Internet layer. Specification of how packets are moved between hosts - just one packet.

### Java Input/Output - IO

* Network communication in Java is done via `java.io` packages.
* These are low level libraries used to communicate with the host OS.
* TCP/IP connections are made va sockets.<br>
	 Light weight, but there is a cost to establish.<br>
* Early Java used one thread for each connection.<br>
	Threads are much costly.<br>
	Modern OSs can support 100s of thousands of sockets, but only ~10,000 threads.


### Blocking and I/O

* Pre Java 1.4 threads would get blocked. One thread per connection.<br>
Thread sleeps while IO completes
* Java 1.4 added non-blocking IO a.k.a. NIO - which allows for I/O without blocking the thread.<br>
	Sets of sockets now can be used by a thread
* Java 1.7 added NIO.2 with asynchronous I/O.<br>
	Networking tasks done completely in the background by the OS.
* __Non-Blocking__ is central to __Reactive Programming__.


### HTTP Client Performance

* Not uncommon for microservices to have many many client connections
* Non-blocking clients typically benchmark much higher than blocking clients
* Connection pooling can be used to avoid cost of thread creation and establishment of connections.<br>
	Non-blocking and connection pooling can have a significant difference in the performance of your application
* As will all benchmarks - Your mileage may vary!!


### Blocking Clients

* JDK - Java’s implementation
* Apache HTTP Client
* Jersey
* OkHttp - may be changing version 4 under development


### NIO Clients

* Apache Async Client
* Jersey Async HTTP Client
* Netty - Used by Reactive Spring. (default)


### HTTP/2
* HTTP/2 is more performant than HTTP 1.1
* HTTP/2 uses the TCP layer much more efficiently
	* Multiplex streams
	* Binary Protocols / Compression 
	*  Reduced Latency
	* Faster Encryption
* To the REST API Developer, Functionally the Same 
* Both server and client need to support HTTP/2

### HTTP/2 HTTP Clients

* Java 9+ 
* Jetty
* Netty
* OkHttp 
* Vert.x
* Firefly
* Apache 5.x 


## Apache HTTP Client Configuration

This is the class that you implement to tell Spring. It's a kind of a helper class that will allow you to customize the `RestTemplate`. 

~~~java
package org.springframework.boot.web.client;

import org.springframework.web.client.RestTemplate;
/**
 * Callback interface that can be used to customize a {@link RestTemplate}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see RestTemplateBuilder
 */
@FunctionalInterface
public interface RestTemplateCustomizer {

	/**
	 * Callback to customize a {@link RestTemplate} instance.
	 * @param restTemplate the template to customize
	 */
    void customize(RestTemplate restTemplate);
}
~~~

So there's quite a bit that you can do with the customization but once I implement this, Spring will pick it up and it will inject a RestTemplate for me to work with and it will customize that RestTemplate. So that standard functionality of Spring. 

Now to get started with the Apache flavor. Going to the pom, and adding a dependency:

~~~xml
		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpasyncclient</artifactId>
		</dependency>
~~~

### Blocking Customization

__1. Customization:__ Then at the implementation of the mentioned interface.

~~~java
@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    public ClientHttpRequestFactory clientHttpRequestFactory(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory();
    }

    /**
     * Customize
     * @param restTemplate
     */
    @Override
    public void customize(RestTemplate restTemplate) {
       restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    }
~~~

going into  [31] `.setRequestFactory` method, and then `ClientHttpRequestfactory`, this is an interface.  

This is standard inside Spring Framework. There we can see all the implementations (almost 10). Selecting `HttpComponentsClientHttpRequestFactory`.

Focusing on Apache, in this configuration here. But the the key thing
I want you to remember is that we are implementing implementation of this interface: the `ClientHttpRequestFactory` and Spring is gonna provide a number of implementation of that.

Highly recommend. If you want to use like `OkHTTP` which is very popular, that's a great one, here I'm gonna go through setting up the Apache flavor of it.

Come back over the `BlockingRestTemplateCustomizer`.
The important part is here it is a `@Component`. It's going to implement that `RestTemplateCustomizer` so I have to implement this method.

First the request configuration with conn and socket timeouts. Then I'm implementing a `ClosableHttpClient` and I'm configuring that with the`connectionManager` that we set up, the `requestConfig`, `KeepAliveStrategy`, so this is some pretty standard stuff as far as setting this up. If you look at the documentation, this is all standard things that you can do and there's more that you can configure. 

Finally I am returning back a new instance of that `HttpComponentsClientHttpRequestFactory`.

At line [31] spring injects in that REST template. This up as a client
requestfactory and that sets up the Apache favor specifically. 


### Non-Blocking Customization

__2. Customization:__




## Apache Client Request Logging

Actually there are a couple different ways that we can do this. In all the clients, you can set up a request interceptor and actually inspect that request and
implement your own logging logic. 

That does work well for a number of them. Some of the clients have it built in where you can use the actual logger. 
That's what I'm gonna demonstrate here. I really like the support that
Apache has.




At `application.properties` set the logging level:

~~~
logging.level.org.apache.http = debug
~~~



___

# References

[High-Concurrency HTTP Clients on the JVM](https://dzone.com/articles/high-concurrency-http-clients-on-the-jvm)

