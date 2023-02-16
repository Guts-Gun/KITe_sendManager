package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.*;
import gutsandgun.kite_sendmanager.entity.read.Broker;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.publisher.RabbitMQProducer;
import gutsandgun.kite_sendmanager.repository.read.ReadBrokerRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import gutsandgun.kite_sendmanager.type.SendingType;
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
    private final ReadBrokerRepository readBrokerRepository;

    @Autowired
    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    private final RabbitMQService rabbitMQService;

    @Autowired
    private final SendingCache sendingCache;

    @Autowired
    private final ModelMapper mapper;



    @Override
    public List<SendingMsgDTO> getSendMsgList(Long sendingId){
        List<SendingMsg> sendingMsgList =  sendingCache.getSendingMsg(sendingId);
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

        Map<Long, List<SendingMsgDTO>> returnMap = new HashMap<Long, List<SendingMsgDTO>>();

        int totalMsgCount = sendingMsgDTOList.size();   // 전체 메세지 갯수
        AtomicInteger startCnt = new AtomicInteger();   // 분배 리스트 시작 인덱스

        sendingRuleDTOList.forEach(sendingRuleDTO -> {
            if(sendingRuleDTO.getWeight() >0){

                Double percent = (double) sendingRuleDTO.getWeight().intValue() / (double) 100;
                int sendCnt = (int) Math.round(percent * totalMsgCount);

                // 분배 리스트 종료 인덱스
                int end = sendCnt + startCnt.get();

                returnMap.put(sendingRuleDTO.getBrokerId(), new ArrayList<>( sendingMsgDTOList.subList(startCnt.get(), end)));
                startCnt.set(end);
            }
        });

        log.info("distributeMessageCustom==========================================================");
        log.info(returnMap);
        log.info("distributeMessageCustom==========================================================");

        produceQueue(sendingDTO, returnMap);
    }

    @Override
    public void distributeMessageSpeed(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList) {

        List<Map<String, Integer>> brokerRuleList = new ArrayList<Map<String, Integer>>();
        AtomicInteger totalRate = new AtomicInteger();      // 전체 비율

        List<BrokerDTO> brokerDTOList = null;

        SendingType sendingType = sendingDTO.getSendingType();
        if(sendingType.equals(SendingType.SMS) || sendingType.equals(SendingType.MMS)){
            brokerDTOList = getMsgBrokerList(); // 중계사 리스트
        }else if(sendingType.equals(SendingType.EMAIL)){
            brokerDTOList = getEmailBrokerList(); // 중계사 리스트
        }

        brokerDTOList.forEach(brokerDTO -> {

            Map<String, Object> queueInfo = rabbitMQService.getQueueInfo(brokerDTO.getName());
            Integer queueSize = (Integer) queueInfo.get("messages_ready"); // 큐 적재량
            Float latency = brokerDTO.getLatency(); // 중계사 지연율

            int  rate = Math.round(1 / ((queueSize ==0? 1: queueSize)*(latency ==0? 1: latency))*100); // 1/큐적재량*지연율*100

            Map<String, Integer> map = new HashMap<>();
            map.put("brokerId", brokerDTO.getId().intValue());
            map.put("rate", rate);
            brokerRuleList.add(map);
            totalRate.addAndGet(rate);
        });

        AtomicInteger startCnt = new AtomicInteger();   // 분배 리스트 시작 인덱스
        int totalMsgCount = sendingMsgDTOList.size();      // 전체 메세지 갯수

        Map<Long, List<SendingMsgDTO>> returnMap = new HashMap<>();
        brokerRuleList.forEach(brokerRule -> {

            float percent = (brokerRule.get("rate").intValue() * 100.0f) / (totalRate.get()*100);
            int sendCnt = (int) Math.round(percent * totalMsgCount);

            // 분배 리스트 종료 인덱스
            int end = sendCnt + startCnt.get();
            Long BrokerId = Long.valueOf(brokerRule.get("brokerId"));
            returnMap.put(BrokerId, new ArrayList<>( sendingMsgDTOList.subList(startCnt.get(), end)));

            startCnt.set(end);
        });

        produceQueue(sendingDTO, returnMap);
    }

    @Override
    public void distributeMessagePrice(SendingDTO sendingDTO, List<SendingMsgDTO> sendingMsgDTOList) {
        List<BrokerDTO> brokerDTOList = null;
        SendingType sendingType = sendingDTO.getSendingType();
        if(sendingType.equals(SendingType.SMS) || sendingType.equals(SendingType.MMS)){
            brokerDTOList = getMsgBrokerList(); // 중계사 리스트
        }else if(sendingType.equals(SendingType.EMAIL)){
            brokerDTOList = getEmailBrokerList(); // 중계사 리스트
        }

        BrokerDTO broker = brokerDTOList.stream().min((x, y) -> (int) (x.getPrice() - y.getPrice())).orElse(new BrokerDTO());

        Map<Long, List<SendingMsgDTO>> returnMap = new HashMap<>();
        returnMap.put(broker.getId(), sendingMsgDTOList);
        produceQueue(sendingDTO, returnMap);
    }


    public void produceQueue(SendingDTO sendingDTO, Map<Long, List<SendingMsgDTO>> map){

        log.info("produceQueue==========================================================");
        log.info(map);
        log.info("produceQueue==========================================================");

        // SKT
        List<SendingMsgDTO> broker1SendingMsgDTOList = map.get(1L);
        if(broker1SendingMsgDTOList != null){
            broker1SendingMsgDTOList.forEach(sendingMsgDTO -> {
                rabbitMQProducer.sendQueue1Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
            });
        }

        // KT
        List<SendingMsgDTO> broker2SendingMsgDTOList = map.get(2l);
        if(broker2SendingMsgDTOList != null) {
            broker2SendingMsgDTOList.forEach(sendingMsgDTO -> {
                rabbitMQProducer.sendQueue2Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
            });
        }

        // LG
        List<SendingMsgDTO> broker3SendingMsgDTOList = map.get(3L);
        if (broker3SendingMsgDTOList != null){
            broker3SendingMsgDTOList.forEach(sendingMsgDTO -> {
                rabbitMQProducer.sendQueue3Message(sendingMsgDTO, sendingDTO.getId(), sendingDTO.getSendingType());
            });
        }

        List<SendingMsgDTO> broker4SendingMsgDTOList = map.get(4L);
        if(broker4SendingMsgDTOList != null){
            broker4SendingMsgDTOList.forEach(sendingMsgDTO -> {
                SendingEmailDTO sendingEmailDTO = new SendingEmailDTO();
                sendingEmailDTO.setSendingId(sendingMsgDTO.getSendingId());
                sendingEmailDTO.setReceiver(sendingMsgDTO.getReceiver());
                sendingEmailDTO.setId(sendingMsgDTO.getId());
                sendingEmailDTO.setName(sendingMsgDTO.getName());
                sendingEmailDTO.setSender(sendingMsgDTO.getSender());
                sendingEmailDTO.setRegId(sendingMsgDTO.getRegId());
                rabbitMQProducer.sendEmailQueue1Message(sendingEmailDTO, sendingDTO.getId(), sendingDTO.getSendingType());
            });
        }


        List<SendingMsgDTO> broker5SendingMsgDTOList = map.get(5L);
        if(broker5SendingMsgDTOList != null){
            broker5SendingMsgDTOList.forEach(sendingMsgDTO -> {
                SendingEmailDTO sendingEmailDTO = new SendingEmailDTO();
                sendingEmailDTO.setSendingId(sendingMsgDTO.getSendingId());
                sendingEmailDTO.setReceiver(sendingMsgDTO.getReceiver());
                sendingEmailDTO.setId(sendingMsgDTO.getId());
                sendingEmailDTO.setName(sendingMsgDTO.getName());
                sendingEmailDTO.setSender(sendingMsgDTO.getSender());
                sendingEmailDTO.setRegId(sendingMsgDTO.getRegId());
                rabbitMQProducer.sendEmailQueue2Message(sendingEmailDTO, sendingDTO.getId(), sendingDTO.getSendingType());
            });
        }

    }


    public List<BrokerDTO> getMsgBrokerList(){
        List<Broker> BrokerList = readBrokerRepository.findBySendingType(SendingType.SMS);
        List<BrokerDTO> brokerDTOList = new ArrayList<>();
        BrokerList.forEach(broker -> {
            brokerDTOList.add(mapper.map(broker,BrokerDTO.class));
        });

        return brokerDTOList;
    }

    public List<BrokerDTO> getEmailBrokerList(){
        List<Broker> BrokerList = readBrokerRepository.findBySendingType(SendingType.EMAIL);
        List<BrokerDTO> brokerDTOList = new ArrayList<>();
        BrokerList.forEach(broker -> {
            brokerDTOList.add(mapper.map(broker,BrokerDTO.class));
        });

        return brokerDTOList;
    }



}
