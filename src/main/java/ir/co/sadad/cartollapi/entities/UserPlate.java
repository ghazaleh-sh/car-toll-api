package ir.co.sadad.cartollapi.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CRTLL_USER_PLATE", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "PLATE_ID"}, name = "UKUSERPLATE_USER_PLATE")})
@EntityListeners(AuditingEntityListener.class)
public class UserPlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PLATE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FKUSER_PLATE_TO_USER"))
    @NotNull
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "PLATE_ID", nullable = false, foreignKey = @ForeignKey(name = "FKUSER_PLATE_TO_PLATE"))
    @NotNull
    private Plate plate;

    @CreatedDate
    @Column(name = "CREATION_DATE_TIME", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

}
