package ir.co.sadad.cartollapi.enumurations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonDeserialize(using = CurrencyDeserializer.class)
@Getter
@RequiredArgsConstructor
public enum Currency {

    RIAL(364);

    private final Integer code;

}
