package ua.kazo.dentalacademy.dto.offering;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OfferingCreateDto {

    @NotBlank
    private String name;
    private String description;
    @NotNull
    private OfferingType type;
    @Positive
    private BigDecimal price;
    @Positive
    private byte term;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime activated;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deactivated;
    @NotEmpty
    private List<Long> programs = new ArrayList<>();
    private List<Long> folders = new ArrayList<>();

}
