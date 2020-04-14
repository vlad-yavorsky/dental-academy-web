package ua.kazo.dentalacademy.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class MathUtil {

    public BigDecimal calculateDiscountPrice(final BigDecimal price, final Byte discount) {
        if (discount == null) {
            return price;
        }
        return price.subtract(price.multiply(BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
