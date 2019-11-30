package com.edi3.service.impl.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.business_processes.ExecutorTaskFolderStructure;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.core.exсeption.ExecutorTaskFolderStructureException;
import com.edi3.dao.i.business_processes.ExecutorTaskFolderStructureDao;
import com.edi3.service.i.business_process.ExecutorTaskFolderStructureService;

import java.util.*;
import java.util.stream.Collectors;

/*
 *
 */
public class ExecutorTaskFolderStructureServiceImpl implements ExecutorTaskFolderStructureService {

    private ExecutorTaskFolderStructureDao executorTaskFolderStructureDao;

    // Getters, setters begin

    public ExecutorTaskFolderStructureDao getExecutorTaskFolderStructureDao() {
        return executorTaskFolderStructureDao;
    }

    public void setExecutorTaskFolderStructureDao(ExecutorTaskFolderStructureDao executorTaskFolderStructureDao) {
        this.executorTaskFolderStructureDao = executorTaskFolderStructureDao;
    }

    // Getters, setters end

    public List<ExecutorTaskFolderStructure> getMarkedTask(User user, String sortingSequence, String filterString) {
        System.out.println("ExecutorTaskFolderStructureServiceImpl - getMarkedTask() begin");
        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = executorTaskFolderStructureDao.getExecutorList(user, FolderStructure.MARKED, filterString,  "", null);

        if (sortingSequence.equals("default")) {
            // Completed (false, true) asc, Date desc
            executorTaskFolderStructureList.sort(
                    (o1, o2) ->
                            (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) != 0 ?
                                    (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) :
                                    o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate())
            );
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.executorTask.document.number
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getNumber()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()));
                    break;
                case '1':  // cell.executorTask.document.getDocumentView("dd.MM.yyyy HH:mm:ss")
                    // Just by date. Or it could be document property + date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getDate().compareTo(o1.getExecutorTask().getDocument().getDate()));
                    break;
                case '2':  // cell.executorTask.document.theme
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '3':  // cell.executorTask.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                    break;
                case '4':  // TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;
            }
        }
        System.out.println("ExecutorTaskFolderStructureServiceImpl - getMarkedTask() end");
        return executorTaskFolderStructureList;

    }

    public List<ExecutorTaskFolderStructure> getTasksByFolder(User user, FolderStructure folderStructure, String groupBy, String sortingSequence, String filterString, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass ) {
        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = executorTaskFolderStructureDao.getExecutorList(user, folderStructure, filterString, groupBy, abstractDocumentEdiClass);

        if (Objects.nonNull(groupBy)) {
            switch (groupBy) {
                case "author":
                    // Delete duplicates using key - Folder, Marked, Result, Completed, ProcessType, Document
                    Map<Integer, ExecutorTaskFolderStructure> mapExecutorTask2 = executorTaskFolderStructureList.stream()
                            .collect(HashMap::new, (m, v) -> m.put(
                                    Objects.hash(v.getFolder(), v.isMarked(), v.getExecutorTask().getResult(), v.getExecutorTask().isCompleted(), v.getExecutorTask().getProcessType(), v.getExecutorTask().getDocument()), v),
                                    HashMap::putAll);

                    executorTaskFolderStructureList.clear();
                    executorTaskFolderStructureList.addAll(mapExecutorTask2.values());
                    break;
                case "sender":
                    // It's ok
                    break;
            }
        }

        if (sortingSequence.equals("default")) {
            // completed asc, date desc
            executorTaskFolderStructureList.sort(
                    (o1, o2) ->
                            (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) != 0 ?
                                    (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) :
                                    o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate())
            );
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':
                case '3':
                    if (((sortingSequence.charAt(0)=='0')&& (folderStructure == FolderStructure.INBOX || folderStructure == FolderStructure.TRASH))
                            || (sortingSequence.charAt(0)=='3' && folderStructure == FolderStructure.MARKED))
                    {
                        if (groupBy.equals("author")) {
                            if (ascSorting)
                                executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getAuthor().getFio()));
                            else
                                executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getAuthor().getFio().compareTo(o1.getExecutorTask().getDocument().getAuthor().getFio()));
                        } else if (groupBy.equals("sender")) {
                            if (ascSorting)
                                executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                            else
                                executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                        }
                    } else if ((sortingSequence.charAt(0)=='0')&&(folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.DRAFT || folderStructure == FolderStructure.MARKED)) {
                        if (groupBy.equals("author")) {
                            if (ascSorting)
                                executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getWhomString()));
                            else
                                executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getWhomString().compareTo(o1.getExecutorTask().getDocument().getWhomString()));
                        } else if (groupBy.equals("sender") &&
                                // condition below cause executorTask.executor = null for DRAFT
                                (folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.MARKED)) {
                            if (ascSorting)
                                executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getExecutor().getFio()));

                            else
                                executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getExecutor().getFio().compareTo(o1.getExecutorTask().getExecutor().getFio()));
                        }
                    }else System.out.println("No!!! groupBy: "+groupBy+", sortingSequence.charAt(0): "+sortingSequence.charAt(0)+", ascSorting: "+ascSorting);
                    break;
                case '1':  // cell.executorTask.processType.ruName + cell.executorTask.document.number + cell.executorTask.document.theme
                    // Draft doesn't have ProcessType that's why for this type sorting by 'Document number' + 'Document theme'
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(
                                (o1, o2) ->
                                        folderStructure != FolderStructure.DRAFT && o1.getExecutorTask().getProcessType().compareTo(o2.getExecutorTask().getProcessType()) != 0 ?
                                                o1.getExecutorTask().getProcessType().compareTo(o2.getExecutorTask().getProcessType()) :
                                                o1.getExecutorTask().getDocument().getNumber().compareTo(o2.getExecutorTask().getDocument().getNumber()) != 0 ?
                                                        o1.getExecutorTask().getDocument().getNumber().compareTo(o2.getExecutorTask().getDocument().getNumber()) :
                                                        o1.getExecutorTask().getDocument().getTheme().compareTo(o2.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort(
                                (o1, o2) ->
                                        folderStructure != FolderStructure.DRAFT && o2.getExecutorTask().getProcessType().compareTo(o1.getExecutorTask().getProcessType()) != 0 ?
                                                o2.getExecutorTask().getProcessType().compareTo(o1.getExecutorTask().getProcessType()) :
                                                o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()) != 0 ?
                                                        o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()) :
                                                        o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '2':  // cell.executorTask.date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;

            }

        }

        return executorTaskFolderStructureList;

    }

    public List<ExecutorTaskFolderStructure> getCommonList(User user, String sortingSequence, String filterString,  Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {

        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = executorTaskFolderStructureDao.getCommonList(user, filterString, abstractDocumentEdiClass);

        if (sortingSequence.equals("default")) {
            // Date desc
            executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.executorTask.document.number
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getNumber()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()));
                    break;
                case '1':  // TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;
                case '2':  // cell.executorTask.document.getDocumentView("dd.MM.yyyy")
                    // Just by date. Or it could be document property + date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getDate().compareTo(o1.getExecutorTask().getDocument().getDate()));
                    break;
                case '3':  // cell.executorTask.document.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getAuthor().getFio().compareTo(o1.getExecutorTask().getDocument().getAuthor().getFio()));
                    break;
                case '4':  // cell.executorTask.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                    break;
                case '5':  // cell.executorTask.executor.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getExecutor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getExecutor().getFio().compareTo(o1.getExecutorTask().getExecutor().getFio()));
                    break;
                case '6':  // cell.executorTask.document.theme
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '7':  // cell.executorTask.completed
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().isCompleted()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> Boolean.compare(o2.getExecutorTask().isCompleted(), o1.getExecutorTask().isCompleted()));
                    break;
            }
        }

        return executorTaskFolderStructureList;

    }

    public void checkDeletionMarkAndChangeFolder(User currentUser, ExecutorTask executorTask, boolean deletionMark) {

        if (Objects.nonNull(executorTask) && Objects.nonNull(currentUser)) {

            boolean foundElement = false;
            List<ExecutorTaskFolderStructure> listBaseExecutorTaskFolderStructureByUser = executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUser(currentUser, executorTask);

            List<ExecutorTaskFolderStructure> listExecutorTaskFolderStructure =
                    listBaseExecutorTaskFolderStructureByUser.stream()
                            .filter(t -> (t.getFolder() != FolderStructure.MARKED))
                            .collect(Collectors.toList());

            boolean isMarked = listBaseExecutorTaskFolderStructureByUser.stream().anyMatch(t -> t.getFolder() == FolderStructure.MARKED);

            if (deletionMark) {

                for (ExecutorTaskFolderStructure executorTaskFolderStructure : listExecutorTaskFolderStructure) {
                    if (executorTaskFolderStructure.getFolder() != FolderStructure.TRASH) {
                        if (foundElement) {
                            executorTaskFolderStructureDao.delete(executorTaskFolderStructure); // Because it's duplicate
                        } else {
                            executorTaskFolderStructure.setFolder(FolderStructure.TRASH);
                            executorTaskFolderStructure.setMarked(false);
                            executorTaskFolderStructureDao.update(executorTaskFolderStructure);
                            foundElement = true;
                        }
                    }
                }

            } else {

                if (listExecutorTaskFolderStructure.size() == 1) {
                    if (currentUser.equals(executorTask.getAuthor())
                            && executorTask.getAuthor().equals(executorTask.getExecutor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.SENT, isMarked);
                        executorTaskFolderStructureDao.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, currentUser, executorTask, isMarked));
                    } else if (currentUser.equals(executorTask.getAuthor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.SENT, isMarked);
                    } else if (currentUser.equals(executorTask.getExecutor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.INBOX, isMarked);
                    }
                } else throw new ExecutorTaskFolderStructureException(executorTask.getId());

            }

        }
    }

    public void changeMarkedStatus(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask executorTask) {

        if (Objects.nonNull(currentUser)) {
            List<ExecutorTaskFolderStructure> listExecutorTaskFolderStructure =
                    (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType())) ?
                            executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUserDocument(currentUser, documentEdi) :
                            executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUserDocumentProcessType(currentUser, documentEdi, executorTask.getProcessType());

            if (listExecutorTaskFolderStructure.size() > 0) {

                boolean setMark = listExecutorTaskFolderStructure.stream().noneMatch(ExecutorTaskFolderStructure::isMarked);

                listExecutorTaskFolderStructure.stream().filter(t -> (t.getFolder() != FolderStructure.TRASH)).forEach(t -> {
                    t.setMarked(setMark);
                    executorTaskFolderStructureDao.update(t);
                });

                if (setMark) {
                    executorTaskFolderStructureDao.save(
                            new ExecutorTaskFolderStructure(FolderStructure.MARKED, currentUser,
                                    (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType())) ?
                                            listExecutorTaskFolderStructure.get(0).getExecutorTask() : executorTask, true));
                } else {
                    //listExecutorTaskFolderStructure.stream().filter(t -> (t.getFolder() == FolderStructure.MARKED)).forEach(ExecutorTaskFolderStructureImpl.INSTANCE::delete);
                    // TODO - check it
                    listExecutorTaskFolderStructure.stream().filter(t -> (t.getFolder() == FolderStructure.MARKED)).forEach(executorTaskFolderStructureDao::delete);
                }
            }
        }

    }

    private void updateExecutorTaskFolderStructureImpl(ExecutorTaskFolderStructure executorTaskFolderStructure, FolderStructure folderStructure, boolean isMarked) {
        executorTaskFolderStructure.setFolder(folderStructure);
        executorTaskFolderStructure.setMarked(isMarked);
        executorTaskFolderStructureDao.update(executorTaskFolderStructure);
    }

    public HashMap<FolderStructure, Integer> getTaskCountByFolders(User user, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {
        return executorTaskFolderStructureDao.getTaskCountByFolders(user, abstractDocumentEdiClass);
    }

    public boolean isMarkedExecutorTask(User user, AbstractDocumentEdi documentEdi, ExecutorTask executorTask) {
        if (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType())) {
            return isMarkedDocument(user, documentEdi);
        }
        return isMarkedDocumentProcessType(user, documentEdi, executorTask.getProcessType());
    }

    private boolean isMarkedDocument(User user, AbstractDocumentEdi documentEdi) {
        return executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUserDocument(user, documentEdi).stream().anyMatch(ExecutorTaskFolderStructure::isMarked);
    }

    private boolean isMarkedDocumentProcessType(User user, AbstractDocumentEdi documentEdi, ProcessType processType) {
        return executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUserDocumentProcessType(user, documentEdi, processType).stream().anyMatch(ExecutorTaskFolderStructure::isMarked);
    }

}



