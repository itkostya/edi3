package com.edi3.service.impl.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.dao.i.documents.DocumentDao;
import com.edi3.service.i.documents.DocumentService_Test;

public class DocumentServiceImpl_Test implements DocumentService_Test {

    private DocumentDao documentDao;

    @Override
    public AbstractDocumentEdi getById(Long id) {
        return documentDao.getById(id);
    }

    // Getters, setters

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }
}
