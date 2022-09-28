package ir.co.sadad.cartollapi.service;

import ir.co.sadad.cartollapi.dtos.*;
import ir.co.sadad.cartollapi.entities.*;
import ir.co.sadad.cartollapi.enumurations.*;
import ir.co.sadad.cartollapi.exception.CarTollException;
import ir.co.sadad.cartollapi.exception.SSOException;
import ir.co.sadad.cartollapi.exception.SadadNajiException;
import ir.co.sadad.cartollapi.providers.naji.SadadNajiServices;
import ir.co.sadad.cartollapi.providers.otp.OTPServices;
import ir.co.sadad.cartollapi.providers.payment.PaymentServices;
import ir.co.sadad.cartollapi.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {


    private final SadadNajiServices sadadNajiServices;
    private final PaymentRepository paymentRepository;
    private final RequestRepository requestRepository;
    private final InquiryResultRepository inquiryResultRepository;
    private final UserPlateRepository userPlateRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OTPServices otpServices;
    private final PaymentServices paymentServices;


    @Value(value = "${payment.wage.amount}")
    private Integer wageAmount;


    @Override
    public ViolationAggregationResponseDto getAggregationViolation(@Valid ViolationRequestDto request, String ssn, String authToken) {


        Request savedRequest = checkingForViolationInquiry(request, ssn);

        try {
            ViolationAggregationData violationAggregation = sadadNajiServices.getViolationAggregation(
                    request.getPhoneNo(),
                    request.getPlateNo(),
                    savedRequest.getUserPlate().getPlate().getPlateOwnerNationalCode());


            if (violationAggregation.getData() != null) {
                savedRequest.setRequestStatus(RequestStatus.INQUIRY_SUCCESS);
                requestRepository.saveAndFlush(savedRequest);

                InquiryResult inquiryResult = new InquiryResult();
                inquiryResult.setInquiryType(InquiryType.VEHICALE_VIOLATION_AGGREGATION);
                inquiryResult.setAmount(violationAggregation.getData().getTotalAmount());
                inquiryResult.setPlateFa(violationAggregation.getData().getPlateFa());
                inquiryResult.setComplaintStatus(violationAggregation.getData().getComplaintStatus());
                inquiryResult.setComplaint(violationAggregation.getData().getComplaint());
                inquiryResult.setHasDebit(violationAggregation.getData().getHasDebit());
                inquiryResult.setPaperId(violationAggregation.getData().getPaperId());
                inquiryResult.setPaymentId(Long.valueOf(violationAggregation.getData().getPaymentId()).toString());
                inquiryResult.setPaymentStatus("نامشخص");
                inquiryResult.setRequest(savedRequest);
                inquiryResult.setPlateNo(request.getPlateNo());
                inquiryResultRepository.saveAndFlush(inquiryResult);

                changeDebitCount(savedRequest, -1);

                return modelMapper.map(violationAggregation.getData(), ViolationAggregationResponseDto.class);

            } else throw new CarTollException("inquiry.error", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //451

        } catch (Exception e) {
            if (!request.isHasDebit() && reverse(savedRequest.getPayment().getReferenceId(), authToken)) {
                changeDebitCount(savedRequest, -1);

                savedRequest.setRequestStatus(RequestStatus.REVERSE_SUCCESS);
                requestRepository.saveAndFlush(savedRequest);
                throw new CarTollException(e instanceof SadadNajiException ? e.getMessage()
                        : "inquiry.error"
                        , "reverse.done", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            }
            savedRequest.setRequestStatus(RequestStatus.INQUIRY_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            throw new CarTollException(e instanceof SadadNajiException ? e.getMessage() : "inquiry.error.reverse.failed"
                    , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
    }

    @Override
    public ViolationListResponseDto getViolationList(@Valid ViolationRequestDto request, String ssn, String authToken) {

        Request savedRequest = checkingForViolationInquiry(request, ssn);

        try {

            ViolationListData violationList = sadadNajiServices.getViolationList(request.getPhoneNo(),
                    request.getPlateNo(),
                    savedRequest.getUserPlate().getPlate().getPlateOwnerNationalCode());

            if (violationList.getData() != null) {
                savedRequest.setRequestStatus(RequestStatus.INQUIRY_SUCCESS);
                requestRepository.saveAndFlush(savedRequest);

                InquiryResult masterInquiryResult = new InquiryResult();

                masterInquiryResult.setInquiryType(InquiryType.VEHICALE_VIOLATION_LIST);
                masterInquiryResult.setPlateDictation(violationList.getData().getPlateDictation());
                masterInquiryResult.setPlateFa(violationList.getData().getPlateFa());
                masterInquiryResult.setComplaintStatus(violationList.getData().getComplaintStatus());
                masterInquiryResult.setComplaint(violationList.getData().getComplaint());
                masterInquiryResult.setHasDebit(violationList.getData().getHasDebit());
                masterInquiryResult.setInquiryDate(violationList.getData().getInquiryDate());
                masterInquiryResult.setInquiryTime(violationList.getData().getInquiryTime());
                masterInquiryResult.setLastSysUpdateDate(violationList.getData().getLastSystemUpdateDate());
                masterInquiryResult.setPaymentId(Long.valueOf(violationList.getData().getPaymentId()).toString());
                masterInquiryResult.setPaperId(violationList.getData().getPaperId());
                masterInquiryResult.setPaymentStatus("نامشخص");
                masterInquiryResult.setPlateNo(request.getPlateNo());
                masterInquiryResult.setRequest(savedRequest);
                InquiryResult savedParentResult = inquiryResultRepository.saveAndFlush(masterInquiryResult);

                violationList.getData().getViolationDetails().forEach(detail -> {
                    InquiryResult inquiryResult = new InquiryResult();

                    inquiryResult.setInquiryType(InquiryType.VEHICALE_VIOLATION_LIST);
                    inquiryResult.setPlateDictation(violationList.getData().getPlateDictation());
                    inquiryResult.setPlateFa(violationList.getData().getPlateFa());
                    inquiryResult.setInquiryDate(violationList.getData().getInquiryDate());
                    inquiryResult.setInquiryTime(violationList.getData().getInquiryTime());

                    inquiryResult.setSerialNo(detail.getSerialNo());
                    inquiryResult.setViolationOccurDate(detail.getViolationOccureDate());
                    inquiryResult.setViolationOccurTime(detail.getViolationOccureTime());
                    inquiryResult.setViolationAddress(detail.getViolatoinAddress());
                    inquiryResult.setViolationDeliveryType(detail.getViolationDeliveryType());
                    inquiryResult.setViolationTypeId(detail.getViolationTypeId());
                    inquiryResult.setViolationType(detail.getViolationType());
                    inquiryResult.setAmount(detail.getAmount());
                    inquiryResult.setPaperId(detail.getPaperId());
                    inquiryResult.setPaymentId(detail.getPaymentId());
                    inquiryResult.setHasImage(detail.getHasImage());
                    inquiryResult.setPaymentStatus("نامشخص");
                    inquiryResult.setPlateNo(request.getPlateNo());
                    inquiryResult.setParentId(savedParentResult.getId());

                    inquiryResult.setRequest(savedRequest);
                    inquiryResultRepository.saveAndFlush(inquiryResult);

                });

                changeDebitCount(savedRequest, -1);

                return modelMapper.map(violationList.getData(), ViolationListResponseDto.class);

            } else throw new CarTollException("inquiry.error", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        } catch (Exception e) {
            if (!request.isHasDebit() && reverse(savedRequest.getPayment().getReferenceId(), authToken)) {
                changeDebitCount(savedRequest, -1);

                savedRequest.setRequestStatus(RequestStatus.REVERSE_SUCCESS);
                requestRepository.saveAndFlush(savedRequest);
                throw new CarTollException(e instanceof SadadNajiException ? e.getMessage()
                        : "inquiry.error"
                        , "reverse.done", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            }
            savedRequest.setRequestStatus(RequestStatus.INQUIRY_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            throw new CarTollException(e instanceof SadadNajiException ? e.getMessage() : "inquiry.error.reverse.failed"
                    , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

    }

    @Override
    public ViolationImageResponseDto getViolationImage(@Valid ViolationImageRequestDto request, String ssn) {

        UserPlate userPlate = userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, request.getPlateNo()).orElseThrow(
                () -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND)
        );
        try {
            ViolationImageData response = sadadNajiServices.getViolationImage(request.getPhoneNo(),
                    request.getPlateNo(),
                    userPlate.getPlate().getPlateOwnerNationalCode(),
                    request.getSerialNo());

            return response.getData();

        } catch (Exception e) {
            log.error("Image SERVICE ERROR IS >>>>>>>>> " + e);
            throw new CarTollException(e instanceof SadadNajiException ? e.getMessage() : "inquiry.error.reverse.failed"
                    , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
    }

    public OtpResponseDto otpRequestService(OtpRequestDto otpReq, String ssn, String token) {

        UserPlate userPlate = userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, otpReq.getPlateNo()).orElseThrow(
                () -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND)
        );
        OtpResponseDto res = new OtpResponseDto();

        if (ssn.equals(userPlate.getPlate().getPlateOwnerNationalCode())) {
            return res.setResponseMessage("plate.belong.to.user.no.need.otp")
                    .setResponseCode("202")
                    .setRequestId(null);
        }

        Request savedRequest = createInquiryRequest(userPlate);

        try {

            SSOResponseDto response = otpServices.sendOtpMessage(userPlate.getPlate().getPlateOwnerNationalCode(),
                    otpReq.getOwnerCellphone(),
                    token);
            if (response != null && response.getError().equals("OTP_SENT")) {
                res.setResponseMessage("otp.sent")
                        .setResponseCode("200")
                        .setRequestId(savedRequest.getRequestId());
            } else
                throw new CarTollException("core.otp.service.exception", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            return res;

        } catch (Exception e) {

            if (e instanceof SSOException) {
                SSOException ex = (SSOException) e;

                if ((ex.getHttpStatus().equals(HttpStatus.PRECONDITION_FAILED))) {
                    res.setResponseMessage("otp.sent")
                            .setResponseCode("200")
                            .setRequestId(savedRequest.getRequestId());
                    return res;
                }
                savedRequest.setRequestStatus(RequestStatus.OTP_FAILED);
                requestRepository.saveAndFlush(savedRequest);
                throw new CarTollException(e.getMessage(),
                        ex.getDescription(),
                        ex.getHttpStatus());
            }


            savedRequest.setRequestStatus(RequestStatus.OTP_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            log.error("otp request service exception: " + e);
            throw new CarTollException(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        }

    }

    public OtpResponseDto otpVerifyService(OtpVerifyRequestDto otpReq, String ssn, String token) {

        if (otpReq.getRequestId() == null || otpReq.getOtpCode().equals("") || otpReq.getOtpCode() == null)
            throw new CarTollException("method.argument.not.valid", HttpStatus.BAD_REQUEST);

        UserPlate userPlate = userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, otpReq.getPlateNo()).orElseThrow(
                () -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND)
        );

        Request savedRequest = requestRepository.findByRequestIdAndUserPlate(otpReq.getRequestId(), userPlate).orElseThrow(
                () -> new CarTollException("request.not.found", HttpStatus.NOT_FOUND));

        try {
            SSOResponseDto response =
                    otpServices.verifyOtp(savedRequest.getUserPlate().getPlate().getPlateOwnerNationalCode(),
                            otpReq.getOwnerCellphone(),
                            otpReq.getOtpCode(),
                            token);

            OtpResponseDto res = new OtpResponseDto();

            if (response == null) {
                savedRequest.setRequestStatus(RequestStatus.OTP_SUCCESS);
                requestRepository.saveAndFlush(savedRequest);
                res.setRequestId(savedRequest.getRequestId())
                        .setResponseCode("200")
                        .setResponseMessage("otp.verified");
            } else
                throw new CarTollException("otp.verify.problem", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            return res;

        } catch (Exception e) {

            savedRequest.setRequestStatus(RequestStatus.OTP_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            log.error("otp verify service exception: " + e);
            if (e instanceof SSOException) {
                throw new CarTollException(e.getMessage(),
                        ((SSOException) e).getDescription(),
                        HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            }
            throw new CarTollException(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

    }


    public WagePaymentResponseDto wagePayment(WagePaymentRequestDto request,
                                              String ssn,
                                              String authToken) {

        Request savedRequest;
        UserPlate userPlate = userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, request.getPlateNo()).orElseThrow(
                () -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND)
        );

        WagePaymentResponseDto res = new WagePaymentResponseDto();

        if (ssn.equals(userPlate.getPlate().getPlateOwnerNationalCode()) || !request.isInquiryDetail()) {
            savedRequest = createInquiryRequest(userPlate);

        } else {
            if (request.getRequestId() == null)
                throw new CarTollException("request.id.must.not.be.null", HttpStatus.BAD_REQUEST);

            savedRequest = requestRepository.findOne(Specification.where(RequestRepository.hasUserPlate(userPlate)
                    .and(RequestRepository.hasRequestId(request.getRequestId()))
                    .and(RequestRepository.hasStatus(RequestStatus.OTP_SUCCESS)
                            .or(RequestRepository.hasStatus(RequestStatus.WAGE_TAN_FAILED)))
            )).orElseThrow(() -> new CarTollException("request.not.found", HttpStatus.NOT_FOUND));
        }

        PaymentRequestDto body = new PaymentRequestDto();
        body.setId(UUID.randomUUID().toString());
        body.setAlias(request.getAlias());
        body.setAccountId(request.getAccountId());
        body.setInstructedAmount(wageAmount);
        body.setPaymentReference(request.getPaymentReference());
        body.setPaymentDescription(request.getPaymentDescription());

        try {
            NajiPaymentDto.Response response = paymentServices.paymentService(body, authToken);

            if (response != null && response
                    .getResultSet()
                    .getInnerResponse()
                    .getStatus()
                    .equals("SUCCEEDED")) {

                Payment payment = new Payment();
                payment.setAccountNo(request.getAccountId());
                payment.setAmount(wageAmount.toString());
                payment.setTraceNo(response.getResultSet().getInnerResponse().getTraceNo());
                payment.setReferenceId(body.getId());
                Payment savedPayment = paymentRepository.saveAndFlush(payment);

                savedRequest.setRequestStatus(RequestStatus.WAGE_SUCCESS);
                savedRequest.setPayment(savedPayment);
                requestRepository.saveAndFlush(savedRequest);

                //add debit count
                changeDebitCount(savedRequest, 1);


                res.setRequestId(savedRequest.getRequestId())
                        .setPaymentId(payment.getPaymentId())
                        .setWageId(payment.getReferenceId())
                        .setResponseCode("200")
                        .setResponseMessage("wage.paid");

                return res;

            } else if (response != null)
                throw new CarTollException(response
                        .getResultSet()
                        .getInnerResponse()
                        .getResponseMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            else
                throw new CarTollException("core.wage.payment.exception", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        } catch (Exception e) {
            if (e instanceof HttpStatusCodeException && ((HttpStatusCodeException) e).getStatusCode().equals(HttpStatus.PRECONDITION_FAILED)) {
                log.error("wage payment 412 : " + e.getMessage());

                //add payment with generated wage id for trace
                Payment payment = new Payment();
                payment.setAccountNo(request.getAccountId());
                payment.setAmount(wageAmount.toString());
                payment.setReferenceId(body.getId());
                Payment savedPayment = paymentRepository.saveAndFlush(payment);


                savedRequest.setRequestStatus(RequestStatus.WAGE_TAN_SEND);
                savedRequest.setPayment(savedPayment);
                requestRepository.saveAndFlush(savedRequest);

                //send response with 412 response
                res.setRequestId(savedRequest.getRequestId())
                        .setPaymentId(payment.getPaymentId())
                        .setWageId(payment.getReferenceId())
                        .setResponseCode("412")
                        .setResponseMessage("wage.TAN.SEND");

                return res;

            }

            savedRequest.setRequestStatus(RequestStatus.WAGE_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            log.error("wage payment exception: " + e.getMessage());
            throw new CarTollException(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

    }

    @Override
    public WagePaymentResponseDto tanVerifyWagePayment(TanRequestDto request,
                                                       String ssn,
                                                       String authToken) {

        Request savedRequest = requestRepository.findById(request.getRequestId()).orElseThrow(() ->
                new CarTollException("request.not.found", HttpStatus.NOT_FOUND));

        if (!savedRequest.getRequestStatus().equals(RequestStatus.WAGE_TAN_SEND)) {
            throw new CarTollException("request.status.not.tan", HttpStatus.BAD_REQUEST);
        }

        if (!savedRequest.getPayment().getReferenceId().equals(request.getWageId())) {
            throw new CarTollException("payment.wage.id.is.not.equal", HttpStatus.BAD_REQUEST);
        }

        WagePaymentResponseDto res = new WagePaymentResponseDto();

        try {

            NajiPaymentDto.PaymentTanRequest body = new NajiPaymentDto.PaymentTanRequest();
            body.setId(savedRequest.getPayment().getReferenceId());


            NajiPaymentDto.Response response = paymentServices.tanVerifyPayment(body, request.getTanCode(), authToken);


            if (response != null && response
                    .getResultSet()
                    .getInnerResponse()
                    .getStatus()
                    .equals("SUCCEEDED")) {

                Payment payment = savedRequest.getPayment();
                payment.setTraceNo(response.getResultSet().getInnerResponse().getTraceNo());
                Payment savedPayment = paymentRepository.saveAndFlush(payment);

                savedRequest.setRequestStatus(RequestStatus.WAGE_SUCCESS);
                savedRequest.setPayment(savedPayment);
                requestRepository.saveAndFlush(savedRequest);

                //add debit count
                changeDebitCount(savedRequest, 1);


                res.setRequestId(savedRequest.getRequestId())
                        .setPaymentId(payment.getPaymentId())
                        .setWageId(payment.getReferenceId())
                        .setResponseCode("200")
                        .setResponseMessage("wage.paid");

                return res;

            } else if (response != null)
                throw new CarTollException(response
                        .getResultSet()
                        .getInnerResponse()
                        .getResponseMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

            else
                throw new CarTollException("wage.tan.payment.exception", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        } catch (Exception e) {
            savedRequest.setRequestStatus(RequestStatus.WAGE_TAN_FAILED);
            requestRepository.saveAndFlush(savedRequest);
            log.error("tan verify wage payment exception: " + e.getMessage());
            throw new CarTollException(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        }
    }


    private Request createInquiryRequest(UserPlate userPlate) {

        Request request = new Request();
        request.setRequestStatus(RequestStatus.INQUIRY_REQUEST);
        request.setRequestType(RequestType.INQUIRY_ORDER);
        request.setOrigination(Origination.HAMBAM);
        request.setServiceType(ServiceType.BILL_VIOLATION);
        request.setUserPlate(userPlate);
        return requestRepository.saveAndFlush(request);
    }

    private boolean reverse(String wageId, String authToken) {
        try {
            NajiPaymentDto.ReverseRequest req = new NajiPaymentDto.ReverseRequest();
            req.setMoneyTransferRequestId(wageId).setPaymentReference("بازگشت کارمزد خلافی خودرو").setDescription("بازگشت کارمزد خلافی خودرو");
            NajiPaymentDto.ReverseResponse reverse = paymentServices.reverse(req, authToken);
            return reverse.getStatus().equals("SUCCESS");
        } catch (Exception e) {
            return false;
        }

    }

    private void changeDebitCount(Request savedRequest, int i) {
        @NotNull User savedUser = savedRequest.getUserPlate().getUser();
        savedUser.setDebitCount(savedUser.getDebitCount() + i);
        userRepository.saveAndFlush(savedUser);
        log.info("debitCount decreased/increased " + i + " number");
    }

    private Request checkingForViolationInquiry(ViolationRequestDto request, String ssn) {

        Request savedRequest = null;
        Payment savedPayment;
        try {
            if (request.isHasDebit()) {
                UserPlate savedUserPlate = userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, request.getPlateNo())
                        .orElseThrow(() -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND));

                if (savedUserPlate.getUser().getDebitCount() <= 0)
                    throw new CarTollException("user.has.not.debit.count", HttpStatus.BAD_REQUEST);

                List<Request> validRequestListOfUser = requestRepository.findAll(Specification.where(RequestRepository.hasUser(savedUserPlate.getUser())
                        .and(RequestRepository.hasStatus(RequestStatus.WAGE_SUCCESS)
                                .or(RequestRepository.hasStatus(RequestStatus.INQUIRY_FAILED)))
                ));

                if (validRequestListOfUser.size() != 0) {
                    savedRequest = validRequestListOfUser.stream().filter(req -> req.getUserPlate().getPlate().getPlateNo().equals(request.getPlateNo())).findFirst()
                            .orElse(createInquiryRequest(savedUserPlate));

                } else
                    //because of deleting request's records which indicates wage-success of inquiry-failed on other plates of this user !
                    savedRequest = createInquiryRequest(savedUserPlate);


            } else {
                if (request.getRequestId() == null || request.getPaymentId().equals("") || request.getPaymentId() == null)
                    throw new CarTollException("method.argument.not.valid", HttpStatus.BAD_REQUEST);

                savedPayment = paymentRepository.findById(Long.valueOf(request.getPaymentId()))
                        .orElseThrow(() -> new CarTollException("payment.not.found", HttpStatus.NOT_FOUND));

                savedRequest = requestRepository.findByRequestIdAndPaymentAndRequestStatus(request.getRequestId(), savedPayment, RequestStatus.WAGE_SUCCESS)
                        .orElseThrow(() -> new CarTollException("request.for.get.inquiry.not.found", HttpStatus.NOT_FOUND));

                //check if inquiry is for the requested plateNo
                if (!savedRequest.getUserPlate().getPlate().getPlateNo().equals(request.getPlateNo())) {
                    savedRequest.setRequestStatus(RequestStatus.INQUIRY_FAILED);
                    requestRepository.saveAndFlush(savedRequest);
                    throw new CarTollException("inquiry.and.plate.not.match", HttpStatus.NOT_FOUND);
                }
            }
            return savedRequest;

        } catch (NumberFormatException e) {
            if (savedRequest != null) {
                savedRequest.setRequestStatus(RequestStatus.INQUIRY_FAILED);
                requestRepository.saveAndFlush(savedRequest);
            }
            log.error("checking violation inquiry exception ");
            throw new CarTollException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
