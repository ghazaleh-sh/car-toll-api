package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ViolationListResponseDto {

    private List<ViolationDetailDto> violationDetails;


    /**
     * پالک به حروف
     */
    private String plateDictation;

    /**
     * پالک
     */
    private String plateFa;

    /**
     * وضعیت شکایت
     */
    private String complaintStatus;

    /**
     * شکایت
     */
    private String complaint;

    /**
     * جریمهها تا این تاریخ میباشد ( تاریخ بهروزرسانی سیستم )
     */
    private String lastSystemUpdateDate;

    /**
     * تاریخ استعالم
     */
    private String inquiryDate;

    /**
     * زمان استعالم
     */
    private String inquiryTime;

    /**
     * بدهی دارد = True
     * False = ندارد بدهی
     */
    private Boolean hasDebit;

    /**
     * شماره صفحه
     */
    private String pageCount;

    /**
     * شناسه قبض مجموع
     */
    private String paperId;

    /**
     * شناسه پرداخت مجموع
     */
    private String paymentId;
}
