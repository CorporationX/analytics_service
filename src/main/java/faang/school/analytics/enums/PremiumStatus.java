package faang.school.analytics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PremiumStatus {
    PURCHASED("Purchesed"),
    FAILED("Failed"),
    REFUNDED("Refunded");

    private String value;
}

