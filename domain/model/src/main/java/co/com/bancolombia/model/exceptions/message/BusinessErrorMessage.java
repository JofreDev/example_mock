package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    INVALID_REQUEST("cod de error", "Invalid request"),
    INVALID_COMPONENT("cod de error", "Invalid component/action/microservice not legal"),

    INVALID_CONFIGURATION_COMPONENT("cod de error", "Trying to create an invalid component");


    private final String code;
    private final String message;

}