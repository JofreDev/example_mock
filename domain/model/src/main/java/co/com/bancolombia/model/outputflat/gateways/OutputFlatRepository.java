package co.com.bancolombia.model.outputflat.gateways;

import co.com.bancolombia.model.In.FlatPlotR;
import co.com.bancolombia.model.outputflat.OutputFlat;
import reactor.core.publisher.Mono;

public interface OutputFlatRepository {

    public Mono<FlatPlotR> findFlat(String trama);




}
