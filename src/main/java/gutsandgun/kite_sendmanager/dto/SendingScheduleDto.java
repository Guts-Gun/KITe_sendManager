package gutsandgun.kite_sendmanager.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendingScheduleDto implements Serializable {
	private Long sendingId;
	private Long time;
}