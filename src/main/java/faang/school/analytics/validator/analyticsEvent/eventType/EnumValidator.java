package faang.school.analytics.validator.analyticsEvent.eventType;

import faang.school.analytics.model.EventType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(EventType.values())
                .anyMatch(event -> event.name().equalsIgnoreCase(value));
    }
}
