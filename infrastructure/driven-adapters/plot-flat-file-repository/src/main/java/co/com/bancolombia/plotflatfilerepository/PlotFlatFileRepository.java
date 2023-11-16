package co.com.bancolombia.plotflatfilerepository;

import co.com.bancolombia.model.dynamicfield.DynamicField;
import co.com.bancolombia.model.dynamicfield.gateways.DynamicFieldRepository;
import co.com.bancolombia.model.plotflat.PlotFlat;
import co.com.bancolombia.model.plotflat.gateways.PlotFlatRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PlotFlatFileRepository implements PlotFlatRepository, DynamicFieldRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${configuration.path}")
    private Path path;


    private Flux<DataBuffer> data;


    public PlotFlatFileRepository(DefaultDataBufferFactory bufferFactory) {
        initializeDataBuffer(bufferFactory);
    }

    private void initializeDataBuffer(DefaultDataBufferFactory bufferFactory) {
        this.data = DataBufferUtils.read(path, bufferFactory, 4096);
    }


    @Override
        public Flux<PlotFlat> createPlotFlats() {

            if (data == null) {
                initializeDataBuffer(new DefaultDataBufferFactory());
            }

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
