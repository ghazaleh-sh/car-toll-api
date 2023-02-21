package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import java.util.List;

/**
 * detail of sadad naji
 */
@Data
public class SadadNajiErrorDetailDto {
    private String code;
    private String target;
    private String message;
    private List<SadadNajiErrorDetailDto> details;
}
