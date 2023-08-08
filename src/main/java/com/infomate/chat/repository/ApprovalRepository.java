package com.infomate.chat.repository;

import com.infomate.chat.dto.ApprovalDTO;
import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Approval;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ApprovalRepository extends ReactiveMongoRepository<Approval, ObjectId> {

}
