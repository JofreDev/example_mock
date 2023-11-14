package co.com.bancolombia.model.dynamicfield.gateways;

import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.plotflat.PlotFlat;
import reactor.core.publisher.Flux;

public interface DynamicFieldRepository {

    /// Contrato para ir a leer una información(No me importa su origen) !!!!!!
    public Flux<DynamicField> createDynamicFieldsTs();

    public Flux<DynamicField> createDynamicFieldsTr();
}
