package com.Backend.Journal.controller;

import com.Backend.Journal.controller.entity.JournalEntry;
import com.Backend.Journal.controller.entity.User;
import com.Backend.Journal.service.JournalEntryService;
import com.Backend.Journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v2/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry userEntry,@PathVariable String userName){
        try {
            journalEntryService.saveEntry(userEntry,userName);
            return new ResponseEntity<>(userEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllEntriesOfUser(@PathVariable String userName)
    {
        User user  = userService.findByUserName(userName);
        List<JournalEntry> Data = user.getJournalEntries();
        if(Data != null && !Data.isEmpty()) {
            return new ResponseEntity<>(Data,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<JournalEntry> getInfoById(@PathVariable ObjectId userId ){
         Optional<JournalEntry> journalEntry = journalEntryService.findById(userId);
         if(journalEntry.isPresent()){
             return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{userId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId userId,@PathVariable String userName){
        journalEntryService.deleteById(userId,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{userName}/{userId}")
    public ResponseEntity<?> UpdateJournalEntryById(
            @PathVariable ObjectId userId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String userName){
        JournalEntry old = journalEntryService.findById((userId)).orElse(null);
        if(old != null)
        {
            old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return  new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
