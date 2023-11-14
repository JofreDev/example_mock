package co.com.bancolombia.model.In;


import co.com.bancolombia.model.components.Component;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@ToString
public class FlatPlotR {


    private String componente;

    private String mensaje;


}
