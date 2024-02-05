package com.rq.challenge.common;

import com.rq.challenge.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestConnector {

    private static final String MSG_FAILED_COMMUNICATION_WITH_API_PROVIDER = "Failed to communicate to API Provider";
    private final RestTemplate restTemplate = new RestTemplate();

    public <S, T> ResponseEntity<T> post(String url, S input, Class<T> clasz) {
        try {
            HttpEntity<S> requestBody = new HttpEntity<>(input, commonHeaders());
            return restTemplate.exchange(url, HttpMethod.POST, requestBody, clasz);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("API POST :: Exception with Request {} with exception  {} ", input, ex.getMessage(), ex);
            throw new RestClientException(ex.getStatusCode() + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("API POST :: Exception with Request {} with exception  {} ", input, ex.getMessage(), ex);
            throw new RestClientException(MSG_FAILED_COMMUNICATION_WITH_API_PROVIDER);
        }
    }

    public <T> ResponseEntity<T> get(String url, Class<T> clasz) {
        try {
            HttpEntity<String> requestBody = new HttpEntity<>(commonHeaders());
            return restTemplate.exchange(url, HttpMethod.GET, requestBody, clasz);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("API GET :: Exception with Request url {} with exception  {} ", url, ex.getMessage(), ex);
            throw new RestClientException(ex.getStatusCode() + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("API GET :: Exception with Request url {} with exception  {} ", url, ex.getMessage(), ex);
            throw new RestClientException(MSG_FAILED_COMMUNICATION_WITH_API_PROVIDER);
        }
    }

    public <T> ResponseEntity<T> delete(String url, Class<T> clasz) {
        try {
            HttpEntity<String> requestBody = new HttpEntity<>(commonHeaders());
            return restTemplate.exchange(url, HttpMethod.DELETE, requestBody, clasz);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("API DELETE :: Exception with Request url {} with exception  {} ", url, ex.getMessage(), ex);
            throw new RestClientException(ex.getStatusCode() + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("API DELETE :: Exception with Request url {} with exception  {} ", url, ex.getMessage(), ex);
            throw new RestClientException(MSG_FAILED_COMMUNICATION_WITH_API_PROVIDER);
        }
    }

    private HttpHeaders commonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}