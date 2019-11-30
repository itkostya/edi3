package com.edi3.service.i.categories;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;

import java.util.List;

public interface UploadedFileService {
    UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask);
    List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi);
    List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask);
    void deleteByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask);
}
