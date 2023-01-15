package gutsandgun.kite_sendmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendingRuleDTO {

    private Long userId;
    private Long sendingId;
    private Long brokerId;
    private Long weight;

}