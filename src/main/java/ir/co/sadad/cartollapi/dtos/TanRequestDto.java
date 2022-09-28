package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TanRequestDto {
    private Long requestId;
    @NotBlank(message = "{wage.id.must.not.be.null}")
    private String wageId;
    @NotBlank(message = "{tan.must.not.be.null}")
    private String tanCode;
}
