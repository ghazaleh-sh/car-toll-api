package ir.co.sadad.cartollapi.dtos;

import ir.co.sadad.cartollapi.validations.NationalCode;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PlateCreateRequestDto {

    /**
     * cell phone of user that own the application
     */
    @Pattern(regexp = "^09[0-9]{9}$",  message = "{phone.pattern.not.valid}")
    private String userCellPhone;

    @NotNull(message = "{plate.type.must.not.be.null}")
    @Pattern(regexp = "^(CAR|MOTORCYCLE)$", message = "{plate.type.not.valid}")
    private String type;

    @Pattern(regexp = "^[0-9]{8,9}$", message = "{plate.no.pattern.not.valid}")
    @NotNull(message = "{plate.no.must.not.be.null}")
    private String plateNo;

    @NationalCode
    private String plateOwnerNationalCode;

    @Pattern(regexp = "^09[0-9]{9}$",  message = "{phone.pattern.not.valid}")
    private String plateOwnerCellPhone;

    @NotNull(message = "{plate.name.must.not.be.null}")
    @Size(min = 3, max = 40, message = "{plate.name.size.not.valid}")
//            @Pattern(regexp = "^[\\u0600-\\u06FFa-zA-Z\\s]+$",  message = "{plate.name.not.valid}")
    private String name;

    @Size(max = 17, message = "{vin.max.size}")
    private String vin;

    private String motorCycleNumber;

    private String barcode;

}
