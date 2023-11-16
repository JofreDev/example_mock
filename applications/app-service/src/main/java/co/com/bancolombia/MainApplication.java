package co.com.bancolombia;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MainApplication  {


    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }



}
