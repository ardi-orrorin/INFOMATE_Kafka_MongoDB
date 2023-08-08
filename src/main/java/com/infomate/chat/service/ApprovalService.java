package com.infomate.chat.service;

import com.infomate.chat.dto.ApprovalDTO;
import com.infomate.chat.entity.Approval;
import com.infomate.chat.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovalService {
    private final ApprovalRepository approvalRepository;

    private final ModelMapper modelMapper;

    public void insertApproval(ApprovalDTO approvalDTO){
        Approval approval = modelMapper.map(approvalDTO, Approval.class);

        approvalRepository.insert(approval);
    }
}
