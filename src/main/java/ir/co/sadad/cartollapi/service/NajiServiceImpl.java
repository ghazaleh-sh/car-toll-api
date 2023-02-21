package ir.co.sadad.cartollapi.service;

import ir.co.sadad.cartollapi.dtos.*;
import ir.co.sadad.cartollapi.entities.*;
import ir.co.sadad.cartollapi.enumurations.InquiryType;
import ir.co.sadad.cartollapi.enumurations.PlateType;
import ir.co.sadad.cartollapi.enumurations.RequestStatus;
import ir.co.sadad.cartollapi.exception.CarTollException;
import ir.co.sadad.cartollapi.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static ir.co.sadad.cartollapi.service.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NajiServiceImpl implements NajiService {

    private final UserRepository userRepository;
    private final PlateRepository plateRepository;
    private final UserPlateRepository userPlateRepository;
    private final InquiryResultRepository inquiryResultRepository;
    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;


    @Transactional(propagation = Propagation.NESTED)
    public PlateCreateResponseDto create(PlateCreateRequestDto request, String ssn, String cellphone) {

        checkPlateNoFormat(request.getPlateNo(), request.getType());

        if (userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, request.getPlateNo()).isPresent())
            throw new CarTollException("user.plate.duplicate", HttpStatus.BAD_REQUEST);

        User savedUser = userRepository.findByNationalCode(ssn).orElseGet(() -> {
            User user = new User();
            user.setCellPhone(cellphone);
            user.setNationalCode(ssn);
            user.setDebitCount(0);
            return userRepository.saveAndFlush(user);
        });

        Plate plate = new Plate();
        plate.setPlateNo(request.getPlateNo());
        plate.setType(PlateType.valueOf(request.getType()));
        plate.setName(request.getName());
        plate.setPlateOwnerNationalCode(request.getPlateOwnerNationalCode());
        plate.setPlateOwnerCellPhone(request.getPlateOwnerCellPhone());
        plate.setBarcode(request.getBarcode());
        if (request.getType().equals(PlateType.CAR.toString()))
            plate.setVin(request.getVin());
        else if (request.getType().equals(PlateType.MOTORCYCLE.toString()))
            plate.setMotorCycleNumber(request.getMotorCycleNumber());
        Plate savedPlate = plateRepository.saveAndFlush(plate);

        UserPlate userPlate = new UserPlate();
        userPlate.setPlate(savedPlate);
        userPlate.setUser(savedUser);
        userPlateRepository.saveAndFlush(userPlate);


        PlateCreateResponseDto response = new PlateCreateResponseDto();
        PlateInfoDto plateInfo = new PlateInfoDto();
        plateInfo.setPlateOwnerNationalCode(request.getPlateOwnerNationalCode());
        plateInfo.setPlateOwnerCellPhone(request.getPlateOwnerCellPhone());
        plateInfo.setName(request.getName());
        plateInfo.setPlateNo(request.getPlateNo());
        plateInfo.setType(request.getType());
        plateInfo.setBarcode(request.getBarcode());
        if (request.getType().equals(PlateType.CAR.toString()))
            plateInfo.setVin(request.getVin());
        else if (request.getType().equals(PlateType.MOTORCYCLE.toString()))
            plateInfo.setMotorCycleNumber(request.getMotorCycleNumber());
        response.setPlateInfo(plateInfo);
        return response;

    }

    @Transactional(readOnly = true)
    public PlateSearchResponseDto getPlates(String ssn) {
        PlateSearchResponseDto response = new PlateSearchResponseDto();
        List<PlateInfoDto> plateInfos = new ArrayList<>();

        List<UserPlate> savedUserPlate = userPlateRepository.findByUser_NationalCode(ssn);

        try {
            savedUserPlate.forEach(up -> {
                PlateInfoDto plateInfo = modelMapper.map(up.getPlate(), PlateInfoDto.class);
                plateInfos.add(plateInfo);
            });

            if (savedUserPlate.isEmpty()) {
                response.setUserNationalCode(ssn);
                response.setUserDebitCount(0);
            } else {
                response.setUserNationalCode(savedUserPlate.get(0).getUser().getNationalCode());
                response.setUserDebitCount(savedUserPlate.get(0).getUser().getDebitCount());
            }

            response.setPlateInfo(plateInfos);
            return response;
        } catch (Exception ex) {
            response.setPlateInfo(Collections.emptyList());
            return response;
        }
    }

    @Transactional
    public PlateDeleteResponseDto deletePlate(String plateNo, String ssn) {

        UserPlate userPlate = getUserPlateFromPlateNo(plateNo, ssn);

        List<Request> requests = requestRepository.findByUserPlate(userPlate);
        requests.forEach(r -> {
                    List<InquiryResult> inquiries = inquiryResultRepository.findByRequest_RequestId(r.getRequestId());
                    if (!inquiries.isEmpty())
                        inquiryResultRepository.deleteAll(inquiries);

                    requestRepository.delete(r);
                }
        );
        userPlateRepository.delete(userPlate);


        PlateDeleteResponseDto response = new PlateDeleteResponseDto();
        response.setResponseCode(SUCCESS_CODE);
        response.setResponseMessage(messageSource.getMessage("success.response.message", null, new Locale("fa")));
        return response;
    }

    public PlateUpdateResponseDto updatePlate(PlateUpdateRequestDto updateRequest, String ssn) {

        UserPlate userPlate = getUserPlateFromPlateNo(updateRequest.getPlateNo(), ssn);

        Plate savedPlate = userPlate.getPlate();
        modelMapper.map(updateRequest, savedPlate);
        if (savedPlate.getType().equals(PlateType.CAR))
            savedPlate.setMotorCycleNumber(null);
        if (savedPlate.getType().equals(PlateType.MOTORCYCLE))
            savedPlate.setVin(null);
        plateRepository.saveAndFlush(savedPlate);

        PlateUpdateResponseDto response = new PlateUpdateResponseDto();
        response.setPlateInfo(modelMapper.map(savedPlate, PlateInfoDto.class));
        return response;

    }

    public ViolationInquiryResponseDto getInquiryResults(String ssn) {
        List<UserPlate> userPlate = userPlateRepository.findByUser_NationalCode(ssn);

        ViolationInquiryResponseDto response = new ViolationInquiryResponseDto();
        List<InquiryDetailDto> najiInquiryResults = new ArrayList<>();
        try {
            userPlate.forEach(up -> {
                requestRepository.findByUserPlateAndRequestStatus(up, RequestStatus.INQUIRY_SUCCESS).forEach(request -> {
                    inquiryResultRepository.findByRequest_RequestId(request.getRequestId()).forEach(inquiry -> {
                        if (inquiry.getInquiryType().equals(InquiryType.VEHICALE_VIOLATION_AGGREGATION)
                                || (inquiry.getInquiryType().equals(InquiryType.VEHICALE_VIOLATION_LIST) && inquiry.getParentId() != null))
                            najiInquiryResults.add(modelMapper.map(inquiry, InquiryDetailDto.class));
                    });
                });
            });

            response.setInquiryList(najiInquiryResults);
            return response;

        } catch (Exception e) {
            response.setInquiryList(Collections.emptyList());
            return response;
        }
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void setInquiryStatus(String ssn, String paymentId, String paperId) {

        List<InquiryResult> validInquiryRecords = inquiryResultRepository.findByPaymentIdAndPaperId(paymentId, paperId);
        String msg = messageSource.getMessage("violation.paid", null, new Locale("fa"));

        if (!validInquiryRecords.isEmpty()) {
            validInquiryRecords.forEach(inquiryResult -> {
                inquiryResult.setPaymentStatus(msg);
                if (inquiryResult.getParentId() == null)
                    inquiryResultRepository.findByParentId(inquiryResult.getId())
                            .forEach(childInquiry -> {
                                childInquiry.setPaymentStatus(msg);
                                inquiryResultRepository.saveAndFlush(childInquiry);
                            });

                inquiryResultRepository.saveAndFlush(inquiryResult);
            });

        } else
            throw new CarTollException("payment.info.not.found", HttpStatus.NOT_FOUND);
    }

//    public CarTollSummaryDto.Response getSummary(Long userId, String deviceId, Integer plateNo) {
//
//        if (plateNo == null) throw new CarTollException("plate.no.is.null", HttpStatus.BAD_REQUEST);
//
//        UserPlate userPlate = getUserPlateFromPlateNo(plateNo.toString(), userId);
//
//        CarTollSummaryDto.Response summary = simorqService.summary(deviceId, plateNo);
//
//        ErrorLog savedErrorLog = saveErrorLogs(summary.getActionCode(), summary.getActionMessage(), summary.getActionCode());
//
//        return summary;
//
//    }

//    public CarTollPaymentDto.Response PayRequestFreewayTollByCard(CarTollPaymentDto.Request request, Long userId) throws ServiceUnavailableException {
//
//        UserPlate userPlate = getUserPlateFromPlateNo(request.getPlateNo(), userId);
//
//        final AtomicLong TS = new AtomicLong(System.currentTimeMillis() * 1000);
//        Long orderId = TS.incrementAndGet();
//
//        Long totalAmount = 0L;
//        for (CarTollPaymentDto.Request.BillInfo billInfo : request.getBillInfo())
//            totalAmount += billInfo.getAmount().longValue();
//
//        PspPaymentRequestDto.Response pspResponse = pspService.paymentRequest(orderId, totalAmount);
//
//        ErrorLog savedErrorLog = saveErrorLogs(String.valueOf(pspResponse.getResCode()), pspResponse.getDescription(), String.valueOf(pspResponse.getResCode()));
//
//        for (CarTollPaymentDto.Request.BillInfo billInfo : request.getBillInfo()) {
//
//            Transaction transaction = new Transaction();
//            transaction.setOrderId(orderId.toString());
//            transaction.setToken(pspResponse.getToken());
//            transaction.setUserPlate(userPlate);
//            transaction.setStatus(pspResponse.getResCode() == 0);
//            transaction.setResponseDateTime(new Date());
//            transaction.setEnquiryId(request.getEnquiryId());
//            transaction.setBillId(billInfo.getBillId());
//            transaction.setAmount(billInfo.getAmount());
//            transaction.setRequestType(RequestType.PAYMENT_REQUEST);
//            transaction.setErrorLog(savedErrorLog);
//
//            transactionRepository.saveAndFlush(transaction);
//        }
//
//        CarTollPaymentDto.Response response = new CarTollPaymentDto.Response();
//        response.setOrderId(orderId);
//        response.setResponseCode(pspResponse.getResCode());
//        response.setResponseMessage(pspResponse.getDescription());
//        response.setToken(pspResponse.getToken());
//
//        return response;
//    }

//    public CarTollFinalPaymentDto.Response finalPayFreewayTollByCard(CarTollFinalPaymentDto.Request req, String serialId) throws ServiceUnavailableException {
//
//        Long fifteenMinAgo = new Date().getTime() - 15 * 60 * 1000;
//
//        List<Transaction> transactions = transactionRepository.findTransactionByOrderId(req.getOrderId().toString());
//        if (transactions == null || transactions.isEmpty() || transactions.get(0).getRequestType() != RequestType.PAYMENT_REQUEST)
//            throw new CarTollException("payment.request.not.exist", HttpStatus.BAD_REQUEST);
//
//        if (transactions.get(0).getResponseDateTime().getTime() < fifteenMinAgo)
//            throw new CarTollException("order.has.expired", HttpStatus.REQUEST_TIMEOUT);
//
//        PspVerifyDto.Response pspVerifyResponse = pspService.verify(transactions.get(0).getToken());
//
//        ErrorLog savedErrorLog = saveErrorLogs(String.valueOf(pspVerifyResponse.getResCode()), pspVerifyResponse.getDescription(), String.valueOf(pspVerifyResponse.getResCode()));
//
//        Long totalAmount = 0L;
//        List<String> billIds = new ArrayList<>();
//        for (Transaction transaction : transactions) {
//
//            totalAmount += transaction.getAmount().longValue();
//            billIds.add(transaction.getBillId());
//
//            transaction.setRequestType(RequestType.VERIFY);
//            transaction.setResponseDateTime(new Date());
//            transaction.setRrn(pspVerifyResponse.getRetrievalRefNo());
//            transaction.setErrorLog(savedErrorLog);
//            transaction.setStatus(pspVerifyResponse.getResCode() == 0);
//            transaction.setTraceNumber(pspVerifyResponse.getSystemTraceNo());
//
//            transactionRepository.saveAndFlush(transaction);
//        }
//
//        switch (pspVerifyResponse.getResCode()) {
//            case 0:
//                String plateNo = transactions.get(0).getUserPlate().getPlate().getPlateNo();
//                SimorqPayFreewayTollDto.Response simorqResponse;
//
//                if (transactions.size() > 1) {
//
//                    simorqResponse = simorqService.PayFreewayTolls(serialId, Integer.valueOf(plateNo), billIds, transactions.get(0).getEnquiryId(), Math.toIntExact(totalAmount));
//                } else {
//                    simorqResponse = simorqService.PayFreewayToll(serialId, Integer.valueOf(plateNo), transactions.get(0).getBillId(), transactions.get(0).getEnquiryId(), transactions.get(0).getAmount().intValue());
//                }
//
//                saveSimorgResult(simorqResponse, transactions, RequestType.PAYMENT_FREEWAY_TOLL_CARD);
//
//                return modelMapper.map(simorqResponse, CarTollFinalPaymentDto.Response.class);
//            case -1:
//                throw new ServiceUnavailableException("psp.verify.invalid.req");
//            case 101:
//                throw new ServiceUnavailableException("psp.verify.timeout");
//            default:
//                throw new CarTollException("psp.verify.unknown.res.code", HttpStatus.INTERNAL_SERVER_ERROR);
//        }

//    }

//    public CarTollWalletPaymentDto.Response payFreewayTollByWallet(CarTollWalletPaymentDto.Request request, String serialId, Long userId, String mobileNo, String nationalId, String oauthToken) throws JsonProcessingException {
//
//        UserPlate userPlate = getUserPlateFromPlateNo(request.getPlateNo(), nationalId);
//
//        final AtomicLong TS = new AtomicLong(System.currentTimeMillis() * 1000);
//        Long orderId = TS.incrementAndGet();
//
//        AtomicReference<Long> totalAmount = new AtomicReference<>(0L);
//        List<String> billIds = new ArrayList<>();
//
//        request.getBillInfo().stream().forEach(billInfo -> {
//                    totalAmount.updateAndGet(v -> v + billInfo.getAmount());
//                    billIds.add(billInfo.getBillId());
//                }
//        );
//
//        NeoWalletPaymentDto.Response trustedResponse = trustedPayment(totalAmount.get(), mobileNo, nationalId, request.getToken(), oauthToken);
////
////        ErrorLog savedErrorLog = saveErrorLogs(String.valueOf(trustedResponse.getResponseCode()),trustedResponse.getResponseMessage(), String.valueOf(trustedResponse.getResponseCode()));
////
////        for (CarTollWalletPaymentDto.Request.BillInfo billInfo : request.getBillInfo()) {
////
////            Transaction transaction = new Transaction();
////            transaction.setOrderId(orderId.toString());
////            transaction.setUserPlate(userPlate);
////            transaction.setStatus(trustedResponse.getResponseCode() == 0);
////            transaction.setResponseDateTime(new Date());
////            transaction.setEnquiryId(request.getEnquiryId());
////            transaction.setBillId(billInfo.getBillId());
////            transaction.setAmount(BigDecimal.valueOf(billInfo.getAmount()));
////            transaction.setRequestType(RequestType.TRUSTED_PAYMENT);
////            transaction.setErrorLog(savedErrorLog);
////
////            transactionRepository.saveAndFlush(transaction);
////        }
//
//        if (trustedResponse.getResponseCode() == 0) {
//
////            List<Transaction> transactions = transactionRepository.findTransactionByOrderId(String.valueOf(orderId));
//
//            SimorqPayFreewayTollDto.Response simorqResponse;
//            if (request.getBillInfo().size() > 1) {
//
//                simorqResponse = simorqService.PayFreewayTolls(serialId, Integer.valueOf(request.getPlateNo()), billIds, request.getEnquiryId(), Math.toIntExact(totalAmount.get()));
//            } else {
//                simorqResponse = simorqService.PayFreewayToll(serialId, Integer.valueOf(request.getPlateNo()), request.getBillInfo().get(0).getBillId(), request.getEnquiryId(), Math.toIntExact(totalAmount.get()));
//            }
//
////            saveSimorgResult(simorqResponse, transactions, RequestType.PAYMENT_FREEWAY_TOLL_WALLET);
//
//            CarTollWalletPaymentDto.Response result = modelMapper.map(simorqResponse, CarTollWalletPaymentDto.Response.class);
//            result.setFee(trustedResponse.getFee());
//            result.setBalanceInfo(trustedResponse.getBalanceInfo());
//
//            return result;
//        } else {
//            GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse();
//            generalErrorResponse.setCode(String.valueOf(trustedResponse.getResponseCode()));
//            generalErrorResponse.setMessage(trustedResponse.getResponseMessage());
//            generalErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            generalErrorResponse.setTimestamp(trustedResponse.getResponseDateTime());
//            throw new CarTollException(generalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private NeoWalletPaymentDto.Response trustedPayment(Long amount, String mobileNo, String nationalId, String token, String oauthToken) throws JsonProcessingException {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(oauthToken);
//
//        NeoWalletPaymentDto.Request walletPayment = new NeoWalletPaymentDto.Request();
//        walletPayment
//                .setToUserId(this.toUserId)
//                .setAmount(amount)
//                .setMobileNo(mobileNo.replaceFirst("98", "0"))
//                .setNationalId(nationalId)
//                .setToken(token);
//        try {
//            HttpEntity<NeoWalletPaymentDto.Request> entity = new HttpEntity<>(walletPayment, headers);
//            ResponseEntity<NeoWalletPaymentDto.Response> responseEntity = restTemplate.postForEntity(trustedPaymentUrl, entity, NeoWalletPaymentDto.Response.class);
//            return responseEntity.getBody();
//        } catch (HttpStatusCodeException e) {
//
//            log.error("wallet payment exception: " + e);
//
//            GeneralErrorResponse generalErrorResponse = new ObjectMapper().readValue(e.getResponseBodyAsString(), GeneralErrorResponse.class);
//            throw new CarTollException(generalErrorResponse, e.getStatusCode());
//        }
//
//    }


    private void checkPlateNoFormat(String plateNo, String type) {

        if (!plateNo.matches("^[0-9]{8,9}$"))
            throw new CarTollException("plate.no.pattern.not.valid", HttpStatus.BAD_REQUEST);

        if (type.equals(PlateType.CAR.toString()))
            if (plateNo.length() != PLATE_NO_CAR)
                throw new CarTollException("plate.no.pattern.not.valid", HttpStatus.BAD_REQUEST);
        if (type.equals(PlateType.MOTORCYCLE.toString()))
            if (plateNo.length() != PLATE_NO_MOTOR)
                throw new CarTollException("plate.no.pattern.not.valid", HttpStatus.BAD_REQUEST);
    }

    private UserPlate getUserPlateFromPlateNo(String plateNo, String ssn) {

        if (plateNo == null || plateNo.equals(""))
            throw new CarTollException("plate.no.must.not.be.null", HttpStatus.BAD_REQUEST);

        return userPlateRepository.findByUser_NationalCodeAndPlate_PlateNo(ssn, plateNo).orElseThrow(
                () -> new CarTollException("plate.not.found", HttpStatus.NOT_FOUND)
        );
    }

//    private ErrorLog saveErrorLogs(String responseCode, String responseMessage, String findErrorLog) {
//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setResponseCode(responseCode);
//        errorLog.setResponseMessage(responseMessage);
//        ErrorLog savedErrorLog = errorLogRepository.findErrorLogByResponseCode(findErrorLog).orElseGet(
//                () -> errorLogRepository.saveAndFlush(errorLog));
//        return savedErrorLog;
//    }
//
//    private void saveSimorgResult(SimorqPayFreewayTollDto.Response simorqResponse, List<Transaction> transactions, RequestType requestType) {
//
//        ErrorLog savedNewErrorLog = saveErrorLogs(simorqResponse.getActionCode(), simorqResponse.getActionMessage(), simorqResponse.getActionCode());
//
//        for (Transaction transaction : transactions) {
//
//            transaction.setTraceNumber(simorqResponse.getTraceNumber());
//            transaction.setStatus("00000".equals(simorqResponse.getActionCode()));
//            transaction.setErrorLog(savedNewErrorLog);
//            transaction.setResponseDateTime(new Date());
//            transaction.setRrn(simorqResponse.getData().getRrn());
//            transaction.setRequestType(requestType);
//            transaction.setSuccess(simorqResponse.getData().getSuccess());
//            transaction.setReferenceNumber(simorqResponse.getReferenceNumber());
//
//            transactionRepository.saveAndFlush(transaction);
//        }
//    }
}
