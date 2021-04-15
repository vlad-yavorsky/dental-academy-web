package ua.kazo.dentalacademy.dto.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderHistoryResponseDto {

    private LocalDateTime created;
    private UnifiedPaymentStatus status;
    private String data;

    @SneakyThrows
    public void setData(String jsonString) {
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        this.data = new YAMLMapper().writeValueAsString(jsonNodeTree)
                .substring(4)
                .replace("\n", "<br/>");
    }

}
