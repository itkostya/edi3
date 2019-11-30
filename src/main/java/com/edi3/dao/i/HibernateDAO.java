package com.edi3.dao.i;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

public interface HibernateDAO<T>  {

    @SuppressWarnings("unused")
    void save(T t) throws ConstraintViolationException, PersistenceException;

    @SuppressWarnings("unused")
    void update(T t);

    @SuppressWarnings("unused")
    void delete(T t);

}
