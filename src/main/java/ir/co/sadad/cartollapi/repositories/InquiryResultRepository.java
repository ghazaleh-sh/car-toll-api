package ir.co.sadad.cartollapi.repositories;

import ir.co.sadad.cartollapi.entities.InquiryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InquiryResultRepository extends JpaRepository<InquiryResult, Long>, JpaSpecificationExecutor<InquiryResult> {

    List<InquiryResult> findByRequest_RequestId(Long reqId);

    List<InquiryResult> findByPaymentIdAndPaperId(String paymentId, String paperId);

    List<InquiryResult> findByParentId(Long parentId);
}
