package com.edi3.service.impl.documents;

import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.documents.Memorandum;
import com.edi3.dao.i.documents.DocumentDao;
import com.edi3.service.i.documents.MemorandumService;
import com.edi3.web.tools.CommonModule;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class MemorandumServiceImpl implements MemorandumService {

    private DocumentDao documentDao;

    @Override
    public Memorandum getById(Long id) {
        return (Memorandum) documentDao.getById(id);
    }

    // Getters, setters

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @Override
    public Memorandum createOrUpdateDocument(Memorandum documentEdi, Timestamp timeStamp, User currentUser, String theme, String textInfo, List<UploadedFile> fileList, User whomUser, String operationType) {
        System.out.println("MemorandumServiceImpl - createOrUpdateDocument() begin");

        if (Objects.isNull(documentEdi)) {
            documentEdi = new Memorandum(timeStamp, false, null, false, currentUser, CommonModule.getCorrectStringForWeb(theme), (operationType.equals("save") ? textInfo : CommonModule.getCorrectStringForWeb(textInfo)), whomUser.getFio(), whomUser);
            documentDao.save(documentEdi);
        } else {
            documentEdi.setWhomString(whomUser.getFio());
            documentEdi.setTheme(CommonModule.getCorrectStringForWeb(theme));
            documentEdi.setText(operationType.equals("save") ? textInfo : CommonModule.getCorrectStringForWeb(textInfo));
            documentDao.update(documentEdi);
        }

//        // files from request should be the same as the files in database - if this condition doesn't work we should delete files in database
////        List<UploadedFile> uploadedFilesFromRequest = DocumentCreate.getUploadedFileListFromRequest(req, documentEdi);
////        List<UploadedFile> filesInDatabase = UploadedFileServiceImpl.INSTANCE.getListByDocument(documentEdi);
////        filesInDatabase.removeAll(uploadedFilesFromRequest);
////        for (UploadedFile uploadedFile : filesInDatabase)
////            UploadedFileImpl.INSTANCE.delete(uploadedFile);
////
////        DocumentCreate.addFilesToDocument(fileList, documentEdi);

        System.out.println("MemorandumServiceImpl - createOrUpdateDocument() end");
        return documentEdi;
    }
}
