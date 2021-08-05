# REST API Client Library 

[![REUSE status](https://api.reuse.software/badge/github.com/SAP/cloud-rest-api-client)](https://api.reuse.software/info/github.com/SAP/cloud-rest-api-client)

Java HTTP client library for HTTP handling, when building clients for RESTful APIs.

## Prerequisities and Dependencies

- Java 8
- [Apache HTTP Client](https://hc.apache.org/httpcomponents-client-ga/index.html)
- [Jackson Faster XML](https://github.com/FasterXML/jackson)
- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)  

## Installation

Build the library using the following maven command.

```
mvn clean install
```

## Executing Unit Tests

The unit tests of the library can be executed with the following Maven command

```
mvn clean install -Punit-tests
```

# Usage

Add the following dependency to your maven __pom.xml__ file.

```
<dependency>
    <groupId>com.sap.cloud.client</groupId>
    <artifactId>rest-api-client</artifactId>
    <version>${latest.version}</version>
</dependency>
```

## Creating a RestApiClient

To create a Rest API Client, you need to extend the __RestApiClient__ class and construct it by passing a __RestApiClientConfig__ to it.

```java
public class DefaultRestApiClient extends RestApiClient {

    private static final String API_PATH = "/api";

    public DefaultRestApiClient(RestApiClientConfig restApiClientConfig) {
        super(restApiClientConfig);
    }

    @Override
    protected String getApiPath() {
        return API_PATH;
    }
}
```

## Using a Custom HttpClientProvider

An __HttpClientProvider__ is an interface with one method ```HttpClient createHttpClient()```.
 The __HttpClient__ returned by this method is used for executing requests in __RestApiClient__. By default, an __HttpClientProviderFactory__ creates default __HttpClientProvider__ implementations depending on the __AuthenticationType__ found in ```restApiClientConfig```.

__RestApiClient__ has a constructor where a custom implementation can be provided, to override the default http clients used.

```java
    public DefaultRestApiClient(RestApiClientConfig restApiClientConfig, HttpClientProvider httpClientProvider) {
        super(restApiClientConfig, httpClientProvider);
    }
```

## Creating a RestApiClientConfig

__RestApiClientConfig__ has a host and an object of type __Authentication__. You can construct it using ```RestApiClientConfigBuilder.getBuilder()``` to get an instance of the __RestApiClientConfigBuilder__. Supported authentication types are __ClientCertAuthentication__, __BasicAuthentication__, __OAuthAuthentication__ and __NoAuthentication__. It also supports configuring a proxy that will be used in the __RestApiClient__.

### No Authentication

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .authentication(new NoAuthentication())
    .build();
```

You can also use the utility method for No Authentication.

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .noAuthentication()
    .build();
```

You could also skip the ```noAuthentication()``` method. __RestApiClientConfigBuilder__ constructs configurations with no authentication by default.

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(<host>)
                .build();
```

### Basic Authentication

To use basic authentication, you have to provide a username and password.

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .authentication(new BasicAuthentication(<username>, <password>))
    .build();
```

You can also use the utility method for Basic Authentication.

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .basicAuthentication(<username>, <password>)
    .build();
```

### Client Certificate Authentication 

To use the client certificate authentication, you have to provide a __KeystoreConfig__. You can construct it using ```KeystoreConfigBuilder.getBuilder()``` to get an instance of the __KeystoreConfigBuilder__.

```java
KeyStore keystore;  // initialize with your keystore

KeystoreConfig keystoreConfig = KeystoreConfigBuilder.getBuilder()
    .keystore(keystore)
    .keystorePassword("<password>")
    .keyAlias("<key alias>")
    .build();

RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .authentication(new ClientCertAuthentication(keystoreConfig))
    .build();
```

You can also use the utility method for Client Certificate Authentication.

```java    
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host(<host>)
    .clientCertAuthentication(keystoreConfig)
    .build();
```

__Note__: By default, the __SSLHttpClientProvider__ returned from __HttpClientProviderFactory__ creates an http client with connection timeout of 30 seconds (30000 milliseconds). To configure the timeout, create an __SSLHttpClientProvider__ with a custom timeout (in milliseconds) and pass it to the __RestApiClient__ constructor.

```java
KeystoreConfig keystoreConfig; // initialize as above
RestApiClientConfig config; // initialize as above

ClientCertAuthentication authentication = new ClientCertAuthentication(keystoreConfig);
SSLHttpClientProvider clientProvider = new SSLHttpClientProvider(authentication, <timeoutInMillis>);

RestApiClient restApiClient = new DefaultRestApiClient(config, clientProvider);
```

### OAuth Authentication

To use OAuth authentication you have to provide an __OAuthServerConfig__. It is used for requesting access tokens. You can construct it using ```OAuthServerConfigBuilder.getBuilder()``` to get an instance of the __OAuthServerConfigBuilder__

```java
OAuthServerConfig oAuthServerConfig = OAuthServerConfigBuilder.getBuilder()
    .oAuthServerHost(<OAuth server host>) // e.g https://authentication.sap.hana.ondemand.com
    .clientID(<client ID>)
    .clientSecret("<client secret>".toCharArray())
    .build();
                
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host("<host>")
    .authentication(new OAuthAuthentication(oAuthServerConfig))
    .build();
```

You can also use the utility method for OAuth Authentication.

```java    
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host("<host>")
    .oauthAuthentication(oAuthServerConfig)
    .build();
```
                
__Note__: By default, the __OAuthServerConfigBuilder__ creates a configuration with an API path ```/oauth/token```. If the path of the server is different, you can set it explicitly with the ```oAuthServerApiPath("<OAuth server API path>")``` method. For example, the access token endpoint for Neo currently has an API path of ```/oauth2/api/v1/token```. 

__Note__: By default, the __OAuthServerConfigBuilder__ creates a configuration with an OAuth Header key ```Authorization```. If the header key you want to set the Bearer header on is different, you can set it explicitly with the ```oAuthHeaderKey("<OAuth Header key>")``` method.

### Configuring a Proxy

To set up a proxy for the __RestApiClient__, pass a __Proxy__ object to the __RestApiClientConfig__. The __Proxy__ object consists of hostname, port and scheme.

```java
Proxy proxy = new Proxy("<hostname>", 8888, "https"); //Represents a proxy with host https://<hostname>:8888
```

If the scheme is not provided, it is ```http``` by default.

```java
Proxy proxy = new Proxy("<hostname>", 8888); //Represents a proxy with host http://<hostname>:8888
```

Then attach the created __Proxy__ to the __RestApiClientConfigBuilder__. When building the configuration, the builder will create an __HttpRoutePlanner__, using the __Proxy__ object. This route planner will be set to the __HttpClient__ that is internally used in the __RestApiClient__.

```java
RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host("<host>")
    .proxy(proxy)
    .build();
```

The __RestApiClientConfigBuilder__ also supports directly passing an __HttpRoutePlanner__ to the proxy method.

```java
HttpHost httpHost = new HttpHost("<hostname>", 8888, "http");
HttpRoutePlanner httpRoutePlanner = new DefaultProxyRoutePlanner(httpHost);

RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
    .host("<host>")
    .proxy(httpRoutePlanner)
    .build();
```

## Using the RestApiClient

To execute a request with __RestApiClient__ you have to build a __Request__ object and call the ```Response<String> execute(Request<RequestType> request)``` method. 

__Request__ is a generic class that consists of an __HttpUriRequest__ and an entity representing the request body. It can be constructed using the provided __RequestBuilder__. Passing a URI is obligatory. It support deserialization of objects to JSON using the Jackson library. It also supports multipart requests.

Initialize the builder with one of its static methods.

```java
Request<TestEntity> request = RequestBuilder
                .getRequest()
                .uri(<uri>)
                .addParameter(<queryParamName>, <queryParamValue>)
                .addHeader(<headerName>, <headerValue>)
                .build();
                
client.execute(testEntityRequest);
```

To use the deserialization , pass a __Class__ object of the type you will deserialize and pass an object of this type to the ```entity(T entity)``` method.

```java
TestEntity testEntity = new TestEntity();
Request<TestEntity> testEntityRequest = RequestBuilder
                .postRequest(TestEntity.class)
                .uri(<uri>)
                .entity(testEntity)
                .build();
                
client.execute(testEntityRequest);
```

To build a multipart request, add entity parts with the ```multipartEntity(String name, T entity)``` method and build the __Request__ with ```buildMultipart()``` instead of ```build()```. The created __Request__ object is of type __MultipartEntity__ - a generic model holding all entity parts and their names.

```java
TestEntity testEntityPartOne = new TestEntity();
TestEntity testEntityPartTwo = new TestEntity();
Request<MultipartEntity<TestEntity>> testEntityMultipartRequest = RequestBuilder
                .postRequest(TestEntity.class)
                .multipartEntity(<part-one-name>, testEntityPartOne)
                .multipartEntity(<part-two-name>, testEntityPartTwo)
                .uri(<uri>)
                .buildMultipart();
                        
client.execute(testEntityMultipartRequest);                        
```
If the __RequestBuilder__ is not suitable to your use case (e.g. lacks functionality), you can directly build an __HttpUriRequest__ and construct the __Request__ object with it using the constructor. Use [org.apache.http.client.methods.RequestBuilder documentation](http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/methods/RequestBuilder.html) for reference.

Note that this approach is not recommended as it leaves the opportunity that the developer does not pass the request entity (if any) to the __Request__ object and miss on the logging that it provides. The following example won't log the request object in case of any errors.

```java
HttpUriRequest httpUriRequest = RequestBuilder.get(<uri>).build();
Request<String> request = new Request<>(httpUriRequest);
client.execute(request);
```
__RestApiClient__ works with a __StatusCodeHandler__ and can accept a custom implementation. The library provides a default implementation (__DefaultStatusCodeHandler__), which is used if a handler is not explicitly provided. It is handling ```401``` response code by throwing an __UnauthorizedException__ and all other status codes larger than 300 by throwing the generic __ResponseException__. The exceptions provide an __HttpExchangeContext__ object that holds both the __Request__ and __Response__ objects of the request execution.

```java
client.execute(request, customStatusCodeHandler);
```

__RestApiClient__ works with a __org.apache.http.client.ResponseHandler__ to parse the response body and can accept a custom implementation. If no custom implementation is provided, it uses the __DefaultResponseHandler__, which returns the response body as a string. The library provides two utility response handlers that use the Jackson library to deserialize a response into whatever type is provided to them as a generic:
* __JacksonJsonResponseHandler__ deserializes JSON responses.
* __PropertiesResponseHandler__ deserializes properties responses in the format of `property=value`.

```java
client.execute(request, customResponseHandler);
```

You can also provide both a response and a status code handler.

```java
client.execute(request, customResponseHandler, customStatusCodeHandler);
```

__Note:__ The following exceptions are thrown by the interface methods:

*  __ConnectionException__ in case of an IOException.
* __ResponseException__ if an error HTTP status code is received.
* __UnauthorizedException__ in case of a 401 Unauthorized response code, if using the default status code handler.
* __RestApiClientException__ is the common exception for all of the above.

# Contribution

We welcome any exchange and collaboration with individuals and organizations interested in the use, support and extension of the library.

# License

Copyright © 2017-2020 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE file](LICENSES/Apache-2.0.txt).
