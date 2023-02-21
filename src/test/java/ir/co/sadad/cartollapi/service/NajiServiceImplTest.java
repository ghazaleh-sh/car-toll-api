package ir.co.sadad.cartollapi.service;

import ir.co.sadad.cartollapi.dtos.PlateCreateRequestDto;
import ir.co.sadad.cartollapi.dtos.PlateDeleteRequestDto;
import ir.co.sadad.cartollapi.enumurations.PlateType;
import ir.co.sadad.cartollapi.exception.CarTollException;
import ir.co.sadad.cartollapi.service.util.Utility;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static ir.co.sadad.cartollapi.service.util.Constants.SUCCESS_CODE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev"})
@RunWith(Parameterized.class)
class NajiServiceImplTest {

    @Autowired
    private NajiServiceImpl najiServiceImpl;

    private Long userId = 158L;
    private String ssn = "0079993141";
    private String cellphone = "9124150188";
    private String deviceId = "5700cd58-3cd6-4ce3-81ff-ee519e1f6df7";

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestDataOfImproperPlateTags() {
        return Arrays.asList(new Object[][]{
                {null, "plate.tag.must.not.be.null"},
                {"", "plateTag is not valid!"},
                {"28ر1358", "{plate.tag.pattern.not.valid}"},
                {"465468988", "{plate.tag.pattern.not.valid}"},
                {"4654689ت88", "{plate.tag.pattern.not.valid}"},
                {"22ر12358", "InputIsDuplicated"}
        });
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestDataOfImproperPlateNo() {
        return Arrays.asList(new Object[][]{
                {null, CarTollException.class},
                {"", "plate.no.must.not.be.null"},
                {"78794651", "{plate.no.pattern.not.valid}"},
                {"587946551", "NotExist"}
        });
    }

    @Test
    void convertNajiTagToNumber(){
        String plateTag = "82ق23611";
        assertEquals("111082236",Utility.convertNajiPlateTagToNo(plateTag, PlateType.CAR));

    }

    ////////////////////////////////create method///////////////////////////////////
    @ParameterizedTest
    @MethodSource("getTestDataOfIncorrectPlateTags")
    void shouldThrowExceptionWhenInputIsImproper(String plateTag, String expectedMessage) {

        PlateCreateRequestDto request = new PlateCreateRequestDto();
        request.setPlateNo(plateTag);

        if (!(plateTag == null || plateTag.equals(""))) {
            Set<ConstraintViolation<PlateCreateRequestDto>> violations = validator.validate(request);
            if (!violations.isEmpty())
                assertEquals(expectedMessage, violations.stream().findFirst().get().getMessage());
            else
                assertThrows(DataIntegrityViolationException.class, () -> najiServiceImpl.create(request, ssn, cellphone));
        } else
            Assert.assertThrows(expectedMessage, CarTollException.class, () -> najiServiceImpl.create(request, ssn, cellphone));
    }

    @Test
    void shouldAddPlateCorrectly() {
        PlateCreateRequestDto request = new PlateCreateRequestDto();
        request.setPlateNo("99ر12358");
        request.setPlateOwnerCellPhone("091245122");
        request.setPlateOwnerNationalCode("0079993141");
        request.setType("CAR");

        assertEquals(request.getPlateNo(), najiServiceImpl.create(request, ssn, cellphone).getPlateInfo().getPlateNo());
    }


    ////////////////////////////////search method///////////////////////////////////
    @Test
    void shouldThrowExceptionWhenUserNotExist() {
        this.userId = 1000L;
        assertNull(najiServiceImpl.getPlates(ssn).getPlateInfo());
    }

    @Test
    void shouldReturnPlateInfoArraySize() {

        assertEquals(1, najiServiceImpl.getPlates(ssn).getPlateInfo().size());

    }

    ////////////////////////////////delete method///////////////////////////////////

    @ParameterizedTest
    @MethodSource("getTestDataOfImproperPlateNo")
    void shouldThrowExceptionWhenImproperPlateNo(String plateNo, String expectedMessage) {
        if (plateNo == null)
            assertThrows(CarTollException.class, () -> najiServiceImpl.deletePlate(plateNo, ssn));

        else {
            PlateDeleteRequestDto request = new PlateDeleteRequestDto();
            request.setPlateNo(plateNo);

            Set<ConstraintViolation<PlateDeleteRequestDto>> violations = validator.validate(request);
            if (!violations.isEmpty())
                assertEquals(expectedMessage, violations.stream().findFirst().get().getMessage());
            else
                assertThrows(CarTollException.class, () -> najiServiceImpl.deletePlate(request.getPlateNo(), ssn));
        }
    }

    @Test
    void shouldDeleteRecordWithSuccessCode() {

        PlateDeleteRequestDto request = new PlateDeleteRequestDto();
        request.setPlateNo("22121358");

        assertEquals(SUCCESS_CODE, najiServiceImpl.deletePlate(request.getPlateNo(), ssn).getResponseCode());
    }

    ////////////////////////////////summary method///////////////////////////////////
//    @ParameterizedTest
//    @MethodSource("getTestDataOfImproperPlateNo")
//    void shouldThrowExceptionWhenInputIsImproperSummary(String plateNo) {
//        if (plateNo == null || plateNo == "")
//            assertThrows(CarTollException.class, () -> najiServiceImpl.getSummary(userId, deviceId, null));
//
//        else
//            assertThrows(CarTollException.class, () -> najiServiceImpl.getSummary(userId, deviceId, Integer.valueOf(plateNo)));
//
//    }
//
//    @Test
//    void shouldGetSummaryOfPlateNo() {
//
//        assertEquals("00000", najiServiceImpl.getSummary(userId, deviceId, 660952525).getActionCode());
//    }
}