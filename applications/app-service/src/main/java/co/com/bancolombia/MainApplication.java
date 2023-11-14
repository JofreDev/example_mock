package co.com.bancolombia;

import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.models.ComponentMap;
import co.com.bancolombia.usecase.createcomponentmap.CreateComponentMapUseCase;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RequiredArgsConstructor
public class MainApplication implements CommandLineRunner {

    ///private final CreateComponentMapUseCase createComponentMapUseCase;


    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //ComponentMap.MapPCCROTIDOC



    }


}
