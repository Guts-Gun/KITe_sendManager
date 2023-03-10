package gutsandgun.kite_sendmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.openfeign.SendingSchedulerServiceClient;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@RequiredArgsConstructor
@Log4j2
public class SendingServiceImpl implements SendingService{

    @Autowired
    private final WriteSendingRepository writeSendingRepository;

    @Autowired
    private final SendingSchedulerServiceClient sendingSchedulerServiceClient;

    @Autowired
    private final ModelMapper mapper;

    @Autowired
    private final SendingCache sendingCache;

    @Override
    public Long insertSending(SendingDTO sendingDTO, String userId) throws JsonProcessingException {

        LocalDateTime reservDateTime = sendingDTO.getReservationTime();

        if (reservDateTime != null){
            sendingDTO.setScheduleTime(reservDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());
        }

        Sending sending = writeSendingRepository.save(mapper.map(sendingDTO, Sending.class));
        Long sendingId = sending.getId();
        sendingCache.insertSending(sendingId, mapper.map(sending, SendingDTO.class));
        log.info("!!Sending Cache Check: {}",sendingCache.getSendingDto(sendingId));
        if (reservDateTime != null) {
            SendingScheduleDto sendingScheduleDto = new SendingScheduleDto();
            sendingScheduleDto.setSendingId(sendingId);
            sendingScheduleDto.setTime(sending.getScheduleTime());
            sendingSchedulerServiceClient.addSchedule(sendingScheduleDto);
        }
        return sendingId;
    }

    @Override
    public SendingDTO getSending(Long sendingId) {
       Sending sending =  writeSendingRepository.findById(sendingId).get();
        return mapper.map(sending, SendingDTO.class);
    }

}
