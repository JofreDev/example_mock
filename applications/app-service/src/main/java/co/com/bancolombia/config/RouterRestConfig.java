package co.com.bancolombia.config;

import co.com.bancolombia.model.components.Component;
import co.com.bancolombia.model.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.INVALID_COMPONENT;
import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.INVALID_CONFIGURATION_COMPONENT;

@Configuration
public class RouterRestConfig {

    @Value("${rest.config.path}")
    private String auxRestPath;



    @Bean
    public Mono<String> validateRestPath() {
        return Mono.just(auxRestPath)
                .filter(value -> isValidComponent(value))
                .switchIfEmpty(Mono.error(() -> new BusinessException(INVALID_CONFIGURATION_COMPONENT)));
    }

    private boolean isValidComponent(String value) {
        try {
            Component.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
