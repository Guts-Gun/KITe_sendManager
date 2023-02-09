package gutsandgun.kite_sendmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitMQService {


    @Value("${spring.rabbitmq.host}")
    private String HOST;

    @Value("${rabbitmq.api.port}")
    private String PORT;

    @Value("${rabbitmq.api.queue-detail}")
    private String URL;

    @Value("${spring.rabbitmq.virtual-host}")
    private String VHOST;

    @Value("${rabbitmq.api.auth}")
    private String AUTH;

    @Autowired
    private final ApiService apiService;




    public Map<String, Object> getQueueInfo(String queueName) {
        String host = HOST;
        String port = PORT;
        String url = URL;
        String vhost = VHOST;
        String auth = AUTH;

        String BASIC_URL = "https://" + host + ":" + port + url + vhost + "/"+ queueName;
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(BASIC_URL).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(auth);

        Object queueInfo = apiService.get(builder.toString(),headers).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(queueInfo, Map.class);

        return map;


    }




}

