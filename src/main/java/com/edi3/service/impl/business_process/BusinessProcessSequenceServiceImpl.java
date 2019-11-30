package com.edi3.service.impl.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.BusinessProcess;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.dao.i.business_processes.BusinessProcessSequenceDao;
import com.edi3.service.i.business_process.BusinessProcessSequenceService;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BusinessProcessSequenceServiceImpl implements BusinessProcessSequenceService {

    private BusinessProcessSequenceDao businessProcessSequenceDao;

    public BusinessProcessSequenceDao getBusinessProcessSequenceDao() {
        return businessProcessSequenceDao;
    }

    public void setBusinessProcessSequenceDao(BusinessProcessSequenceDao businessProcessSequenceDao) {
        this.businessProcessSequenceDao = businessProcessSequenceDao;
    }

    public BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask) {
        return businessProcessSequenceDao.getBusinessProcessSequence(executorTask);
    }

    public List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi) {
        return businessProcessSequenceDao.getHistoryByDocumentList(abstractDocumentEdi);
    }

    public Map<BusinessProcess, List<BusinessProcessSequence>> getHistoryByDocumentMap(AbstractDocumentEdi abstractDocumentEdi) {

        LinkedHashMap<BusinessProcess, List<BusinessProcessSequence>> linkedHashMap =
                businessProcessSequenceDao.getHistoryByDocumentList(abstractDocumentEdi).stream()
                        .collect(Collectors.groupingBy(
                                BusinessProcessSequence::getBusinessProcess,
                                LinkedHashMap::new,
                                Collectors.toList()));

        //noinspection ResultOfMethodCallIgnored
        linkedHashMap.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().getDate())).map(Map.Entry::getKey).collect(Collectors.toList());

        return linkedHashMap;
    }

    public Map<ProcessType, List<BusinessProcessSequence>> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user) {

        return businessProcessSequenceDao.getNotCompletedSequenceByDocumentAndUser(abstractDocumentEdi, user).stream()
                .collect(Collectors.groupingBy(
                        BusinessProcessSequence::getProcessType,
                        LinkedHashMap::new,
                        Collectors.toList()));

    }
}
