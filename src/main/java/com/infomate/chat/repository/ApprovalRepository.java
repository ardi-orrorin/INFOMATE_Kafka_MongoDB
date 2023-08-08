package com.infomate.chat.repository;

import com.infomate.chat.entity.Approval;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApprovalRepository extends MongoRepository<Approval, ObjectId> {

}
