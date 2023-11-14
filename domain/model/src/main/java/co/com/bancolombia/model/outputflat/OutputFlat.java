package co.com.bancolombia.model.outputflat;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class OutputFlat {

    private String tramaOut;

    private int delay;
}
