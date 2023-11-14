package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalErrorMessage {

    PLOTFLAT_NOT_FOUND_ERROR("SCT0010","Plot flat not found error");

    private final String code;
    private final String message;
}
