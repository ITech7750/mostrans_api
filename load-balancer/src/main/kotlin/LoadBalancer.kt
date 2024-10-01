package ru.itechn

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@SpringBootApplication
class LoadBalancer

fun main(args: Array<String>) {
    val ctx =
        SpringApplicationBuilder(LoadBalancer::class.java)
            .web(WebApplicationType.NONE)
            .run(*args)

    val loadBalancedClient = ctx.getBean(WebClient.Builder::class.java).build()

    val requestBody =
        mapOf(
            "login" to "string",
            "password" to "string",
        )

    for (i in 1..10) {
        val response: Mono<String> =
            loadBalancedClient
                .post()
                .uri("http://auth-service/api/auth/login")
                .header("accept", "*/*")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String::class.java)

        // Печатаем ответ
        response.subscribe { println(it) }
    }
}
