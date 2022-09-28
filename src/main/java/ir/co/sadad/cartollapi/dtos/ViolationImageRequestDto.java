package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ViolationImageRequestDto {
    /**
     * phone no pf owner
     */
    @NotNull(message = "{cellphone.must.not.be.null}")
    @Pattern(regexp = "^09[0-9]{9}$", message = "{phone.pattern.not.valid}")
    private String phoneNo;

    @NotNull(message = "{serial.no.require}")
    private String serialNo;

    @NotNull(message = "{plate.tag.must.not.be.null}")
    private String plateNo;

}
