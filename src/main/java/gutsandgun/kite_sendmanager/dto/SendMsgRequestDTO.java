package gutsandgun.kite_sendmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgRequestDTO {

    List<Map<String,String>> receiverList;  // 수신자 리스트
    SendingDTO sendingDTO;
    String reservationYn;                   // 예약 여부
    String sender;                          // 발신자 전화번호
    Integer contentLength;                  // 메세지 길이
    List<SendingRuleDTO> brokerList;        // 중계사 비율 리스트

}

