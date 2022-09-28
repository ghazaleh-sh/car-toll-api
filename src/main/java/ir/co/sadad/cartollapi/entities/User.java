package ir.co.sadad.cartollapi.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_USER")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "NATIONAL_CODE",columnDefinition = "CHAR(10)", length = 10, nullable = false)
    private String nationalCode;

    @Column(name = "CELL_PHONE")
    private String cellPhone;

    @Column(name = "DEBIT_COUNT")
    private Integer debitCount;

//    @Column(name = "AUTHORIZATION_EXPIRE_TIME")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date authorizationExpireTime;

}
