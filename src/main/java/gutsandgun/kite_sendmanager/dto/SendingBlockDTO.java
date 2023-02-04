package gutsandgun.kite_sendmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendingBlockDTO {
    private Long id;
    private String sender;
    private String receiver;
    private Long blockTime;
}
