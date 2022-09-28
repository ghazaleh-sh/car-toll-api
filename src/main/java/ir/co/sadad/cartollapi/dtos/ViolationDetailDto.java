package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

@Data
public class ViolationDetailDto {
    /**
     * شماره سریال قبض
     */
    private String serialNo;


    /**
     * تاریخ وقوع جرم
     */
    private String violationOccureDate;

    /**
     * زمان وقوع جرم
     */
    private String violationOccureTime;


    /**
     * نوع ثبت جریمه )الصاقی، دوربین،تسلیمی، دوبرگی (
     */
    private String violationDeliveryType;

    /**
     * محل وقوع جرم
     */
    private String violatoinAddress;


    /**
     * کد نوع جریمه
     */
    private Integer violationTypeId;


    /**
     * نوع جریمه
     */
    private String violationType;


    /**
     * مبلغ جریمه
     */
    private String amount;

    /**
     * شناسه قبض
     */
    private String paperId;


    /**
     * شناسه پرداخت
     */
    private String paymentId;


    /**
     * عکس دارد = True
     * عکس ندارد = False
     */
    private Boolean hasImage;
}
