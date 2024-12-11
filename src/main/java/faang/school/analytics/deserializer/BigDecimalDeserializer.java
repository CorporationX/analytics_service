package faang.school.analytics.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    private static final int EXPECTED_ARRAY_SIZE = 2;
    private static final int INDEX_OF_VALUE = 1;

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)  {
        JsonNode node;
        try {
            node = p.getCodec().readTree(p);
            log.info("Deserializing message: {}", p.getText());
        } catch (IOException e) {
            log.error("Failed to map message to event type", e);
            throw new RuntimeException(e);
        }
        if (node.isArray() && node.size() == EXPECTED_ARRAY_SIZE) {
            log.info("Deserialized value: {}", node.get(INDEX_OF_VALUE).asText());
            return new BigDecimal(node.get(INDEX_OF_VALUE).asText());
        }
        return new BigDecimal(node.asText());
    }
}
