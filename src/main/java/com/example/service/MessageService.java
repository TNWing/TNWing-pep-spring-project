package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public Message createMessage(Message msg){
        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageFromMsgId(int id){
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessageFromMsgId(int id){
        messageRepository.deleteById(id);
    }

    public long getNumberOfMessages(){
        
        return messageRepository.count();
    }

    public Message updateMessageFromMsgId(int id,String text){
        Optional<Message> optional=messageRepository.findById(id);
        if (optional.isPresent()){
            Message msg=optional.get();
            msg.setMessageText(text);
            return messageRepository.save(msg); 
        }
        return null;

    }

    public List<Message> getMessagesFromAccountId(int id){
        return messageRepository.findMessagesByPostedBy(id);
    }
}
