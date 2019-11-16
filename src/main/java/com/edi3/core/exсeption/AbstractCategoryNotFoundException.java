package com.edi3.core.exсeption;

/*
* Error when we can't find the document
*
* Created by kostya on 10/31/2017.
*/

import com.edi3.core.abstract_entity.AbstractCategory;

public class AbstractCategoryNotFoundException extends RuntimeException {
    public AbstractCategoryNotFoundException(Class<? extends AbstractCategory> abstractCategoryClass, Long id) {
        super("AbstractCategoryNotFoundException class: "+abstractCategoryClass +" id: " + id);
    }
}
