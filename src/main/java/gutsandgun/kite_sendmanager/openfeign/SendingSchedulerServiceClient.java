package gutsandgun.kite_sendmanager.openfeign;


import gutsandgun.kite_sendmanager.dto.SendingScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "sending-scheduler-client", url = "${feign.url.sending-scheduler}", configuration = FeignConfig.class)
public interface SendingSchedulerServiceClient {
	/**
	 * 스케쥴 추가
	 *
	 * @param sendingScheduleDto Schedule info = {Long sendingId, Long time(UTC)}
	 * @return
	 */
	@PostMapping("/scheduler/add")
	String addSchedule(@RequestBody SendingScheduleDto sendingScheduleDto);

	/**
	 * 스케쥴 삭제
	 *
	 * @param sendingScheduleDto Schedule info = {Long sendingId, Long time(UTC, 의미없음)}
	 * @return
	 */
	@DeleteMapping("/scheduler/remove")
	String removeSchedule(@RequestBody SendingScheduleDto sendingScheduleDto);

	/**
	 * 스케쥴 변경
	 *
	 * @param sendingScheduleDto Schedule info = {Long sendingId, Long time(UTC)}
	 * @return
	 */
	@PostMapping("/scheduler/update")
	String updateSchedule(@RequestBody SendingScheduleDto sendingScheduleDto);

}