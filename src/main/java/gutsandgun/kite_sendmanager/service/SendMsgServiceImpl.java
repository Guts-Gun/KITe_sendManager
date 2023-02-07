package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.dto.SendingRuleDTO;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
@Log4j2
public class SendMsgServiceImpl implements SendMsgService {

    @Autowired
    private final ReadSendingMsgRepository readSendingMsgRepository;

    @Autowired
    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    private final ModelMapper mapper;



    @Override
    public List<SendingMsgDTO> getSendMsgList(Long sendingId){
        List<SendingMsg> sendingMsgList =  readSendingMsgRepository.findBySendingId(sendingId);
        List<SendingMsgDTO> sendingMsgDTOList = new ArrayList<>();
        sendingMsgList.forEach(SendingMsg -> {
            sendingMsgDTOList.add(mapper.map(SendingMsg,SendingMsgDTO.class));
        });
        return sendingMsgDTOList;
    }

    @Override
    public SendingMsgDTO getSendMsg(Long sendingId, Long txId) {
        return mapper.map(readSendingMsgRepository.findBySendingIdAndId(sendingId, txId), SendingMsgDTO.class);
    }

    @Override
    public void distributeMessageCustom(SendingDTO sendingDTO, List<SendingRuleDTO> sendingRuleDTOList, List<SendingMsgDTO> sendingMsgDTOList) {

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

        produceQueue(sendingDTO, returnList);
    }

    @Override
    public void distributeMessageSpeed(List<SendingMsgDTO> sendingMsgDTOList) {
    }

    @Override
    public void distributeMessagePrice(List<SendingMsgDTO> sendingMsgDTOList) {
    }


    public void produceQueue(SendingDTO sendingDTO, List<Map<Long, List<SendingMsgDTO>>> list){

        // MQ produce
        list.forEach(listMap ->{
            // SKT
            List<SendingMsgDTO> broker1SendingMsgDTOList = listMap.get(1L);
            if(broker1SendingMsgDTOList != null){
                broker1SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingDTO.getId() + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 1, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue1Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

            // KT
            List<SendingMsgDTO> broker2SendingMsgDTOList = listMap.get(2l);
            if(broker2SendingMsgDTOList != null) {
                broker2SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingDTO.getId() + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 2, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue2Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

            // LG
            List<SendingMsgDTO> broker3SendingMsgDTOList = listMap.get(3L);
            if (broker3SendingMsgDTOList != null){
                broker3SendingMsgDTOList.forEach(sendingMsgDTO -> {
                    log.info("Service: sendingManager, type: pushQueue" + ", sendingId: " + sendingDTO.getId() + ", sendingType: " + sendingDTO.getSendingType().toString() + ", brokerId: 3, TXId: " + sendingMsgDTO.getId() + ", time: " + new Date().getTime());
                    rabbitMQProducer.sendQueue3Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
                });
            }

        });

    }
}
