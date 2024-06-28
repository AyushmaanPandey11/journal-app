package com.Backend.Journal.controller;

import com.Backend.Journal.entity.JournalEntry;
import com.Backend.Journal.entity.User;
import com.Backend.Journal.service.JournalEntryService;
import com.Backend.Journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v2/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry userEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(userEntry,userName);
            return new ResponseEntity<>(userEntry,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser()
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName= authentication.getName();
            User user = userService.findByUserName(userName);
            List<JournalEntry> data = user.getJournalEntries();
            if(data != null && !data.isEmpty())
                return new ResponseEntity<>(data, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    @GetMapping("/id/{journalId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId journalId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x->x.getId().equals(journalId)).toList();
        if(!collect.isEmpty())
        {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);
            if(journalEntry.isPresent()){
                return  new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/id/{userId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean isDeleted = journalEntryService.deleteById(userId,userName);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }
    @PutMapping("/id/{journalId}")
    public ResponseEntity<?> UpdateJournalEntryById(@PathVariable ObjectId journalId,@RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x->x.getId().equals(journalId)).toList();
        if(!collect.isEmpty()){
            JournalEntry old = journalEntryService.findById((journalId)).orElse(null);
            if(old != null)
            {
                old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
