package co.com.bancolombia.config;

import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.dynamicfield.gateways.DynamicFieldRepository;
import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.model.plotflat.gateways.PlotFlatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;

@Configuration
//@AllArgsConstructor
public class MocksStiConfig {


    @Bean
    public List<DynamicField> createListOfDynamicsTr(DynamicFieldRepository dynamicFieldRepository){

        return dynamicFieldRepository.createDynamicFieldsTr().collectList().block();
    }


    @Bean
    public HashMap<String, OutputFlat> createMap(PlotFlatRepository plotFlatRepository,
                                                 DynamicFieldRepository dynamicFieldRepository){

        return (HashMap<String, OutputFlat>) plotFlatRepository.createPlotFlats().collectMap(
                        plotFlat -> {

                            StringBuilder tramaOutAux = new StringBuilder(plotFlat.getTramaIn());
                            dynamicFieldRepository.createDynamicFieldsTs().collectList().block().stream()
                                    .forEach(dynamicField -> {
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








}
