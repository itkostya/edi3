package com.edi3.service.impl.business_process;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.BusinessProcess;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.business_processes.ExecutorTaskFolderStructure;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.core.enumerations.ProcessOrderType;
import com.edi3.core.enumerations.ProcessResult;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.dao.i.business_processes.BusinessProcessDao;
import com.edi3.dao.i.business_processes.BusinessProcessSequenceDao;
import com.edi3.dao.i.business_processes.ExecutorTaskDao;
import com.edi3.dao.i.business_processes.ExecutorTaskFolderStructureDao;
import com.edi3.service.i.business_process.BusinessProcessSequenceService;
import com.edi3.service.i.business_process.BusinessProcessService;
import com.edi3.service.i.business_process.ExecutorTaskService;
import com.edi3.service.i.categories.UploadedFileService;
import com.edi3.service.i.categories.UserService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BusinessProcessServiceImpl implements BusinessProcessService {

    private BusinessProcessDao businessProcessDao;
    private BusinessProcessSequenceDao businessProcessSequenceDao;
    private BusinessProcessSequenceService businessProcessSequenceService;
    private ExecutorTaskDao executorTaskDao;
    private ExecutorTaskService executorTaskService;
    private ExecutorTaskFolderStructureDao executorTaskFolderStructureDao;
    private UploadedFileService uploadedFileService;
    private UserService userService;

    // Getters, setters begin
    public BusinessProcessDao getBusinessProcessDao() {
        return businessProcessDao;
    }

    public void setBusinessProcessDao(BusinessProcessDao businessProcessDao) {
        this.businessProcessDao = businessProcessDao;
    }

    public BusinessProcessSequenceDao getBusinessProcessSequenceDao() {
        return businessProcessSequenceDao;
    }

    public void setBusinessProcessSequenceDao(BusinessProcessSequenceDao businessProcessSequenceDao) {
        this.businessProcessSequenceDao = businessProcessSequenceDao;
    }

    public BusinessProcessSequenceService getBusinessProcessSequenceService() {
        return businessProcessSequenceService;
    }

    public void setBusinessProcessSequenceService(BusinessProcessSequenceService businessProcessSequenceService) {
        this.businessProcessSequenceService = businessProcessSequenceService;
    }

    public ExecutorTaskDao getExecutorTaskDao() {
        return executorTaskDao;
    }

    public void setExecutorTaskDao(ExecutorTaskDao executorTaskDao) {
        this.executorTaskDao = executorTaskDao;
    }

    public ExecutorTaskService getExecutorTaskService() {
        return executorTaskService;
    }

    public void setExecutorTaskService(ExecutorTaskService executorTaskService) {
        this.executorTaskService = executorTaskService;
    }

    public ExecutorTaskFolderStructureDao getExecutorTaskFolderStructureDao() {
        return executorTaskFolderStructureDao;
    }

    public void setExecutorTaskFolderStructureDao(ExecutorTaskFolderStructureDao executorTaskFolderStructureDao) {
        this.executorTaskFolderStructureDao = executorTaskFolderStructureDao;
    }

    public UploadedFileService getUploadedFileService() {
        return uploadedFileService;
    }

    public void setUploadedFileService(UploadedFileService uploadedFileService) {
        this.uploadedFileService = uploadedFileService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // Getters, setters end

    public void createAndStartBusinessProcess(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask draftExecutorTask, Timestamp timeStamp,
                                              String[] usersIdArray, String[] orderTypeArray, String[] processTypeArray,
                                              ProcessType processTypeCommon, String comment, Timestamp finalDate) {

        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() begin");
        if (Objects.nonNull(documentEdi)) {
            System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 1");
            BusinessProcess businessProcess = new BusinessProcess(timeStamp, false, currentUser, finalDate, documentEdi, comment, null);
            businessProcessDao.save(businessProcess);
            System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 2");
            boolean createNewTask = true;
            for (int k = 0; k < usersIdArray.length; k++) {
                ProcessOrderType processOrderType = (Objects.nonNull(orderTypeArray) && (k < orderTypeArray.length) ? ProcessOrderType.values()[Integer.valueOf(orderTypeArray[k])] : null);
                System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 3");
                ProcessType processType = (Objects.nonNull(processTypeCommon) ? processTypeCommon :
                        (k < processTypeArray.length ? documentEdi.getDocumentProperty().getProcessTypeList().get(Integer.valueOf(processTypeArray[k])) : null));

                User userExecutor = userService.getUserById(Long.valueOf(usersIdArray[k]));
                System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 4");
                BusinessProcessSequence businessProcessSequence =
                        new BusinessProcessSequence(null, businessProcess, userExecutor, false, processOrderType, null, processType, false, null);
                businessProcessSequenceDao.save(businessProcessSequence);
                System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 5");
                if ((k == 0) || (createNewTask)) {
                    ExecutorTask executorTask;
                    if (k == 0 && Objects.nonNull(draftExecutorTask)) {
                        executorTask = draftExecutorTask;
                        executorTask.setBusinessProcess(businessProcess);
                        executorTask.setCompleted(false);
                        executorTask.setProcessType(processType);
                        executorTask.setExecutor(userExecutor);
                        executorTask.setDraft(false);
                        executorTaskDao.update(executorTask);
                        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 6");
                        for (ExecutorTaskFolderStructure executorTaskFolderStructure : executorTaskFolderStructureDao.getExecutorTaskFolderStructureByUser(currentUser, executorTask))
                            executorTaskFolderStructureDao.delete(executorTaskFolderStructure);
                        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 7");

                    } else {
                        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 8");
                        executorTask = new ExecutorTask(timeStamp, businessProcess, false, currentUser, documentEdi, null, "", null, processType, finalDate, userExecutor, false, false, false);
                        executorTaskDao.save(executorTask);
                        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 9");
                    }
                    System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 10");
                    businessProcessSequence.setDate(timeStamp);
                    businessProcessSequence.setExecutorTask(executorTask);
                    businessProcessSequenceDao.update(businessProcessSequence);
                    System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 11");

                    executorTaskFolderStructureDao.save(new ExecutorTaskFolderStructure(FolderStructure.SENT, executorTask.getAuthor(), executorTask, false));
                    executorTaskFolderStructureDao.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, executorTask.getExecutor(), executorTask, false));

                    System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() 12");

                    if (processOrderType == ProcessOrderType.AFTER) createNewTask = false;
                }
            }
        }
        System.out.println("BusinessProcessServiceImpl createAndStartBusinessProcess() end");
    }


    public ExecutorTask createDraftExecutorTask(User currentUser, AbstractDocumentEdi documentEdi, Timestamp timeStamp, Timestamp finalDate) {

        ExecutorTask executorTask = new ExecutorTask(timeStamp, null, true, currentUser, documentEdi, null, "", null, null, finalDate, null, false, false, true);
        executorTaskDao.save(executorTask);
        executorTaskFolderStructureDao.save(new ExecutorTaskFolderStructure(FolderStructure.DRAFT, executorTask.getAuthor(), executorTask, false));

        return executorTask;
    }

    public void stopBusinessProcessSequence(AbstractDocumentEdi documentEdi, Long[] businessProcessSequenceArrayId, ExecutorTask currentExecutorTask) {
        Timestamp timeStamp = new Timestamp(new java.util.Date().getTime());

        Map<BusinessProcess, List<BusinessProcessSequence>> mapBusinessProcess = businessProcessSequenceService.getHistoryByDocumentMap(documentEdi);
        //businessProcessSequenceDao.
        for (Map.Entry<BusinessProcess, List<BusinessProcessSequence>> businessProcessListEntry : mapBusinessProcess.entrySet()) {
            boolean shouldBeCanceled = false;
            for (int i = 0; i < businessProcessListEntry.getValue().size(); i++) {
                BusinessProcessSequence businessProcessSequence = businessProcessListEntry.getValue().get(i);
                if (shouldBeCanceled || (Arrays.stream(businessProcessSequenceArrayId).anyMatch(t -> t.equals(businessProcessSequence.getId())))) {
                    if (Objects.isNull(businessProcessSequence.getExecutorTask())) {
                        businessProcessSequenceDao.delete(businessProcessSequence);
                        shouldBeCanceled = true;
                    } else {
                        businessProcessSequence.setResult(ProcessResult.CANCELED);
                        businessProcessSequenceDao.update(businessProcessSequence);
                        ExecutorTask executorTask = businessProcessSequence.getExecutorTask();
                        cancelExecutorTask(executorTask.equals(currentExecutorTask) ? currentExecutorTask : executorTask, timeStamp);
                        if (businessProcessSequence.getOrderBp() == ProcessOrderType.AFTER) shouldBeCanceled = true;
                    }
                }
            }
        }
    }

    private void cancelExecutorTask(ExecutorTask executorTask, java.sql.Timestamp timeStamp) {
        executorTask.setCompleted(true);
        executorTask.setResult(ProcessResult.CANCELED);
        executorTask.setDateCompleted(timeStamp);
        executorTaskDao.update(executorTask);
    }

    private void clearExecutorTask(ExecutorTask executorTask) {
        executorTask.setCompleted(false);
        executorTask.setResult(null);
        executorTask.setDateCompleted(null);
        executorTask.setComment("");
        executorTaskDao.update(executorTask);
    }

    public void withdrawExecutorTasks(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask currentExecutorTask) {
        List<ExecutorTask> withdrawAvailableList = executorTaskService.getWithdrawAvailable(currentUser, documentEdi);

        withdrawAvailableList.stream().filter(Objects::nonNull).forEach(withdrawExecutorTask -> {
            clearExecutorTask(withdrawExecutorTask.equals(currentExecutorTask) ? currentExecutorTask : withdrawExecutorTask);
            BusinessProcess businessProcess = withdrawExecutorTask.getBusinessProcess();
            if (Objects.nonNull(businessProcess)) {
                businessProcess.setCompleted(false);
                businessProcess.setResult(null);
                businessProcessDao.update(businessProcess);
            }

            BusinessProcessSequence businessProcessSequence = withdrawExecutorTask.getBusinessProcessSequence();
            if (Objects.nonNull(businessProcessSequence)) {
                businessProcessSequence.setCompleted(false);
                businessProcessSequence.setResult(null);
                businessProcessSequenceDao.update(businessProcessSequence);
            }

            uploadedFileService.deleteByDocumentAndExecutorTask(documentEdi, withdrawExecutorTask); // TODO - check it
            //UploadedFileServiceImpl.INSTANCE.getListByDocumentAndExecutorTask(documentEdi, withdrawExecutorTask).forEach(UploadedFileImpl.INSTANCE::delete);
       });
    }

}
