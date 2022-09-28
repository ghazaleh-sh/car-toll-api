package ir.co.sadad.cartollapi.entities;

import ir.co.sadad.cartollapi.enumurations.InquiryType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_INQUIRY_RESULT")
@EntityListeners(AuditingEntityListener.class)
public class InquiryResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "INQUIRY_TYPE")
    private InquiryType inquiryType;

    @Column(name = "SERIAL_NO")
    private String serialNo;

    @Column(name = "VIOLATION_OCCUR_DATE")
    private String violationOccurDate;

    @Column(name = "VIOLATION_OCCUR_TIME")
    private String violationOccurTime;

    @Column(name = "VIOLATION_DELIVERY_TYPE")
    private String violationDeliveryType;

    @Column(name = "VIOLATION_ADDRESS")
    private String violationAddress;

    @Column(name = "VIOLATION_TYPE_ID")
    private Integer violationTypeId;

    @Column(name = "VIOLATION_TYPE")
    private String violationType;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "PAPER_ID")
    private String paperId;

    @Column(name = "PAYMENT_ID", length = 10)
    private String paymentId;

    @Column(name = "HAS_IMAGE")
    private Boolean hasImage;

    @Column(name = "PLATE_DICTATION")
    private String plateDictation;

    @Column(name = "PLATE_FA")
    private String plateFa;

    @Column(name = "COMPLAINT_STATUS")
    private String complaintStatus;

    @Column(name = "COMPLAINT")
    private String complaint;

    @Column(name = "TOTAL_PAPER_ID")
    private String totalPaperId;

    @Column(name = "TOTAL_PAYMENT_ID")
    private String totalPaymentId;

    @Column(name = "HAS_DEBIT")
    private Boolean hasDebit;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false, foreignKey = @ForeignKey(name = "FKINQUIRY_RESULT_TO_REQUEST"))
    Request request;

    @Column(name = "INQUIRY_DATE")
    private String inquiryDate;

    @Column(name = "INQUIRY_TIME")
    private String inquiryTime;

    @Column(name = "LAST_SYS_UPDATE_DATE")
    private String lastSysUpdateDate;

    @Column(name = "PAYMENT_STATUS", columnDefinition = "VARCHAR(30) DEFAULT 'نامشخص'")
    private String paymentStatus;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "PLATE_NO")
    private String plateNo;

}
