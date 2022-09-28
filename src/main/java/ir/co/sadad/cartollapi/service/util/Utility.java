package ir.co.sadad.cartollapi.service.util;

import ir.co.sadad.cartollapi.enumurations.NajiPlateChar;
import ir.co.sadad.cartollapi.enumurations.PlateType;
import ir.co.sadad.cartollapi.exception.CarTollException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Utility {

    private final Environment env;

    public String getPaymentRequestSignData(String terminalId, String orderId, String amount) {

        StringBuilder chainString = new StringBuilder(terminalId).append(";").append(orderId).append(";").append(amount);
        try {
            return new String(TripleDES.encrypt(chainString.toString(), env.getProperty("sadad.psp.tripleDesKey")));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new CarTollException("triple.des.failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public String getVerifySignData(String token) {
        try {
            return new String(TripleDES.encrypt(token, env.getProperty("sadad.psp.tripleDesKey")));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new CarTollException("triple.des.failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String convertNajiPlateTagToNo(String plateTag, PlateType type) {
        try {
            switch (type) {
                case CAR:
                    String plateChar = NajiPlateChar.getPlateMap().keySet().stream().filter(plateTag::contains).findAny()
                            .orElseThrow(() -> new CarTollException("plate.tag.pattern.not.valid", HttpStatus.BAD_REQUEST));

                    String plateValue = NajiPlateChar.getByKey(plateChar);
                    if (plateValue != null && plateTag.length() == 8)
                        return Integer.valueOf(plateTag.substring(6, 8).concat(plateValue).concat(plateTag.substring(0, 2)).concat(plateTag.substring(3, 6))).toString();
                    else
                        throw new CarTollException("plate.tag.pattern.not.valid", HttpStatus.BAD_REQUEST);

                case MOTORCYCLE:
                    if (plateTag.length() != 8)
                        throw new CarTollException("plate.tag.pattern.not.valid", HttpStatus.BAD_REQUEST);
                    return "08".concat(String.valueOf(Integer.valueOf(plateTag))).concat("000");
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            throw new CarTollException("plate.no.pattern.not.valid", HttpStatus.BAD_REQUEST);
        }
    }

}
