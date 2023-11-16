package co.com.bancolombia.api;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.usecase.compareflats.CompareFlatsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.util.EnumUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.INVALID_COMPONENT;
import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.INVALID_REQUEST;

@Component
@RequiredArgsConstructor
public class Handler {
    private final CompareFlatsUseCase compareFlatsUseCase;

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {



        return serverRequest.bodyToMono(FlatPlotR.class)
                .flatMap(request ->
                        Mono.just(request)
                                .filter( req ->  req.getMensaje() != null &&  req.getComponente() != null)
                                .switchIfEmpty(Mono.error(() -> new BusinessException(INVALID_REQUEST)))
                                .filter( req ->  Arrays.stream(co.com.bancolombia.model.components.Component.values())
                                        .anyMatch( c -> c.toString().equals(req.getComponente())))
                                .switchIfEmpty(Mono.error(() -> new BusinessException(INVALID_COMPONENT)))
                )
                .flatMap(request -> compareFlatsUseCase.findFlat(request.getMensaje()))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));

    }
}
