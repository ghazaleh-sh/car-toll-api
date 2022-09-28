package ir.co.sadad.cartollapi.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_PAYMENT")
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "AMOUNT", length = 20)
    private String amount;

    @Column(name = "PAPER_ID")
    private String paperId;

    @Column(name = "ACCOUNT_NO", length = 30, nullable = false)
    private String accountNo;

    @Column(name = "TRACE_NO")
    private String traceNo;

    @Column(name = "REFERENCE_ID")
    private String referenceId;

}
