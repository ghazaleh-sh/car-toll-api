package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViolationInquiryResponseDto {

    private List<InquiryDetailDto> inquiryList;
}
