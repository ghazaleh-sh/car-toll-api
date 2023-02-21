package ir.co.sadad.cartollapi.dtos;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * respone of otp service
 */
@Data
@Accessors(chain = true)
public class SSOResponseDto {
    private String error;
    private String error_description;

}
