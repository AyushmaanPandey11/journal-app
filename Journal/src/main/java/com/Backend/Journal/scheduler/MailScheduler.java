package com.Backend.Journal.scheduler;

import com.Backend.Journal.entity.JournalEntry;
import com.Backend.Journal.entity.User;
import com.Backend.Journal.repository.UserRepositoryCriteria;
import com.Backend.Journal.service.EmailService;
import com.Backend.Journal.service.SentimentAnalysisService;
import org.jetbrains.annotations.ApiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MailScheduler {

    @Autowired
    private UserRepositoryCriteria userRepositoryCriteria;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled( cron = " 0 0 9 * * SUN")
    public void fetchUsersAndSendSAMail(){
        List<User> users = userRepositoryCriteria.getUserForSA();
        for( User user : users ){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> entries = journalEntries.stream().filter(x->x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getContent()).collect(Collectors.toList());
            String input = String.join(" ", entries);
            String sentiment = sentimentAnalysisService.getSentiment(input);
            emailService.sendEmail(user.getEmail(), "Sentiments for Last 7 Days", sentiment);
        }
    }
}
