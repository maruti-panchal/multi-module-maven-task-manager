package com.learnspring.repository;

import com.learnspring.entity.TaskEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final ReactiveMongoTemplate template;

    public TaskRepositoryCustomImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<TaskEntity> findByFilters(String username, Integer dueBefore, int offset, int limit) {
        Criteria criteria = new Criteria();
        if (username != null && !username.isEmpty()) {
            criteria = criteria.and("username").is(username);
        }
        if (dueBefore > 0) {
            criteria = criteria.and("dueDays").lt(dueBefore);
        }

        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.asc("dueDays")));
        if (offset > 0) query.skip(offset);
        if (limit > 0) query.limit(limit);

        return template.find(query, TaskEntity.class);
    }



    @Override
    public Mono<TaskEntity> findByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return Mono.empty();
        }
        Query q = Query.query(Criteria.where("title").regex(title, "i")).limit(1);
        return template.findOne(q, TaskEntity.class);
    }

    @Override
    public Flux<TaskEntity> findDueDateBetween(int min, int max) {
        Query q = Query.query(Criteria.where("dueDays").gte(min).lte(max))
                .with(Sort.by(Sort.Order.asc("dueDays")));
        return template.find(q, TaskEntity.class);
    }




}
