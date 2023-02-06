package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.dto.SendingScheduleDto;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.entity.write.Sending;
import gutsandgun.kite_sendmanager.openfeign.SendingSchedulerServiceClient;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import gutsandgun.kite_sendmanager.repository.write.WriteSendingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService{

    @Autowired
    WriteSendingRepository writeSendingRepository;

    @Autowired
    ReadSendingMsgRepository readSendingMsgRepository;


    @Autowired
    private final SendingSchedulerServiceClient sendingSchedulerServiceClient;


    @Autowired
    private final ModelMapper mapper;


    @Override
    public Long insertSending(SendingDTO sendingDTO, String userId) {

        LocalDateTime reservDateTime = sendingDTO.getReservationTime();

        if (reservDateTime != null){
            sendingDTO.setScheduleTime(reservDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        Sending sending = writeSendingRepository.save(mapper.map(sendingDTO, Sending.class));
        Long sendingId = sending.getId();

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


    public List<SendingMsgDTO> getSendMsgList(Long sendingId){
        List<SendingMsg> sendingMsgList =  readSendingMsgRepository.findBySendingId(sendingId);
        List<SendingMsgDTO> sendingMsgDTOList = new ArrayList<>();
        sendingMsgList.forEach(SendingMsg -> {
            sendingMsgDTOList.add(mapper.map(SendingMsg,SendingMsgDTO.class));
        });
        return sendingMsgDTOList;
    }

    @Override
    public List<Map<Long, List<SendingMsgDTO>>> distributeMessageCustom(List<SendingRuleDTO> sendingRuleDTOList, List<SendingMsgDTO> sendingMsgDTOList) {

        List<Map<Long, List<SendingMsgDTO>>> returnList = new ArrayList<>();

        int totalMsgCount = sendingMsgDTOList.size();   // 전체 메세지 갯수
        AtomicInteger startCnt = new AtomicInteger();   // 분배 리스트 시작 인덱스

        sendingRuleDTOList.forEach(sendingRuleDTO -> {
            if(sendingRuleDTO.getWeight() >0){

                Double percent = (double) sendingRuleDTO.getWeight().intValue() / (double) 100;
                int sendCnt = (int) Math.round(percent * totalMsgCount);

                // 분배 리스트 종료 인덱스
                int end = sendCnt + startCnt.get();

                Map<Long, List<SendingMsgDTO>> map = new HashMap<>();
                map.put(sendingRuleDTO.getBrokerId(), new ArrayList<>( sendingMsgDTOList.subList(startCnt.get(), end)));
                returnList.add(map);

                startCnt.set(end);
            }
        });

        return returnList;
    }

    @Override
    public List<Map<Long, List<SendingMsgDTO>>> distributeMessageSpeed(List<SendingMsgDTO> sendingMsgDTOList) {
        return null;
    }

    @Override
    public List<Map<Long, List<SendingMsgDTO>>> distributeMessagePrice(List<SendingMsgDTO> sendingMsgDTOList) {
        return null;
    }
}
