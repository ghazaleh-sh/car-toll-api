package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

@Data
public class SadadNajiViolationRequestDto {
    /**
     * phone no of owner - for sending request to naji service
     */
    private String phoneNumber;

    /**
     * nationalCode of owner
     * calculated by service - for sending request to naji service
     */
    private String nationalCode;

    private String plateNo;

    private boolean checkShahkar;
}
