package gutsandgun.kite_sendmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KiTeSendManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiTeSendManagerApplication.class, args);
    }

}
