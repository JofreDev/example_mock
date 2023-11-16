package co.com.bancolombia.usecase.compareflats;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.model.outputflat.gateways.OutputFlatRepository;
import co.com.bancolombia.model.plotflat.gateways.PlotFlatRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.PLOTFLAT_NOT_FOUND_ERROR;

@RequiredArgsConstructor
public class CompareFlatsUseCase{

    private final  HashMap<String, OutputFlat> mapPCCROTIDOC ;

    private final List<DynamicField>  listDynamicTrPCCROTIDOC;



    public Mono<FlatPlotR> findFlat(String trama) {

        return mapPCCROTIDOC.keySet().stream()
                .filter(key -> {
                    Pattern pattern = Pattern.compile(key);
                    return pattern.matcher(trama.trim()).matches();
                })
                .findFirst()
                .map(mapPCCROTIDOC::get)
                .map(entry -> FlatPlotR.builder()
                        .mensaje( assembleOutPutFlat(entry.getTramaOut(), trama) )
                        .componente(String.valueOf(co.com.bancolombia.model.components.Component.PCCROTIDOC))
                        .build())
                .map(Mono::just)
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
