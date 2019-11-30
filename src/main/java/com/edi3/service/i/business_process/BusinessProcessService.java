package com.edi3.service.i.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.ProcessType;

import java.sql.Timestamp;

public interface BusinessProcessService {
    void createAndStartBusinessProcess(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask draftExecutorTask, java.sql.Timestamp timeStamp,
                                       String[] usersIdArray, String[] orderTypeArray, String[] processTypeArray,
                                       ProcessType processTypeCommon, String comment, java.sql.Timestamp finalDate);
    ExecutorTask createDraftExecutorTask(User currentUser, AbstractDocumentEdi documentEdi, Timestamp timeStamp, Timestamp finalDate);
    void stopBusinessProcessSequence(AbstractDocumentEdi documentEdi, Long[] businessProcessSequenceArrayId, ExecutorTask currentExecutorTask);
    void withdrawExecutorTasks(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask currentExecutorTask);
}
