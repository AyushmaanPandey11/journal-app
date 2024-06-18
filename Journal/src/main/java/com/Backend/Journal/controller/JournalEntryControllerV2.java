package com.Backend.Journal.controller;

import com.Backend.Journal.controller.entity.JournalEntry;
import com.Backend.Journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v2/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public String createEntry(@RequestBody JournalEntry userEntry){
        userEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(userEntry);
        return "Entry Save Successfully";
    }

    @GetMapping
    public List<JournalEntry> getAll()
    {
        return journalEntryService.getAll();
    }

    @GetMapping("/id/{userId}")
    public JournalEntry getInfoById(@PathVariable ObjectId userId ){
        return journalEntryService.findById(userId).orElse(null);
    }
    @DeleteMapping("/id/{userId}")
    public String deleteJournalEntryById(@PathVariable ObjectId userId){
        journalEntryService.deleteById(userId);
        return "User Deleted Successfully";
    }
    @PutMapping("/id/{userId}")
    public JournalEntry UpdateJournalEntryById( @PathVariable ObjectId userId, @RequestBody JournalEntry newEntry ){
        JournalEntry old = journalEntryService.findById((userId)).orElse(null);
        if(old != null)
        {
            old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return  old;
    }
}
