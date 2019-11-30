package com.edi3.service.i.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.ProcessType;

import java.util.List;
import java.util.Map;

public interface ExecutorTaskService {
    List<ExecutorTask> getReviewTask(User user, String sortingSequence, String filterString);
    List<ExecutorTask> getControlledTask(User user, String sortingSequence, String filterString);
    Map<ExecutorTask, List<UploadedFile>> getSignaturesByMap(AbstractDocumentEdi abstractDocumentEdi);
    List<ExecutorTask> getFilterByExecutorAndDocument(User executor, AbstractDocumentEdi documentEdi);
    List<ExecutorTask> getFilterByUserAndDocument(User user, AbstractDocumentEdi documentEdi);
    List<ExecutorTask> getFilterByExecutorDocumentAndProcessType(User executor, AbstractDocumentEdi documentEdi, ProcessType processType);
    List<ExecutorTask> getWithdrawAvailable(User executor, AbstractDocumentEdi documentEdi);
    void checkAndSetDeletionDependOnUser(User currentUser, ExecutorTask executorTask, boolean deletionMark);
    ExecutorTask getDraft(User author, AbstractDocumentEdi documentEdi);

    // dao
    ExecutorTask getById(Long id);
}
