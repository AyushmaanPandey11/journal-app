package com.Backend.Journal.repository;

import com.Backend.Journal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryCriteria {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(
                criteria.andOperator(
                        Criteria.where("email").regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
                        ),
                Criteria.where("sentimentalAnalysis").is(true))
        );
        return mongoTemplate.find(query,User.class);
    }

}
