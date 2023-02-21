package ir.co.sadad.cartollapi.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PlateTagValidator implements ConstraintValidator<PlateTag, String> {

//    private Boolean notBlank;
    private String messageNotBlank;

    @Override
    public void initialize(PlateTag platetag) {
//        notBlank = platetag.notBlank();
        this.messageNotBlank = platetag.messageNotBlank();
    }

    @Override
    public boolean isValid(String plateTag, ConstraintValidatorContext context) {
        if (plateTag == null || plateTag.equals("")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageNotBlank).addConstraintViolation();
            return false;
        }

        if (plateTag.length() < 8 || plateTag.length() > 14) {
            return false;
        }
        return plateTag.matches("^[0-9]{2}([^0-9]{1,7})[0-9]{5}$") || plateTag.matches("^[0-9]{8}$");
    }
}
