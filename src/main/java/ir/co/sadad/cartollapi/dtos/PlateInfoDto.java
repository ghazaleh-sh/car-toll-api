package ir.co.sadad.cartollapi.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlateInfoDto {
    private String plateOwnerNationalCode;
    private String plateOwnerCellPhone;
    private String PlateNo;
    private String name;
    private String type;
    private String vin;
    private String barcode;
    private String motorCycleNumber;


}
