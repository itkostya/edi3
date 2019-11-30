package com.edi3.web.view.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.app_info.Constant;
import com.edi3.core.app_info.TimeModule;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.documents.DocumentProperty;
import com.edi3.core.documents.Memorandum;
import com.edi3.core.documents.Message;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.service.i.business_process.BusinessProcessSequenceService;
import com.edi3.service.i.business_process.ExecutorTaskFolderStructureService;
import com.edi3.service.i.business_process.ExecutorTaskService;
import com.edi3.service.i.categories.UserService;
import com.edi3.web.model.ElementStatus;
import com.edi3.web.model.SessionDataElement;
import com.edi3.web.model.SessionParameter;
import com.edi3.web.tools.CommonModule;
import com.edi3.web.tools.PageContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = {PageContainer.EXECUTOR_TASK_PAGE})
@MultipartConfig
public class ExecutorTaskController {
    private BusinessProcessSequenceService businessProcessSequenceService;
    private ExecutorTaskService executorTaskService;
    private ExecutorTaskFolderStructureService executorTaskFolderStructureService;
    private UserService userService;

    private List<User> userList;  // TODO - it was static

    @Autowired
    public ExecutorTaskController(BusinessProcessSequenceService businessProcessSequenceService, ExecutorTaskService executorTaskService, UserService userService) {
        this.businessProcessSequenceService = businessProcessSequenceService;
        this.executorTaskService = executorTaskService;
        this.userService = userService;
        this.userList =  userService.getUsers();
    }

    private java.sql.Date finalDate = TimeModule.getFinalDateOfProcess();

//    private void doGet(
//            @RequestParam(value="tempId", required=false) Long tempId,
//            @RequestParam(value="documentId", required=false) Long documentId,
//            @RequestParam(value="executorTaskId", required=false) Long executorTaskId,
//            HttpServletRequest req) {
//
//        AbstractDocumentEdi documentEdi = null;
//        ExecutorTask executorTask = null;
//
////        req.setCharacterEncoding("UTF-8");
////        resp.setCharacterEncoding("UTF-8");
//
//        if (SessionParameter.INSTANCE.accessAllowed(req)) {
//
//            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);
//
//            if (Objects.nonNull(sessionDataElement) &&
//                    (sessionDataElement.getElementStatus() == ElementStatus.CREATE
//                            || sessionDataElement.getElementStatus() == ElementStatus.ERROR
//                            || sessionDataElement.getElementStatus() == ElementStatus.STORE
//                            || sessionDataElement.getElementStatus() == ElementStatus.UPDATE
//                    )) {
//
//                if (Objects.nonNull(sessionDataElement.getDocumentEdi())) {
//                    documentEdi = sessionDataElement.getDocumentEdi();
//                } else {
//                    if (Objects.nonNull(documentId)) {
//                        //documentEdi = AbstractDocumentEdiImpl.INSTANCE.getById(documentId); // TODO!!!
//                    }
//                }
//
//                if (Objects.nonNull(sessionDataElement.getExecutorTask())) {
//                    executorTask = sessionDataElement.getExecutorTask();
//                } else {
//                    if (Objects.nonNull(executorTaskId))
//                        executorTask = executorTaskService.getById(executorTaskId);
//                }
//
//                if (Objects.nonNull(documentEdi)) {
//
//                    User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
//
//                    // Check executorTask:
//                    // 1) is this task of current user
//                    // 2) is completed
//                    if (Objects.nonNull(executorTask)) {
//                        if (currentUser.equals(executorTask.getExecutor())) {
//                            if (executorTask.isCompleted()) {
//                                // find another (not completed) task with this process type
//                                executorTask = executorTaskService.getFilterByExecutorDocumentAndProcessType(currentUser, documentEdi, executorTask.getProcessType()).stream().findFirst().orElse(null);
//                            }
//                        } else {
//                            executorTask = null;
//                        }
//                    }
//
//                    // Open task by link without accurate job
//                    if (Objects.isNull(executorTask)) {
//                        // Check open tasks of this user and get 1st of them
//                        executorTask = executorTaskService.getFilterByExecutorAndDocument(currentUser, documentEdi).stream().findFirst().orElse(null);
//                    }
//
//                    if (Objects.nonNull(executorTask)) {
//                        BusinessProcessSequence businessProcessSequence = businessProcessSequenceService.getBusinessProcessSequence(executorTask);
//                        if (!businessProcessSequence.isViewed()) {
//                            businessProcessSequence.setViewed(true);
//                            //BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence); // TODO!!!
//                        }
//
//                        if (documentEdi instanceof Message) {
//                            setBusinessProcessAction(documentEdi, executorTask, currentUser, true, "", Collections.emptyList());
//                        }
//                    }
//
//                    sessionDataElement.setDocumentEdi(documentEdi);
//                    sessionDataElement.setExecutorTask(executorTask);
//                    if (sessionDataElement.getElementStatus() != ElementStatus.UPDATE) {
//                        sessionDataElement.setElementStatus(ElementStatus.STORE);
//                    }
//
//                    if ((documentEdi instanceof Memorandum) && (Objects.nonNull(((Memorandum)documentEdi).getWhom()))) {
//                        req.setAttribute("positionTo", ((Memorandum)documentEdi).getWhom().getPosition().getName());
//                        req.setAttribute("userTo", ((Memorandum)documentEdi).getWhom().getFioInitials());
//                    }
//
//                    req.setAttribute("userWhomString", documentEdi.getWhomString()); // for Message
//
//                    if (Objects.nonNull(documentEdi.getAuthor())) {
//                        if (Objects.nonNull(documentEdi.getAuthor().getPosition()))
//                            req.setAttribute("positionFrom", documentEdi.getAuthor().getPosition().getName());
//                        req.setAttribute("userFrom", documentEdi.getAuthor().getFioInitials());
//                    }
//
//                    if (Objects.nonNull(documentEdi.getDocumentProperty()))
//                        req.setAttribute("docType", documentEdi.getDocumentProperty().getRuName());
//
//                    req.setAttribute("docNumber", documentEdi.getNumber());
//                    req.setAttribute("docDate", new SimpleDateFormat("dd.MM.yyyy").format(documentEdi.getDate()));
//                    req.setAttribute("docTheme", documentEdi.getTheme());
//                    req.setAttribute("docText", documentEdi.getText());
//                    req.setAttribute("documentEdi", documentEdi);
//                    req.setAttribute("processType", (Objects.nonNull(executorTask) ? executorTask.getProcessType() : null));
//                    req.setAttribute("mapSignatures", executorTaskService.getSignaturesByMap(documentEdi));
//                    req.setAttribute("finalDate", finalDate);
//                    req.setAttribute("userList", userList);
//                    req.setAttribute("currentUser", currentUser);
//                    req.setAttribute("executorTask", executorTask);
////                    req.setAttribute("uploadedFiles", UploadedFileServiceImpl.INSTANCE.getListByDocument(documentEdi));
//
//                    // Command bar
//                    req.setAttribute("markedAvailable", markedAvailable(currentUser, documentEdi, executorTask));
//                    req.setAttribute("isMarkedExecutorTask", executorTaskFolderStructureService.isMarkedExecutorTask(currentUser, documentEdi, executorTask));
//                    req.setAttribute("withdrawAvailable", documentEdi.getDocumentProperty().getDeclinedFieldList().contains("withdrawAvailable") ? null : !executorTaskService.getWithdrawAvailable(currentUser, documentEdi).isEmpty());
//                    req.setAttribute("mapHistory", documentEdi.getDocumentProperty().getDeclinedFieldList().contains("mapHistory") ? null : businessProcessSequenceService.getHistoryByDocumentMap(documentEdi));
//                    req.setAttribute("mapStop",  documentEdi.getDocumentProperty().getDeclinedFieldList().contains("mapStop")? null : businessProcessSequenceService.getNotCompletedSequenceByDocumentAndUser(documentEdi, currentUser));
//
//                    req.setAttribute("commentForRecipients", (Objects.nonNull(executorTask) && Objects.nonNull(executorTask.getBusinessProcess()) && Objects.nonNull(executorTask.getBusinessProcess().getComment()) ? executorTask.getBusinessProcess().getComment() : ""));
//                }
//            }
//            req.setAttribute("tempId", tempId);
//            req.setAttribute("sessionDataElement", sessionDataElement);
//            //req.getRequestDispatcher(PageContainer.EXECUTOR_TASK_JSP).forward(req, resp);
//        } else {
//            req.setAttribute("error_message", "Access denied");
//            //req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
//        }
//    }

//    protected void doPost(
//            @RequestParam(value="tempId") Long tempId,
//            @RequestParam(value="comment") String comment,
//            @RequestParam(value="param") String param,
//            @RequestParam(value="param2") String param2,
//            @RequestParam(value="closeDocument") String closeDocumentString,
//            HttpServletRequest req) throws ServletException, IOException{
//
//        AbstractDocumentEdi documentEdi = null;
//        ExecutorTask executorTask = null;
////
////        req.setCharacterEncoding("UTF-8");
////        resp.setCharacterEncoding("UTF-8");
////        resp.setContentType("text/html;charset=UTF-8");
//
//        if (SessionParameter.INSTANCE.accessAllowed(req)) {
//            try {
//                req.getParts();  // Check is it possible to work (catch error maxFileSize with exception IllegalStateException)
//
//                User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
//                SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);
//
//                if ((Objects.nonNull(sessionDataElement) && Objects.nonNull(sessionDataElement.getDocumentEdi())))
//                    documentEdi = sessionDataElement.getDocumentEdi();
//                if ((Objects.nonNull(sessionDataElement) && Objects.nonNull(sessionDataElement.getExecutorTask())))
//                    executorTask = sessionDataElement.getExecutorTask();
//
//                if (Objects.nonNull(param2)) {
//                    param = param2;
//                }
//                if (Objects.nonNull(comment)) {
//                    comment = CommonModule.getCorrectStringForWeb(comment);
//                }
//
//                Boolean closeDocument = (!Objects.isNull(closeDocumentString) && "on".equals(closeDocumentString));
//
//                sessionDataElement.setElementStatus(closeDocument ? ElementStatus.CLOSE : ElementStatus.STORE);
//
//                try {
//
//                    if (Objects.isNull(param)) {
//                        // No param - set status in document (accept, decline)
//                        int chosenActionId = Integer.valueOf(req.getParameter("chosenActionId"));
//                        if ((chosenActionId == 0) || (chosenActionId == 1)) {
//                            List<UploadedFile> fileList = CommonModule.getFileListFromRequest(req, "fileList[]"); // new files from client
//                            setBusinessProcessAction(documentEdi, executorTask, currentUser, chosenActionId == 0, comment, fileList);
//                            sessionDataElement.setExecutorTask(executorTask);   // Could be some trouble when executorTask wasn't updated in  sessionDataElement and in database has another condition
//                        }
//                    } else {
//                        switch (param) {
//                            case "mark":
//                                if (markedAvailable(currentUser, documentEdi, executorTask)) {
//                                    executorTaskFolderStructureService.changeMarkedStatus(currentUser, documentEdi, executorTask);
//                                    sessionDataElement.setElementStatus(ElementStatus.UPDATE);
//                                }
//                                break;
//                            case "send":
//                                String[] usersIdArray = "".equals(req.getParameter("post_users[]")) ? null : req.getParameter("post_users[]").split(",");
//
//                                if (Objects.isNull(usersIdArray)) {
//                                    sessionDataElement.setElementStatus(ElementStatus.ERROR);
//                                    sessionDataElement.setErrorMessage("Не выбран(ы) получатели документа");
//                                } else {
//                                    // Order type
//                                    String[] orderTypeArray = req.getParameter("post_order_type[]").split(",");
//
//                                    // Process type
//                                    int processTypeParameter = Integer.valueOf(req.getParameter("process_type"));  // scenario - a lot of different process
//                                    String[] processTypeArray = null;
//                                    ProcessType processTypeCommon = null;
//                                    if (processTypeParameter == Constant.SCENARIO_NUMBER) {
//                                        processTypeArray = req.getParameter("post_process_type[]").split(",");
//                                    } else {
//                                        processTypeCommon = DocumentProperty.MEMORANDUM.getProcessTypeList().get(processTypeParameter);
//                                    }
//
//                                    finalDate = java.sql.Date.valueOf(req.getParameter("finalDate")); //java.sql.Date.valueOf("2015-01-21")
//
//                                    // Create document Memorandum, business_process' classes: BusinessProcess, BusinessProcessSequence, ExecutorTask
//                                    CommonBusinessProcessServiceImpl.INSTANCE.createAndStartBusinessProcess(currentUser, documentEdi, null, TimeModule.getCurrentDate(), usersIdArray, orderTypeArray, processTypeArray, processTypeCommon, comment, new Timestamp(finalDate.getTime()));
//                                }
//                                break;
//                            case "in-trash":
//                            case "restore-trash":
//                                boolean deletionMark = param.equals("in-trash");
//                                ExecutorTaskFolderStructureServiceImpl.INSTANCE.checkDeletionMarkAndChangeFolder(currentUser, executorTask, deletionMark);
//                                ExecutorTaskServiceImpl.INSTANCE.checkAndSetDeletionDependOnUser(currentUser, executorTask, deletionMark);
//                                break;
//                            case "stop":
//                                Long[] businessProcessSequenceId = CommonModule.convertArray(req.getParameter("businessProcessSequenceId[]").split(","), Long::parseLong, Long[]::new);
//                                CommonBusinessProcessServiceImpl.INSTANCE.stopBusinessProcessSequence(documentEdi, businessProcessSequenceId, executorTask);
//                                sessionDataElement.setExecutorTask(executorTask);
//                                break;
//                            case "withdraw":
//                                CommonBusinessProcessServiceImpl.INSTANCE.withdrawExecutorTasks(currentUser, documentEdi, executorTask);
//                                sessionDataElement.setExecutorTask(executorTask);
//                                sessionDataElement.setElementStatus(ElementStatus.UPDATE);
//                                break;
//                        }
//                    }
//
//                } catch (ConstraintViolationException e) {
//                    sessionDataElement.setElementStatus(ElementStatus.ERROR);
//                    sessionDataElement.setErrorMessage("Ошибка (ConstraintViolationException): " +
//                            e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
//                } catch (PersistenceException e) {
//                    sessionDataElement.setElementStatus(ElementStatus.ERROR);
//                    sessionDataElement.setErrorMessage("PersistenceException: " +
//                            e.getMessage());
//                }
//
//                if (ElementStatus.ERROR == sessionDataElement.getElementStatus()) {
//                    req.setAttribute("infoResult", sessionDataElement.getErrorMessage());
//                }
//
//            } catch (IllegalStateException e) {
//                req.setAttribute("infoResult", (e.getCause().getMessage().contains("exceeds its maximum permitted size") ?
//                        "Ошибка (превышен максимальный размер файла " + Constant.MAX_FILE_SIZE + " или всех файлов " + Constant.MAX_REQUEST_SIZE :
//                        e.getCause().getMessage()));
//
//            }
//            doGet(req, resp);
//        }
//        else {
//            req.setAttribute("error_message", "Access denied");
//            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
//        }
//
//    }
//
//    private boolean markedAvailable(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask executorTask) {
//        return Objects.nonNull(executorTask) || !ExecutorTaskServiceImpl.INSTANCE.getFilterByUserAndDocument(currentUser, documentEdi).isEmpty();
//    }
//
//    private void setBusinessProcessAction(AbstractDocumentEdi documentEdi, ExecutorTask executorTask, User currentUser, boolean accepted, String comment, List<UploadedFile> fileList) {
//
//        if (Objects.nonNull(documentEdi) && Objects.nonNull(executorTask) && Objects.nonNull(executorTask.getProcessType())) {
//
//            for (Map.Entry<BusinessProcess, List<BusinessProcessSequence>> businessProcessListEntry : BusinessProcessSequenceServiceImpl.INSTANCE.getHistoryByDocumentMap(documentEdi).entrySet()) {
//                boolean bpCompleted = true;
//                int newTaskPosition = -1;
//
//                for (int i = 0; i < businessProcessListEntry.getValue().size(); i++) {
//
//                    BusinessProcessSequence businessProcessSequence = businessProcessListEntry.getValue().get(i);
//
//                    if (Objects.isNull(businessProcessSequence.getExecutorTask())) {
//                        bpCompleted = false;
//                    } else if ((executorTask.getProcessType().equals(businessProcessSequence.getProcessType())
//                            && currentUser.equals(businessProcessSequence.getExecutor()))
//                            && !businessProcessSequence.isCompleted()
//                            && Objects.nonNull(businessProcessSequence.getExecutorTask())) {
//
//                        ExecutorTask executorTask2 = businessProcessSequence.getExecutorTask();
//
//                        boolean isCurrentExecutorTask = executorTask.getId().equals(executorTask2.getId());
//
//                        executorTask2.setCompleted(true);
//                        executorTask2.setDateCompleted(new Timestamp(new Date().getTime()));
//                        executorTask2.setComment(comment);
//                        executorTask2.setResult(accepted ? ProcessResultServiceImpl.INSTANCE.getProcessResult(executorTask.getProcessType()) : ProcessResult.DECLINED);
//                        ExecutorTaskImpl.INSTANCE.update(executorTask2);
//
//                        if (isCurrentExecutorTask) {
//
//                            executorTask.setCompleted(executorTask2.isCompleted());
//                            executorTask.setDateCompleted(executorTask2.getDateCompleted());
//                            executorTask.setComment(executorTask2.getComment());
//                            executorTask.setResult(executorTask2.getResult());
//
//                            // I don't want to delete another executorTask which comes to this user.
//                            // And I want to bind tasks (it'll be obvious when you look at comment),
//                            // but I don't want to duplicate files, so we add files only in current executorTask
//                            if (Objects.nonNull(fileList)) {
//                                for (UploadedFile uploadedFile : fileList) {
//                                    uploadedFile.setDocument(documentEdi);
//                                    uploadedFile.setExecutorTask(executorTask2);
//                                    UploadedFileImpl.INSTANCE.save(uploadedFile);
//                                }
//                            }
//                        }
//
//                        businessProcessSequence.setCompleted(true);
//                        businessProcessSequence.setViewed(true);  // if BP completed it's already viewed
//                        businessProcessSequence.setResult(executorTask2.getResult());
//                        businessProcessSequence.setExecutorTask(executorTask2);
//                        BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence);
//                    } else if (!businessProcessSequence.isCompleted()) bpCompleted = false;
//
//                    if (((newTaskPosition + 1) == i) && businessProcessSequence.isCompleted()) newTaskPosition++;
//                }
//
//                checkAndCreateNewExecutorTasks(documentEdi, businessProcessListEntry, bpCompleted, accepted, newTaskPosition);
//
//            }
//        }
//
//    }
//
//    private void checkAndCreateNewExecutorTasks(AbstractDocumentEdi documentEdi, Map.Entry<BusinessProcess, List<BusinessProcessSequence>> businessProcessListEntry, boolean bpCompleted, boolean accepted, int newTaskPosition){
//
//        Timestamp timeStamp = new Timestamp(new Date().getTime());
//
//        BusinessProcess currentBusinessProcess = businessProcessListEntry.getKey();
//
//        if (bpCompleted || !accepted) {
//            currentBusinessProcess.setCompleted(true);
//            currentBusinessProcess.setResult(accepted ? ProcessResult.POSITIVE : ProcessResult.NEGATIVE);
//            BusinessProcessImpl.INSTANCE.update(currentBusinessProcess);
//        } else if (newTaskPosition != -1) {
//            for (int i = newTaskPosition + 1; i < businessProcessListEntry.getValue().size(); i++) {
//                BusinessProcessSequence businessProcessSequence = businessProcessListEntry.getValue().get(i);
//
//                if (Objects.isNull(businessProcessSequence.getExecutorTask())) {
//
//                    ExecutorTask executorTask2 = new ExecutorTask(timeStamp, currentBusinessProcess, false, currentBusinessProcess.getAuthor(), documentEdi, null, "", null, businessProcessSequence.getProcessType(), new Timestamp(finalDate.getTime()), businessProcessSequence.getExecutor(), false, false, false);
//                    ExecutorTaskImpl.INSTANCE.save(executorTask2);
//
//                    businessProcessSequence.setDate(timeStamp);
//                    businessProcessSequence.setExecutorTask(executorTask2);
//                    BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence);
//
//                    ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.SENT, executorTask2.getAuthor(), executorTask2, false));
//                    ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, executorTask2.getExecutor(), executorTask2, false));
//
//                    if (businessProcessSequence.getOrderBp().equals(ProcessOrderType.AFTER))
//                        i = businessProcessListEntry.getValue().size();
//                } else i = businessProcessListEntry.getValue().size();
//            }
//        }
//
//    }

}