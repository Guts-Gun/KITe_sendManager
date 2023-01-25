package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService{

    @Autowired
    WriteSendingRepository writeSendingRepository;

    @Autowired
    private final ModelMapper mapper;

    @Override
    public Long insertSending(SendingDTO sendingDTO, String userId) {

        sendingDTO.setScheduleTime(sendingDTO.getReservationTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        sendingDTO.setInputTime(new Date().getTime());
        Sending sending = writeSendingRepository.save(mapper.map(sendingDTO, Sending.class));
        return sending.getId();
    }

    @Override
    public void startSending(Long sendingId) {

    }


}
