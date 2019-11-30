package com.edi3.service.i.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.business_processes.ExecutorTaskFolderStructure;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.FolderStructure;

import java.util.HashMap;
import java.util.List;

public interface ExecutorTaskFolderStructureService {
    List<ExecutorTaskFolderStructure> getMarkedTask(User user, String sortingSequence, String filterString);
    List<ExecutorTaskFolderStructure> getTasksByFolder(User user, FolderStructure folderStructure, String groupBy, String sortingSequence, String filterString, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
    List<ExecutorTaskFolderStructure> getCommonList(User user, String sortingSequence, String filterString,  Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
    void checkDeletionMarkAndChangeFolder(User currentUser, ExecutorTask executorTask, boolean deletionMark);
    void changeMarkedStatus(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask executorTask);
    HashMap<FolderStructure, Integer> getTaskCountByFolders(User user, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
    boolean isMarkedExecutorTask(User user, AbstractDocumentEdi documentEdi, ExecutorTask executorTask);
}
