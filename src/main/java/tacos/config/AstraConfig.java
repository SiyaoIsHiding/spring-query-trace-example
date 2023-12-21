package tacos.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "datastax.astra")
@Getter
@Setter
@Component
public class AstraConfig {
    private File secureConnectBundle;
}