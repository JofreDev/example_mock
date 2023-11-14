package co.com.bancolombia.usecase.compareflats;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.outputflat.gateways.OutputFlatRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CompareFlatsUseCase{

    private final OutputFlatRepository outputFlatRepository;

    public Mono<FlatPlotR> compare(String tramaPlana){

        return  outputFlatRepository.findFlat(tramaPlana);
    }
}
