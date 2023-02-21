package ir.co.sadad.cartollapi.repositories;

import ir.co.sadad.cartollapi.entities.Payment;
import ir.co.sadad.cartollapi.entities.Request;
import ir.co.sadad.cartollapi.entities.User;
import ir.co.sadad.cartollapi.entities.UserPlate;
import ir.co.sadad.cartollapi.enumurations.RequestStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

    List<Request> findByUserPlateAndRequestStatus(UserPlate userPlate, RequestStatus status);

    List<Request> findByUserPlate(UserPlate userPlate);

    Optional<Request> findByRequestIdAndUserPlate(Long id, UserPlate userPlate);

    Optional<Request> findByRequestIdAndUserPlateAndRequestStatus(Long id, UserPlate userPlate, RequestStatus status);

    Optional<Request> findByRequestIdAndPaymentAndRequestStatus(Long id, Payment payment, RequestStatus status);

    /**
     * @param status
     * @return to call Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb)
     */
    static Specification<Request> hasStatus(RequestStatus status) {
        return (root, cq, cb) -> cb.equal(root.get("requestStatus"), status);
    }

    static Specification<Request> hasUser(User user) {
        return (root, cq, cb) -> cb.equal(root.get("userPlate").get("user"), user);
    }

    static Specification<Request> hasRequestId(Long id) {
        return (root, cq, cb) -> cb.equal(root.get("requestId"), id);
    }

    static Specification<Request> hasUserPlate(UserPlate userPlate) {
        return (root, cq, cb) -> cb.equal(root.get("userPlate"), userPlate);
    }
}
