package ir.co.sadad.cartollapi.repositories;

import ir.co.sadad.cartollapi.entities.UserPlate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserPlateRepository extends JpaRepository<UserPlate, Long>, JpaSpecificationExecutor<UserPlate> {

    Optional<UserPlate> findByUser_NationalCodeAndPlate_PlateNo(String nationalCode, String plateNo);

    List<UserPlate> findByUser_NationalCode(String nationalCode);
}
