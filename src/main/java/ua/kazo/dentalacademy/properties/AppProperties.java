package ua.kazo.dentalacademy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
    @NestedConfigurationProperty
    private PaymentProperties payment;
    @NestedConfigurationProperty
    private StorageProperties storage;
    @NestedConfigurationProperty
    private AWSProperties aws;

}
