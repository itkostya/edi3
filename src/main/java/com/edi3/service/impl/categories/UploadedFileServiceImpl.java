package com.edi3.service.impl.categories;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.dao.i.categories.UploadedFileDao;
import com.edi3.service.i.categories.UploadedFileService;

import java.util.List;

/*
 *
 */
public class UploadedFileServiceImpl implements UploadedFileService {

    private UploadedFileDao uploadedFileDao;

    // Getters, setters begin

    public UploadedFileDao getUploadedFileDao() {
        return uploadedFileDao;
    }

    public void setUploadedFileDao(UploadedFileDao uploadedFileDao) {
        this.uploadedFileDao = uploadedFileDao;
    }
    // Getters, setters end

    public UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask) {
        return uploadedFileDao.getByFileNameAndDocument(fileName, abstractDocumentEdi, executorTask);
    }

    public List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi){
        return uploadedFileDao.getListByDocument(abstractDocumentEdi);
    }

    public List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask){
        return uploadedFileDao.getListByDocumentAndExecutorTask(abstractDocumentEdi, executorTask);
    }

    public void deleteByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask) {
        uploadedFileDao.getListByDocumentAndExecutorTask(abstractDocumentEdi, executorTask).forEach(uploadedFileDao::delete);
    }
}
