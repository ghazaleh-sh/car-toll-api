package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PlateDeleteRequestDto {
    @Pattern(regexp = "^[0-9]{8,9}$", message = "{plate.no.pattern.not.valid}")
    private String plateNo;
}
