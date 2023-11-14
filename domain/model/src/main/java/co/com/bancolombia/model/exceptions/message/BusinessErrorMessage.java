package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    INVALID_REQUEST("cod de error", "Invalid request"),
    INVALID_COMPONENT("cod de error", "Invalid component/action/microservice not legal");

    private final String code;
    private final String message;

}