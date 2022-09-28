package ir.co.sadad.cartollapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SimorqPayFreewayTollDto {

    @Data
    @Accessors(chain = true)
    public static class TollRequest {

        @JsonProperty("EnquiryId")
        private String enquiryId;
        @JsonProperty("PlateNo")
        private Integer plateNo;
        @JsonProperty("FreewayAmount")
        private Integer freewayAmount;
        @JsonProperty("BillId")
        private String billId;

    }

    @Data
    @Accessors(chain = true)
    public static class TollsRequest {

        @JsonProperty("EnquiryId")
        private String enquiryId;
        @JsonProperty("PlateNo")
        private Integer plateNo;
        @JsonProperty("FreewayAmount")
        private Integer freewayAmount;
        @JsonProperty("BillIds")
        private List<String> billIds;

    }

    @Data
    @Accessors(chain = true)
    public static class Response {
        private String actionCode;
        private String actionMessage;
        private List<String> errorMessages;
        private String referenceNumber;
        private String traceNumber;
        private PayFreewayDetail data;

        @Getter
        @Setter
        public class PayFreewayDetail {

            @JsonProperty("PlateNo")
            private String plateNo;
            @JsonProperty("Success")
            private Boolean success;
            @JsonProperty("TotalPaidAmount")
            private BigDecimal totalPaidAmount;
            @JsonProperty("RRN")
            private String rrn;
            @JsonProperty("PaidTollNumber")
            private Integer paidTollNumber;
        }
    }


}
