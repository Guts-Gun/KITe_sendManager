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

    private String userId;

    private Long sendingId;

    private Long brokerId;

    private Long weight;

    private String regId;

    private String modId;

}