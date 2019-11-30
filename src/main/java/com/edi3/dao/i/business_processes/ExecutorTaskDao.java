package com.edi3.dao.i.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.dao.i.HibernateDAO;

import java.util.HashMap;
import java.util.List;

public interface ExecutorTaskDao extends HibernateDAO<ExecutorTask> {
    ExecutorTask getById(Long id);
    List<ExecutorTask> getReviewTask(User executor, String filterString);
    List<ExecutorTask> getControlledTask(User author, String filterString);
    List<ExecutorTask> getFilterByExecutorAndDocument(User executor, AbstractDocumentEdi documentEdi);
    List<ExecutorTask> getFilterByUserAndDocument(User user, AbstractDocumentEdi documentEdi);
    List<ExecutorTask> getFilterByExecutorDocumentAndProcessType(User executor, AbstractDocumentEdi documentEdi, ProcessType processType);
    List<ExecutorTask> getWithdrawAvailable(User executor, AbstractDocumentEdi documentEdi);
    HashMap<ExecutorTask, List<UploadedFile>> getSignaturesWithUploadedFiles(AbstractDocumentEdi documentEdi);
    ExecutorTask getDraft(User author, AbstractDocumentEdi documentEdi);
}
