package ir.co.sadad.cartollapi.repositories;

import ir.co.sadad.cartollapi.entities.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlateRepository extends JpaRepository<Plate, Long>, JpaSpecificationExecutor<Plate> {

}
