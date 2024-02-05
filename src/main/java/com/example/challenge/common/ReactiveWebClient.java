package com.example.challenge.common;

import com.example.challenge.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;

@Component
@Slf4j
public class ReactiveWebClient {

    private WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies
                    .builder()
                    .codecs(config -> config.defaultCodecs().maxInMemorySize(-1))
                    .build()
            )
            .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()))
            .build();

    public <S, T> Mono<T> postMono(String url, S body, Class<T> clasz) {
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err -> log.error("ReactiveWebClient() :: Exception in post for url {}, Body {}, Exception - {}", url, body, err))
                .doOnSuccess(resp -> log.info("ReactiveWebClient() :: POST :: Received successful response {} from url {} for body {}", resp, url, body));
    }

    public <S> Mono<S> getMono(String url, Class<S> clasz) {
        return webClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(httpStatus -> httpStatus != HttpStatus.OK, error -> Mono.error(new RestClientException("error Body")))
                .bodyToMono(clasz)
                .doOnError(err -> log.error("ReactiveWebClient() :: Exception in get for url {}, Exception - {}", url, err))
                .doOnSuccess(resp -> log.info("ReactiveWebClient() :: GET :: Received successful response {} from url {}", resp, url));
    }

    public <S> Mono<S> deleteMono(String url, Class<S> clasz) {
        return webClient.delete()
                .uri(URI.create(url))
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err -> log.error("ReactiveWebClient() :: DELETE :: Exception in get for url {}, Exception - {}", url, err))
                .doOnSuccess(resp -> log.info("ReactiveWebClient() :: DELETE :: Received successful response {} from url {}", resp, url));
    }
}
