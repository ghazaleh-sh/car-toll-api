package ir.co.sadad.cartollapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Data
public class NajiPaymentDto {


    /**
     * obj request for payment service
     */
    @Data
    @Accessors(chain = true)
    public static class PaymentTanRequest {
        private String id;
    }

    @Data
    @Accessors(chain = true)
    public static class ReverseRequest {
        private String moneyTransferRequestId;
        private String paymentReference;
        private String description;
    }


    @Data
    @Accessors(chain = true)
    public static class ReverseResponse {
        private String responseMessageCode;
        private String responseMessage;
        private String traceNumber;
        private String status;
        private String serviceCallStatus;
        private String requestPayload;
        private String responsePayload;
        private String exception;
        private String reverseRequestId;
        private String dateTime;
        private String notifications;

    }


    @Data
    @Accessors(chain = true)
    public static class Response {

        private ResultSet resultSet;
        private MetaData metaData;


        @Getter
        @Setter
        public static class ResultSet {

            private InnerResponse innerResponse;

            @Getter
            @Setter
            public static class InnerResponse {
                @JsonProperty("id")
                private String id;
                @JsonProperty("status")
                private String status;
                @JsonProperty("traceNo")
                private String traceNo;
                @JsonProperty("responseCode")
                private String responseCode;
                @JsonProperty("responseMessage")
                private String responseMessage;
                @JsonProperty("updateDateTime")
                private Long updateDateTime;
            }
        }

        @Getter
        @Setter
        public static class MetaData {

            private List<Notifications> notifications;

            @Getter
            @Setter
            public static class Notifications {
                @JsonProperty("code")
                private String code;
                @JsonProperty("type")
                private String type;
                @JsonProperty("message")
                private String message;
            }
        }
    }
}
