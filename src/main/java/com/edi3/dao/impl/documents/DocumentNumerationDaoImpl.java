package com.edi3.dao.impl.documents;

//import abstract_entity.AbstractDocumentEdi;
import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.documents.DocumentNumeration;
import com.edi3.core.documents.DocumentProperty;
import com.edi3.core.exсeption.DocumentNumerationException;
import com.edi3.dao.i.documents.DocumentNumerationDao;
//import documents.DocumentNumeration;
//import documents.DocumentProperty;
//import exсeption.DocumentNumerationException;
//import hibernate.HibernateDAO;
//import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/*
 *
 */
@Transactional
public class DocumentNumerationDaoImpl implements DocumentNumerationDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(DocumentNumeration documentNumeration) {
        sessionFactory.getCurrentSession().save(documentNumeration);
    }

    public void update(DocumentNumeration documentNumeration) {
        sessionFactory.getCurrentSession().update(documentNumeration);
    }

    public void delete(DocumentNumeration documentNumeration) {
        sessionFactory.getCurrentSession().delete(documentNumeration);
    }

    private DocumentNumeration getDocumentNumeration(Session session, DocumentProperty documentProperty, String currentPrefix, Long newNumber){

        Query query = session.createQuery("from DocumentNumeration " +
                "where documentProperty =:documentProperty and prefix =:prefix");
        query.setParameter("documentProperty", documentProperty);
        query.setParameter("prefix", currentPrefix);

        DocumentNumeration documentNumeration = (DocumentNumeration) query.uniqueResult();

        if (Objects.isNull(documentNumeration)){
            documentNumeration = new DocumentNumeration(documentProperty, currentPrefix, Objects.isNull(newNumber) ? 1L : newNumber );
            save(documentNumeration);
        } else {
            documentNumeration.setNumber(Objects.isNull(newNumber) ? documentNumeration.getNumber()+ 1L: newNumber);
            update(documentNumeration);
        }

        return documentNumeration;
    }

    private StringBuilder getFullNumber(DocumentNumeration documentNumeration, DocumentProperty documentProperty, String currentPrefix) throws DocumentNumerationException{

        StringBuilder result = new StringBuilder();
        result.append("").append(documentNumeration.getPrefix()).append("-");
        for (int i = documentProperty.getPrefixLength() - (documentNumeration.getPrefix()+"-"+documentNumeration.getNumber()).length(); i > 0; i--)
            result.append("0");
        result.append(documentNumeration.getNumber());
        if (result.length() >  documentProperty.getPrefixLength()) throw new DocumentNumerationException(currentPrefix, documentNumeration.getNumber());
        return result;

    }

    public String getNextNumber(AbstractDocumentEdi abstractDocumentEdi, String prefix) {
        System.out.println("DocumentNumerationDaoImpl getNextNumber() begin");
        DocumentProperty documentProperty = abstractDocumentEdi.getDocumentProperty();
        String currentPrefix = ((Objects.isNull(prefix) || prefix.equals("")) ? documentProperty.getDefaultPrefix(): prefix);
        StringBuilder result = new StringBuilder();

        if (Objects.nonNull(currentPrefix)) {
            System.out.println("DocumentNumerationDaoImpl getNextNumber() nonNull currentPrefix");
            Session session = sessionFactory.getCurrentSession();
//            session.beginTransaction();

            DocumentNumeration documentNumeration = getDocumentNumeration(session, documentProperty, currentPrefix, null);
            result = getFullNumber(documentNumeration, documentProperty, currentPrefix);

//            session.getTransaction().commit();
//            session.close();
        }

        System.out.println("DocumentNumerationDaoImpl getNextNumber() end");
        return result.toString();
    }

    public String getNextNumberUsingMax(AbstractDocumentEdi abstractDocumentEdi, String prefix) {

        DocumentProperty documentProperty = abstractDocumentEdi.getDocumentProperty();
        String currentPrefix = ((Objects.isNull(prefix) || prefix.equals("")) ? documentProperty.getDefaultPrefix(): prefix);
        StringBuilder result = new StringBuilder();
        Long number;

        if (Objects.nonNull(currentPrefix)) {

            Session session = sessionFactory.getCurrentSession();
//            session.beginTransaction();

            // SELECT number FROM edi.doc_abstract_document_edi where date = (select max(date) FROM edi.doc_abstract_document_edi);
            // SELECT number FROM edi.doc_abstract_document_edi order by date desc limit 1;

            // Create new number using existed documents in database - get max number and increase it

            Query query = session.createQuery("from AbstractDocumentEdi as abstractdocument " +
                    "where type(abstractdocument)= :abstractDocumentEdiClass " +
                    "order by date desc");
            query.setParameter("abstractDocumentEdiClass", abstractDocumentEdi.getClass());
            query.setMaxResults(1);

            AbstractDocumentEdi maxAbstractDocumentEdi = (AbstractDocumentEdi) query.uniqueResult();

            if (Objects.nonNull(maxAbstractDocumentEdi)) {

                String stringMaxNumber = maxAbstractDocumentEdi.getNumber();

                if (!stringMaxNumber.isEmpty()) {
                    if (stringMaxNumber.contains(currentPrefix)) {
                        result.append(stringMaxNumber, currentPrefix.length() + 1, stringMaxNumber.length());
                    } else {
                        // get digits from last to first number and new prefix
                        boolean unexpectedSymbol = false;
                        StringBuilder newPrefix = new StringBuilder();
                        for (int i = stringMaxNumber.length() - 1; i >= 0; i--) {
                            if (!unexpectedSymbol && (stringMaxNumber.charAt(i) >= '0') && (stringMaxNumber.charAt(i) <= '9'))
                                result.insert(0, stringMaxNumber.charAt(i));
                            else {
                                unexpectedSymbol = true;
                                if (stringMaxNumber.charAt(i) != '-') newPrefix.insert(0, stringMaxNumber.charAt(i));
                            }
                        }
                        if (newPrefix.length() > 0) currentPrefix = newPrefix.toString();
                    }
                    number = Long.parseLong(result.toString());
                    DocumentNumeration documentNumeration = getDocumentNumeration(session, documentProperty, currentPrefix, ++number);
                    result = getFullNumber(documentNumeration, documentProperty, currentPrefix);
                }
            }

//            session.getTransaction().commit();
//            session.close();
        }

        return result.toString();
    }
}
