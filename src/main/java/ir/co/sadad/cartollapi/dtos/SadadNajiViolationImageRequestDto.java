package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SadadNajiViolationImageRequestDto extends SadadNajiViolationRequestDto {
    private String SerialNo;
}
