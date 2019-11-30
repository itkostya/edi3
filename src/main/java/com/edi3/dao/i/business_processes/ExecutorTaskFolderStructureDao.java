package com.edi3.dao.i.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.business_processes.ExecutorTaskFolderStructure;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.dao.i.HibernateDAO;

import java.util.HashMap;
import java.util.List;

public interface ExecutorTaskFolderStructureDao extends HibernateDAO<ExecutorTaskFolderStructure> {
    List<ExecutorTaskFolderStructure> getExecutorList(User user, FolderStructure folderStructure, String filterString, String groupBy, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
    List<ExecutorTaskFolderStructure> getCommonList(User user, String filterString, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
    List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUser(User user, ExecutorTask executorTask);
    List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocumentProcessType(User user, AbstractDocumentEdi documentEdi, ProcessType processType);
    List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocument(User user, AbstractDocumentEdi documentEdi);
    HashMap<FolderStructure, Integer> getTaskCountByFolders(User user, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass);
}
