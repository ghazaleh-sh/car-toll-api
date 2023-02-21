package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

@Data
public class ViolationImageResponseDto {
    private String serialNo;
    private byte[] plateImage;
    private byte[] vehicleImage;
}
