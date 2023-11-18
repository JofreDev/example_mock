package co.com.bancolombia.api;

import co.com.bancolombia.model.components.Component;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Value("${rest.config.path}")
    private String restPath;



    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/mock/findplotflat/".concat(String.valueOf(Component.valueOf(restPath)))), handler::listenPOSTUseCase);
    }
}
