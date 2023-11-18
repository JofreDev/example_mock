package co.com.bancolombia.usecase.compareflats;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.outputflat.OutputFlat;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.PLOTFLAT_NOT_FOUND_ERROR;

@RequiredArgsConstructor
public class CompareFlatsUseCase{

    private final  HashMap<String, OutputFlat> mapPCCROTIDOC;

    private final List<DynamicField>  listDynamicTrPCCROTIDOC;



    public Mono<FlatPlotR> findFlat(String trama, String componente) {

        return mapPCCROTIDOC.keySet().stream()
                .filter(key -> {
                    Pattern pattern = Pattern.compile(key);
                    return pattern.matcher(trama.trim()).matches();
                })
                .findFirst()
                .map(mapPCCROTIDOC::get)
                .map(entry -> {
                    return Mono.just(entry)
                            .map(delayedEntry -> FlatPlotR.builder()
                                    .mensaje(assembleOutPutFlat(delayedEntry.getTramaOut(), trama))
                                    .componente(String.valueOf(co.com.bancolombia.model.components.Component.valueOf(componente)))
                                    .build())
                            .delayElement(Duration.ofMillis(entry.getDelay()));
                })
                .orElse(Mono.error(() -> new TechnicalException(PLOTFLAT_NOT_FOUND_ERROR)));
    }

    private String assembleOutPutFlat(String tramaOutOriginal, String tramaIn) {

        StringBuilder tramaOutAux = new StringBuilder(tramaOutOriginal);

        listDynamicTrPCCROTIDOC.forEach(dynamicField -> {
            int inicio = dynamicField.getPosInicial() - 1;
            int fin = dynamicField.getPosInicial() + dynamicField.getLongitud() - 1;

            tramaOutAux.replace(inicio, fin, tramaIn.substring(inicio, fin));
        });

        return tramaOutAux.toString();
    }


}
