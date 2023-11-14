package co.com.bancolombia.model.dynamicfield;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true)
@ToString
public class DynamicField {

    private String nombre;

    private String descripcion;

    private int posInicial;

    private int longitud;


}
