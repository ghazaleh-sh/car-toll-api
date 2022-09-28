package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class OtpRequestDto {
    @NotNull(message = "{cellphone.must.not.be.null}")
    @Pattern(regexp = "^98[0-9]{10}$", message = "{phone.pattern.not.valid.98}")
    private String ownerCellphone;

    @NotNull(message = "{plate.no.must.not.be.null}")
    @Pattern(regexp = "^[0-9]{8,9}$", message = "{plate.no.pattern.not.valid}")
    private String plateNo;


}
