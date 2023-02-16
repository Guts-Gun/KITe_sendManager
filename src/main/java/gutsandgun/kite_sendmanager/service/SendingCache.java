package gutsandgun.kite_sendmanager.service;

import gutsandgun.kite_sendmanager.dto.SendingEmailDTO;
import gutsandgun.kite_sendmanager.dto.SendingMsgDTO;
import gutsandgun.kite_sendmanager.entity.read.SendingEmail;
import gutsandgun.kite_sendmanager.entity.read.SendingMsg;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingEmailRepository;
import gutsandgun.kite_sendmanager.repository.read.ReadSendingMsgRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendingCache {

    @Autowired
    private final ReadSendingMsgRepository readSendingMsgRepository;

    @Autowired
    private final ReadSendingEmailRepository readSendingEmailRepository;


    @Cacheable(value="sendingMsg" , key = "#sendingId" ,cacheManager = "redisCacheManager")
    public List<SendingMsg> getSendingMsg(Long sendingId){
        List<SendingMsg> sendingMsgList = readSendingMsgRepository.findBySendingId(sendingId);
        return sendingMsgList;
    }

    @Cacheable(value="sendingEmail" , key = "#sendingId" ,cacheManager = "redisCacheManager")
    public  List<SendingEmail> getSendingEmail(Long sendingId){
        List<SendingEmail> sendingEmailList =  readSendingEmailRepository.findBySendingId(sendingId);
        return sendingEmailList;

    }

}

