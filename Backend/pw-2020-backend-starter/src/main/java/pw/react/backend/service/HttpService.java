package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import pw.react.backend.web.Quote;

import javax.annotation.PostConstruct;

public class HttpService implements HttpClient {

    private String carlyUrl;
    private String flatlyUrl;
    private String parklyUrl;
    private String integrationUrl;

    private final Logger logger = LoggerFactory.getLogger(HttpService.class);

    private final SecurityProvider securityProvider;
    private final RestTemplate restTemplate;

    public HttpService(RestTemplate restTemplate, SecurityProvider securityProvider) {
        this.restTemplate = restTemplate;
        this.securityProvider = securityProvider;
    }

    HttpService withCarlyUrl(String carlyUrl) {
        this.carlyUrl = carlyUrl;
        return this;
    }

    HttpService withFlatlyUrl(String flatlyUrl) {
        this.flatlyUrl = flatlyUrl;
        return this;
    }

    HttpService withParklyUrl(String parklyUrl) {
        this.parklyUrl = parklyUrl;
        return this;
    }

    HttpService withIntegrationUrl(String integrationUrl) {
        this.integrationUrl = integrationUrl;
        return this;
    }

    @PostConstruct
    private void init() {
        logger.info("Injected CARLY URL: [{}]", carlyUrl);
        logger.info("Injected PARKLY URL: [{}]", parklyUrl);
        logger.info("Injected FLATLY URL: [{}]", flatlyUrl);
        logger.info("Injected INTEGRATION URL: [{}]", integrationUrl);
    }


    private <T> HttpEntity<T> getRequestEntity(T entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add(securityProvider.getSecurityHeader(), securityProvider.getSecurityHeaderValue());
        return new HttpEntity<>(entity, headers);
    }

    /*
    @Override
    public Quote consume(String url) {
        final Quote quote = restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        if (quote != null) {
            logger.info("This is Quote: {}", quote.toString());
        } else {
            logger.warn("Quote is null");
        }
        return quote;
    }*/
}
