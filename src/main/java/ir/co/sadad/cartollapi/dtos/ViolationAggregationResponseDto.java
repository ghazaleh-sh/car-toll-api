package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

@Data
public class ViolationAggregationResponseDto {
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

    /**
     * مجموع مبلغ جریمه
     */
    private String totalAmount;
}
