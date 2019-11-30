package com.edi3.service.i.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.BusinessProcess;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.ProcessType;

import java.util.List;
import java.util.Map;

public interface BusinessProcessSequenceService {
    BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask);
    List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi);
    Map<BusinessProcess, List<BusinessProcessSequence>> getHistoryByDocumentMap(AbstractDocumentEdi abstractDocumentEdi);
    Map<ProcessType, List<BusinessProcessSequence>> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user);
}
