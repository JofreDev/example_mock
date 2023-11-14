package co.com.bancolombia.model.plotflat.gateways;

import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.model.plotflat.PlotFlat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PlotFlatRepository {

    /// Contrato para ir a leer una información(No me importa su origen) !!!!!!
    public Flux<PlotFlat> createPlotFlats();



}
