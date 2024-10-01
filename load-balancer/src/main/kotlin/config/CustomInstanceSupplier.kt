import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import kotlin.random.Random

class CustomInstanceSupplier(
    private val serviceId: String,
) : ServiceInstanceListSupplier {
    private val instances =
        listOf(
            DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8001, false),
            DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 8002, false),
        )

    override fun getServiceId(): String = serviceId

    override fun get(): Flux<List<ServiceInstance>> = Flux.just(instances)

    fun getRandomInstance(): ServiceInstance = instances[Random.nextInt(instances.size)]
}

@Configuration
@LoadBalancerClient(name = "auth-service", configuration = [DemoServerInstanceConfiguration::class])
class WebClientConfig {
    @Bean
    @LoadBalanced
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()
}

@Configuration
class DemoServerInstanceConfiguration {
    @Bean
    fun serviceInstanceListSupplier(): ServiceInstanceListSupplier = CustomInstanceSupplier("auth-service")
}
