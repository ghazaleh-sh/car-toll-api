package ir.co.sadad.cartollapi.enumurations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CurrencyDeserializer extends JsonDeserializer<Currency> {

    public Currency deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);

        return Currency.RIAL;
    }
}
