package ir.co.sadad.cartollapi.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(PlateTag.List.class)
@Documented
//@NotBlank(message = "{plate.tag.must.not.be.null}")
@Constraint(validatedBy = {PlateTagValidator.class})
public @interface PlateTag {

    String message() default "plate.tag.pattern.not.valid";

    String messageNotBlank() default "plate.tag.must.not.be.null";

//    boolean notBlank() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "";

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PlateTag[] value();
    }
}