package es.omarall.restical;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import net.fortuna.ical4j.util.UidGenerator;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public UidGenerator uidGenerator() throws Exception {
        return new UidGenerator(UUID.randomUUID().toString());
    }
}
