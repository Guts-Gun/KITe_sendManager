package gutsandgun.kite_sendmanager.dto;

import gutsandgun.kite_sendmanager.type.SendingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrokerDTO {

    private Long id;
    private String name;
    private String ip;
    private String color;
    private SendingType sendingType;
    private Float price;
    private Float latency;
    private Float failureRate;

}
