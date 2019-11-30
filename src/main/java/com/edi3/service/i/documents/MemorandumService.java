package com.edi3.service.i.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.documents.Memorandum;

import java.util.List;

public interface MemorandumService {
    Memorandum getById(Long documentId);
    Memorandum createOrUpdateDocument(Memorandum documentEdi, java.sql.Timestamp timeStamp, User currentUser, String theme,
                                      String textInfo, List<UploadedFile> fileList, User whomUser, String operationType);
}
