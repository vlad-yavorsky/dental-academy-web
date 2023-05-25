package ua.kazo.dentalacademy.dto.offering;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OfferingUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private OfferingType type;
    @Positive
    private BigDecimal price;
    @Min(1)
    @Max(99)
    private Byte discount;
    @Positive
    private byte term;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime activated;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deactivated;
    @NotEmpty
    private List<Long> programs;
    private List<Long> bonuses;

}
