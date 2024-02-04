package com.example.challenge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class ReactiveWebClient {

    @Autowired
    private WebClient webClient;

    public <S, T> Mono<T> postMono(String url, S body, Class<T> clasz) {
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err -> {
                    log.error("ReactiveWebClient() :: Exception in post for url {}, Body {}, Exception - {}", url, body, err);
//                    throw new RuntimeException("");
                })
                .doOnSuccess(resp -> {
                    log.info("ReactiveWebClient() :: POST :: Received successful response {} from url {} for body {}", resp, url, body);
                });
    }

    public <S> Mono<S> getMono(String url, Class<S> clasz) {
        return webClient.get()
                .uri(URI.create(url))
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err -> {
                    log.error("ReactiveWebClient() :: Exception in get for url {}, Exception - {}", url, err);
                })
                .doOnSuccess(resp -> {
                    log.info("ReactiveWebClient() :: GET :: Received successful response {} from url {}", resp, url);
                });
    }

    public <S> Mono<S> deleteMono(String url, Class<S> clasz) {
        return webClient.delete()
                .uri(URI.create(url))
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err -> {
                    log.error("ReactiveWebClient() :: DELETE :: Exception in get for url {}, Exception - {}", url, err);
                })
                .doOnSuccess(resp -> {
                    log.info("ReactiveWebClient() :: DELETE :: Received successful response {} from url {}", resp, url);
                });
    }
}
