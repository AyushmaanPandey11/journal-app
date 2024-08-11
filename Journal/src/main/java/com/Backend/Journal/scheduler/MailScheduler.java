package com.Backend.Journal.scheduler;

import com.Backend.Journal.entity.JournalEntry;
import com.Backend.Journal.entity.User;
import com.Backend.Journal.enums.Sentiment;
import com.Backend.Journal.repository.UserRepositoryCriteria;
import com.Backend.Journal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MailScheduler {

    @Autowired
    private UserRepositoryCriteria userRepositoryCriteria;

    @Autowired
    private EmailService emailService;


    @Scheduled( cron = " 0 0 9 * * SUN")
    public void fetchUsersAndSendSAMail(){
        List<User> users = userRepositoryCriteria.getUserForSA();
        for( User user : users ){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x->x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCount = new HashMap<>();
            for( Sentiment sentiment : sentiments ){
                if( sentiment != null ){
                    sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment userSentiment = null;
            int maxCount=0;
            for (Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()){
                if(entry.getValue()> maxCount){
                    maxCount = entry.getValue();
                    userSentiment = entry.getKey();
                }
            }
            if(userSentiment!= null){
                emailService.sendEmail(user.getEmail(), "Sentiment for Last 7 Days", userSentiment.toString() );
            }
        }
    }
}
