package com.Backend.Journal.service;


import com.Backend.Journal.entity.JournalEntry;
import com.Backend.Journal.entity.User;
import com.Backend.Journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try{
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }catch(Exception e){
            throw new RuntimeException("Error of Replication",e);
        }
    }
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAll(){

        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){

        return journalEntryRepository.findById(id);
    }
    @Transactional
    public boolean deleteById(ObjectId entryId, String userName){
        boolean removed = false;
        try{
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(entryId));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(entryId);
            }
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("Error occurred while deleting entry: ",e);
        }
        return removed;
    }
}
