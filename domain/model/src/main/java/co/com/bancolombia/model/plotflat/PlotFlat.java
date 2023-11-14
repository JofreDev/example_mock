package co.com.bancolombia.model.plotflat;
import co.com.bancolombia.model.outputflat.OutputFlat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@ToString
//@Builder(toBuilder = true)
@NoArgsConstructor
public class PlotFlat  {

    private String key;

    private String tramaIn;

    private String tramaOut;

    private int delay;



}
