package com.edi3.dao.i.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.dao.i.HibernateDAO;

public interface DocumentDao extends HibernateDAO<AbstractDocumentEdi> {
    AbstractDocumentEdi getById(Long id);
}
