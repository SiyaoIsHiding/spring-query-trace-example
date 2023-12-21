package tacos.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "datastax.astra")
@Getter
@Setter
public class AstraConfig {
    private File secureConnectBundle;
}