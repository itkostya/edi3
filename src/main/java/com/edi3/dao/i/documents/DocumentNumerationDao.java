package com.edi3.dao.i.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.documents.DocumentNumeration;
import com.edi3.dao.i.HibernateDAO;

public interface DocumentNumerationDao extends HibernateDAO<DocumentNumeration> {
    String getNextNumber(AbstractDocumentEdi abstractDocumentEdi, String prefix);
    String getNextNumberUsingMax(AbstractDocumentEdi abstractDocumentEdi, String prefix);
}
