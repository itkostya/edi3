package com.edi3.dao.i.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.User;
import com.edi3.dao.i.HibernateDAO;

import java.util.List;

public interface BusinessProcessSequenceDao extends HibernateDAO<BusinessProcessSequence> {
    List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi);
    List<BusinessProcessSequence> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user);
    BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask);
}
