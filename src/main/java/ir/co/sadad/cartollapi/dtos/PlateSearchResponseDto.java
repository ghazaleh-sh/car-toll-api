package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PlateSearchResponseDto {

    private String userNationalCode;
    private Integer userDebitCount;
    private List<PlateInfoDto> plateInfo;
}
