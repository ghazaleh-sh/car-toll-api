package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

/**
 * dto for naji services
 */
@Data
public class SadadNajiRequestDto {
    /**
     * request obj for sadad naji services
     * <pre>
     *     request field must be string
     * </pre>
     */
    private String Request;
    private String Sign;
}
