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

    public BigDecimal calculateDiscount(final BigDecimal price, final BigDecimal discountPrice) {
        if (price.equals(discountPrice)) {
            return BigDecimal.ZERO;
        }
        BigDecimal hundred = BigDecimal.valueOf(100);
        return hundred.subtract(discountPrice.divide(price, 2, RoundingMode.HALF_UP).multiply(hundred))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
