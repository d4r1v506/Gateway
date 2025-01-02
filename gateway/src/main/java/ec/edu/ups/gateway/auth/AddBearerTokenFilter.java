package ec.edu.ups.gateway.auth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class AddBearerTokenFilter extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token = obtenerTokenDesdeMicroservicio(); // Llama al método para obtener el token
            exchange.getRequest()
                .mutate()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
            return chain.filter(exchange);
        };
    }

    private String obtenerTokenDesdeMicroservicio() {
        // Implementa aquí el método explicado en el Paso 1
        try {
            WebClient webClient = WebClient.builder().build();
            
            String body = "{\"username\":\"admin\",\"password\":\"admin123\"}";
            
            String tokenResponse = webClient.post()
                .uri("http://172.17.0.4:8081/gestor/api/auth/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(tokenResponse).get("token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el token desde el microservicio: " + e.getMessage());
        }
    }
}
