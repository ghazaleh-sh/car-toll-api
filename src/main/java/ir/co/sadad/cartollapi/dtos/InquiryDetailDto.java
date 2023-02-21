package ir.co.sadad.cartollapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryDetailDto {
    private String plateFa;
    private String complaintStatus;
    private String complaint;
    private Boolean hasDebit;
    private String pageCount;
    private String paperId;
    private String paymentId;
    private String serialNo;
    private String violationOccurDate;
    private String violationOccurTime;
    private String violationDeliveryType;
    private String violationAddress;
    private Integer violationTypeId;
    private String amount;
    private Boolean hasImage;
    private String plateDictation;
    private String violationType;
    private String inquiryType;
    private String paymentStatus;
    private String plateNo;
    private String inquiryDate;
    private String inquiryTime;
}
