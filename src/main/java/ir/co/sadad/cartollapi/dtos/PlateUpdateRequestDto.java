package ir.co.sadad.cartollapi.dtos;

import ir.co.sadad.cartollapi.validations.NationalCode;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PlateUpdateRequestDto {
    @Pattern(regexp = "^[0-9]{8,9}$", message = "{plate.no.pattern.not.valid}")
    private String plateNo;
    @Size(min = 3, max = 40, message = "{plate.name.size.not.valid}")
    private String name;
    private String vin;
    private String barcode;
    private String motorCycleNumber;
    @NationalCode
    private String plateOwnerNationalCode;
}
