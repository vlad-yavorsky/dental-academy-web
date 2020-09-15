package ua.kazo.dentalacademy.config.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WayForPayProperties {

    private String merchantLogin;
    private String merchantSecretKey;

}
