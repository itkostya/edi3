package com.edi3.dao.i.categories;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.dao.i.HibernateDAO;

import java.util.List;

public interface UploadedFileDao extends HibernateDAO<UploadedFile> {
    UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask);
    List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi);
    List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask);
}
