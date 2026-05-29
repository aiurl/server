package io.theurl.config;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ConfigController {
    private final MongoTemplate mongo;
    private final String collectionName;

    public ConfigController(MongoTemplate mongo, @Value("${spring.cloud.config.server.mongodb.collection:properties}") String collectionName) {
        this.mongo = mongo;
        this.collectionName = collectionName;
    }

    @PostMapping("{application}/{profile}")
    public void update(@PathVariable String application, @PathVariable String profile, @RequestBody Document payload) {

        if (application == null || application.isEmpty()) {
            throw new IllegalArgumentException("application cannot be null or empty");
        }

        if (profile == null || profile.isEmpty()) {
            throw new IllegalArgumentException("profile cannot be null or empty");
        }

        var query = new Query();
        query.addCriteria(Criteria.where("application").is(application)
                                  .and("profile").is(profile));
        var update = new Update();
        update.set("version", System.currentTimeMillis());
        update.set("properties", payload);
        mongo.upsert(query, update, collectionName);
    }

    @GetMapping("{application}/{profile}")
    public Object query(@PathVariable String application, @PathVariable String profile) {
        var query = new Query();
        query.addCriteria(Criteria.where("application").is(application).and("profile").is(profile));
        var document = mongo.find(query, Document.class, collectionName).stream().findFirst().orElse(null);
        if (document == null) {
            return null;
        }

        return document.get("properties");
    }
}
