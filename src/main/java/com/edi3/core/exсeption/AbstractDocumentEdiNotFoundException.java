package com.edi3.core.exсeption;

/*
* Error when we can't find the document
*
* Created by kostya on 1/18/2017.
*/

public class AbstractDocumentEdiNotFoundException extends RuntimeException {
    public AbstractDocumentEdiNotFoundException(long id) {
        super("AbstractDocumentEdiNotFoundException = " + id);
    }
}
