package ir.co.sadad.cartollapi.entities;

import ir.co.sadad.cartollapi.enumurations.PlateType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_PLATE")
@EntityListeners(AuditingEntityListener.class)
public class Plate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLATE_ID")
    private Long plateId;

    @Column(name = "PLATE_NO", nullable = false)
    @NotNull
    private String plateNo;

    @Column(name = "PLATE_OWNER_NATIONAL_CODE",columnDefinition = "CHAR(10)", length = 10, nullable = false)
    @NotNull
    private String plateOwnerNationalCode;

    @Column(name = "PLATE_OWNER_CELL_PHONE", length = 14)
    @Transient
    private String plateOwnerCellPhone;

    @Column(name = "Name", columnDefinition = "VARCHAR(40 CODEUNITS32)", nullable = false)
    @Length(min = 3, max = 40)
    private String name;

    @Column(name = "VIN", length = 30)
    private String vin;

    @Column(name = "MOTOR_CYCLE_NUMBER", length = 50)
    private String motorCycleNumber;

    @Column(name = "BARCODE", length = 50)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE", nullable = false, length = 30)
    private PlateType type;

}
