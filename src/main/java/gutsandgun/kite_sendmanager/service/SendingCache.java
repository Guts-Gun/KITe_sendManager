package gutsandgun.kite_sendmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gutsandgun.kite_sendmanager.dto.SendingDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.entity.read.SendReplace;
import gutsandgun.kite_sendmanager.entity.read.Sending;
import gutsandgun.kite_sendmanager.entity.read.SendingEmail;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingEmailRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingReplaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SendingCache {

    @Autowired
    private final ReadSendingMsgRepository readSendingMsgRepository;

    @Autowired
    private final ReadSendingEmailRepository readSendingEmailRepository;

    @Autowired
    private final ReadSendingReplaceRepository readSendingReplaceRepository;

    @Autowired
    private final ModelMapper mapper;

    ObjectMapper objectMapper = new ObjectMapper();


    @Cacheable(value="sending" , key = "#sendingId" ,cacheManager = "CacheManager")
    public String insertSending(Long sendingId, SendingDTO sendingDTO) throws JsonProcessingException {
        log.info("==================================================");
        log.info("Cacheable" + sendingDTO );
        log.info("==================================================");
        String sendingDtoStr = objectMapper.writeValueAsString(sendingDTO);
        return sendingDtoStr;
    }
    @Cacheable(value="sending" , key = "#sendingId" ,cacheManager = "CacheManager")
    public String getSendingDto(Long sendingId) {
        return "NONMO";
    }


    @Cacheable(value="sendingMsg" , key = "#sendingId" ,cacheManager = "CacheManager")
    public List<String> getSendingMsgDTOList(Long sendingId) throws JsonProcessingException {
        List<SendingMsg> sendingMsgList = readSendingMsgRepository.findBySendingId(sendingId);
        List<String> list = new ArrayList<>();
        sendingMsgList.forEach(SendingMsg -> {
            try {
                list.add(objectMapper.writeValueAsString(mapper.map(SendingMsg,SendingMsgDTO.class)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return list;
    }

    @Cacheable(value="sendingEmail" , key = "#sendingId" ,cacheManager = "CacheManager")
    public  List<String> getSendingEmailDTOList(Long sendingId){
        List<SendingEmail> sendingEmailList =  readSendingEmailRepository.findBySendingId(sendingId);
        List<String> list = new ArrayList<>();
        sendingEmailList.forEach(sendingEmail -> {
            try {
                list.add(objectMapper.writeValueAsString(mapper.map(sendingEmail,SendingMsgDTO.class)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return list;

    }

    @Cacheable(value = "sendReplaceId", key = "#Id", cacheManager = "CacheManager")
    public SendReplace getSendReplaceInfo(Long Id){
        return readSendingReplaceRepository.findById(Id).get();
    }

}



