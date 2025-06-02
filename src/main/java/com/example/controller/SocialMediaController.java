package com.example.controller;
import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    ObjectMapper om = new ObjectMapper();

    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<String> createAccount(@RequestBody Account acc){
        if (acc.getUsername()=="" || acc.getPassword().length()<4){
            return ResponseEntity.status(400).body(null);
        }
        if(accountService.doesAccountWithUsernameExist(acc.getUsername())!=null){
            return ResponseEntity.status(409).body(null);
        }
        Account newAccount=accountService.createAccount(acc);
        try {
            return ResponseEntity.status(200).body(om.writeValueAsString(newAccount));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<String> login(@RequestBody Account acc){
        Account retrievedAcc=accountService.verifyAccount(acc.getUsername(),acc.getPassword());
        if (retrievedAcc!=null){
            try {
                return ResponseEntity.status(200).body(om.writeValueAsString(retrievedAcc));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(401).body(null);
            }

        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<String> createPost(@RequestBody Message msg){
        String text=msg.getMessageText();
        if (!text.isBlank() && text.length()<=255 && accountService.getAccountFromID(msg.getPostedBy())!=null){
            Message createdMsg=messageService.createMessage(msg);
            if (createdMsg!=null){
                try{
                    return ResponseEntity.status(200).body(om.writeValueAsString(createdMsg));
                }
                catch(JsonProcessingException e) {
                    return ResponseEntity.status(400).body(null);
                }
                
            }
        }
        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<String> getAllMessages(){
        try{
            return ResponseEntity.status(200).body(om.writeValueAsString(messageService.getAllMessages()));
        }
        catch(JsonProcessingException e) {
            return ResponseEntity.status(200).body(null);
        }
        
    }

    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<String> getMessageFromMessageID(@PathVariable int messageId){
        Message msg=messageService.getMessageFromMsgId(messageId);
        if (msg!=null){
            try{
                return ResponseEntity.status(200).body(om.writeValueAsString(msg));
            }
            catch(JsonProcessingException e) {
                return ResponseEntity.status(200).body("");
            }
        }
        return ResponseEntity.status(200).body("");
    }
    @DeleteMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<String> deleteMessageFromMessageID(@PathVariable int messageId){
        String str=new String();
        long initialRows=messageService.getNumberOfMessages();
        if (messageService.getMessageFromMsgId(messageId)!=null){
            messageService.deleteMessageFromMsgId(messageId);
            long afterRows=messageService.getNumberOfMessages();
            if (initialRows!=afterRows){
                str=String.valueOf(initialRows-afterRows);
            }
        }
        return ResponseEntity.status(200).body(str);
    }
    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<String> updateMessageById(@PathVariable int messageId,@RequestBody Message m){
        String txt=m.getMessageText();
        if (!txt.isBlank() && txt.length()<=255){
            Message msg=messageService.updateMessageFromMsgId(messageId, m.getMessageText());
            if (msg!=null){
                return ResponseEntity.status(200).body(String.valueOf(1));
            }
        }
    
        return ResponseEntity.status(400).body("");
    }

     /*
      As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.

- The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
      */

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<String> getAllMessagesFromAccountId(@PathVariable int accountId){
        List<Message> messages=messageService.getMessagesFromAccountId(accountId);
        System.out.println("messages is " + messages);
        try{
            return ResponseEntity.status(200).body(om.writeValueAsString(messages));
        }
        catch(JsonProcessingException e) {
            return ResponseEntity.status(200).body("");
        }
        
    }
}
