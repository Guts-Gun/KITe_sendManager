package gutsandgun.kite_sendmanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendManagerConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
