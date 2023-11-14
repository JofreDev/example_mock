package co.com.bancolombia.plotflatfilerepository;

import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.dynamicfield.gateways.DynamicFieldRepository;
import co.com.bancolombia.model.outputflat.OutputFlat;
import co.com.bancolombia.model.plotflat.PlotFlat;
import co.com.bancolombia.model.plotflat.gateways.PlotFlatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class PlotFlatFileRepository implements PlotFlatRepository, DynamicFieldRepository {

        private final ObjectMapper objectMapper = new ObjectMapper();
        private final Flux<DataBuffer> data = DataBufferUtils.read(Path.of("C:\\Users\\eduardo.oliveros_pra\\Desktop\\TestE2E - Orquestador STI Nube\\applications\\app-service\\src\\main\\resources\\PCCROTIDOCJson.json"), new DefaultDataBufferFactory(), 4096);


        @Override
        public Flux<PlotFlat> createPlotFlats() {

            return data.map(buffer -> {
                        byte[] bytes = new byte[buffer.readableByteCount()];
                        buffer.read(bytes);
                        DataBufferUtils.release(buffer);
                        return new String(bytes, StandardCharsets.UTF_8);
                    })
                    .map(jsonString -> {
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = objectMapper.readTree(jsonString);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        JsonNode casesNode = jsonNode.get("cases");

                        return Optional.ofNullable(casesNode)
                                .filter(JsonNode::isArray)
                                .map(cases -> Flux.fromIterable(cases)
                                        .map(caseNode -> objectMapper.convertValue(caseNode, PlotFlat.class)))
                                .orElse(Flux.empty());
                    })
                    .flatMap(Function.identity());

        }

        @Override
        public Flux<DynamicField> createDynamicFieldsTs() {
            return createDynamicFields("tsDynamicFields");
        }

        @Override
        public Flux<DynamicField> createDynamicFieldsTr() {
            return createDynamicFields("trDynamicFields");
        }


    private Flux<DynamicField> createDynamicFields(String nameField) {
        return data.map(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .map(jsonString -> {
                    JsonNode jsonNode = null;
                    try {
                        jsonNode = objectMapper.readTree(jsonString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JsonNode casesNode = jsonNode.get(nameField);

                    return Optional.ofNullable(casesNode)
                            .filter(JsonNode::isArray)
                            .map(cases -> Flux.fromIterable(cases)
                                    .map(caseNode -> objectMapper.convertValue(caseNode, DynamicField.class)))
                            .orElse(Flux.empty());
                })
                .flatMap(Function.identity());
    }
}
