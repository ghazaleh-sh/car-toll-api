package ir.co.sadad.cartollapi.entities;

import ir.co.sadad.cartollapi.enumurations.Origination;
import ir.co.sadad.cartollapi.enumurations.RequestStatus;
import ir.co.sadad.cartollapi.enumurations.RequestType;
import ir.co.sadad.cartollapi.enumurations.ServiceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_REQUEST")
@EntityListeners(AuditingEntityListener.class)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Long requestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "REQUEST_TYPE", nullable = false)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "SERVICE_TYPE")
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "REQUEST_STATUS", nullable = false)
    private RequestStatus requestStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORIGINATION")
    private Origination origination;

    @ManyToOne
    @JoinColumn(name = "USER_PLATE_ID", nullable = false, foreignKey = @ForeignKey(name = "FKREQUEST_TO_USER_PLATE"))
    UserPlate userPlate;

    @CreatedDate
    @Column(name = "CREATION_DATE_TIME", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "PAYMENT_ID", foreignKey = @ForeignKey(name = "FKREQUEST_TO_PAYMENT"))
    private Payment payment;
}
