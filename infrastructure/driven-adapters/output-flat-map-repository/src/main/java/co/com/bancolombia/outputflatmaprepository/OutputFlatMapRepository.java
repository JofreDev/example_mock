package co.com.bancolombia.outputflatmaprepository;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.dynamicfield.gateways.DynamicFieldRepository;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.model.outputflat.gateways.OutputFlatRepository;
import co.com.bancolombia.model.plotflat.PlotFlat;
import co.com.bancolombia.model.plotflat.gateways.PlotFlatRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.PLOTFLAT_NOT_FOUND_ERROR;

@Getter
@Component
@AllArgsConstructor
public class OutputFlatMapRepository implements InitializingBean , OutputFlatRepository {

    private final PlotFlatRepository plotFlatRepository;
    private final DynamicFieldRepository dynamicFieldRepository;
    private static HashMap<String, OutputFlat> mapPCCROTIDOC;
    private static List<DynamicField> listDynamicTsPCCROTIDOC;
    private static List<DynamicField>  listDynamicTrPCCROTIDOC;



    private HashMap<String, OutputFlat> createMap(){

        Flux<PlotFlat> componentData =  plotFlatRepository.createPlotFlats();

        return (HashMap<String, OutputFlat>) componentData.collectMap(
                        plotFlat -> {

                            StringBuilder tramaOutAux = new StringBuilder(plotFlat.getTramaIn());
                            listDynamicTsPCCROTIDOC.stream().forEach(dynamicField -> {
                                tramaOutAux.replace(
                                        dynamicField.getPosInicial() - 1,
                                        dynamicField.getPosInicial() + dynamicField.getLongitud() - 1,
                                        "\\w{" + dynamicField.getLongitud() + "}");
                            });

                            return tramaOutAux.toString();
                        } ,
                        plotFlat-> new OutputFlat(plotFlat.getTramaOut(),plotFlat.getDelay()).toBuilder().build()
                )
                .block();
    }

    private List<DynamicField> createListOfDynamics(Flux<DynamicField> dynamics){

        return dynamics.collectList().block();
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        listDynamicTsPCCROTIDOC = createListOfDynamics(dynamicFieldRepository.createDynamicFieldsTs());
        listDynamicTrPCCROTIDOC = createListOfDynamics(dynamicFieldRepository.createDynamicFieldsTr());
        mapPCCROTIDOC = createMap();

    }

    @Override
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
