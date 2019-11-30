package com.edi3.web.view.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.app_info.Constant;
import com.edi3.core.app_info.TimeModule;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.core.categories.User;
import com.edi3.core.documents.DocumentProperty;
import com.edi3.core.documents.Memorandum;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.service.i.business_process.BusinessProcessService;
import com.edi3.service.i.business_process.ExecutorTaskService;
import com.edi3.service.i.documents.MemorandumService;
import com.edi3.service.i.categories.UserService;
import com.edi3.web.model.ElementStatus;
import com.edi3.web.model.SessionDataElement;
import com.edi3.web.model.SessionParameter;
import com.edi3.web.tools.CommonModule;
import com.edi3.web.tools.PageContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/*
 * 
 */

//@WebServlet(urlPatterns = {PageContainer.DOCUMENT_MEMORANDUM_CREATE_PAGE})
//@MultipartConfig(
//        fileSizeThreshold = Constant.FILE_SIZE_THRESHOLD,
//        maxFileSize = Constant.MAX_FILE_SIZE,
//        maxRequestSize = Constant.MAX_REQUEST_SIZE)

@Controller
@RequestMapping(value = {PageContainer.DOCUMENT_MEMORANDUM_CREATE_PAGE})
public class MemorandumCreationController{

    private BusinessProcessService businessProcessService;
    private ExecutorTaskService executorTaskService;
    private MemorandumService memorandumService;
    private UserService userService;

    private java.sql.Date finalDate = TimeModule.getFinalDateOfProcess();
    private List<User> userList;  // TODO - it was static

    @Autowired
    public MemorandumCreationController(BusinessProcessService businessProcessService, ExecutorTaskService executorTaskService, MemorandumService memorandumService, UserService userService) {
        this.businessProcessService = businessProcessService;
        this.executorTaskService = executorTaskService;
        this.memorandumService = memorandumService;
        this.userService = userService;
        this.userList =  userService.getUsers();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet(HttpServletRequest req) {
//        System.out.println("MemorandumCreationController - doGet() begin");

        ModelAndView model;
        Memorandum documentEdi;

        if (SessionParameter.INSTANCE.accessAllowed(req)) {
//            System.out.println("MemorandumCreationController - accessAllowed");
            model = new ModelAndView(PageContainer.DOCUMENT_MEMORANDUM_CREATE_JSP);
            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);

            model.addObject("finalDate", finalDate);
            model.addObject("userList", userList);
            model.addObject("documentTypeId", DocumentProperty.MEMORANDUM.getId());  // TODO - Change it

            Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

            if (sessionDataElement.getElementStatus() == ElementStatus.CREATE
                    || sessionDataElement.getElementStatus() == ElementStatus.ERROR
                    || sessionDataElement.getElementStatus() == ElementStatus.STORE
                    ) {
                documentEdi = null;

                Long documentId = (Long) CommonModule.getNumberFromRequest(req, "documentId", Long.class);
                if (Objects.nonNull(documentId)) {
                    // Work with draft
                    documentEdi = memorandumService.getById(documentId);
                    sessionDataElement.setDocumentEdi(documentEdi);
                    if (Objects.nonNull(executorTaskService.getDraft(currentUser, documentEdi))) {
                        sessionDataElement.setElementStatus(ElementStatus.CREATE);
                    }
                } else if (Objects.nonNull(sessionDataElement.getDocumentEdi())) {
                    documentEdi = (Memorandum) sessionDataElement.getDocumentEdi();
                }

                if (Objects.nonNull(documentEdi)) {
                    // Get for draft
                    model.addObject("theme", documentEdi.getTheme());
                    model.addObject("textInfo", documentEdi.getText());
                    User whomUser = documentEdi.getWhom();
                    if (Objects.nonNull(whomUser)) {
                        model.addObject("whomId", whomUser.getId());
                        model.addObject("selectedUser", whomUser.getFio());
                    }
//                    req.setAttribute("uploadedFiles", UploadedFileServiceImpl.INSTANCE.getListByDocument(documentEdi));
                }

                if (sessionDataElement.getElementStatus() == ElementStatus.ERROR)
                    model.addObject("infoResult", sessionDataElement.getErrorMessage());

                // For review
                model.addObject("docDate", new SimpleDateFormat("dd.MM.yyyy").format(Objects.nonNull(documentEdi) ? documentEdi.getDate() : TimeModule.getCurrentDate()));
                model.addObject("docNumber", Objects.nonNull(documentEdi) ? documentEdi.getNumber() : "БЕЗ НОМЕРА");
                if (Objects.nonNull(currentUser)) {
                    if (Objects.nonNull(currentUser.getPosition())) {
                        model.addObject("positionFrom", currentUser.getPosition().getName());
                    }
                    model.addObject("userFrom", currentUser.getFioInitials());
                }
                model.addObject("docType", DocumentProperty.MEMORANDUM.getRuName());

            }
            model.addObject("tempId", tempId);
            model.addObject("sessionDataElement", sessionDataElement);
        } else {
//            System.out.println("MemorandumCreationController - !accessAllowed");
            model = new ModelAndView(PageContainer.ERROR_JSP);
            model.addObject("error_message", "Access denied");
        }
//        System.out.println("MemorandumCreationController - doGet() end");
        return model;
    }

    // https://docs.spring.io/spring/docs/4.0.4.RELEASE/spring-framework-reference/htmlsingle/#mvc-multipart
    // https://netjs.blogspot.com/2018/09/spring-mvc-file-upload-multipart-request-example.html

    //  =============================> WORK <=================
    @RequestMapping(value = "/create5", method = RequestMethod.POST)
    public String create5(
            @RequestParam(value="theme", required=false) String theme,
            @RequestParam(value="uploadedFileString", required=false) String uploadedFileString,
            @RequestParam(value="file", required=false) Part file
    ) {
        System.out.println("Creating contact5 - begin");
        if (file != null) {
            System.out.println("File name: " + file.getName());
            System.out.println("File size: " + file.getSize());
        }
        System.out.println("theme = " + theme);
        System.out.println("uploadedFileString = " + uploadedFileString);
        System.out.println("Creating contact5 - end");
        return "redirect:/doc_memorandum_create/";
    }

//    <input type="hidden" name="post_users[]" value="x" class="post_users" id="post_users"/>
//    <input type="hidden" name="post_order_type[]" value="0" class="post_order_type" id="post_order_type"/>
//    <input type="hidden" name="process_type" value="0" class="process_type" id="process_type"/>
//    <input type="hidden" name="post_process_type[]" value="0" class="post_process_type" id="post_process_type"/>
//    <input type="hidden" name="theme" value="x" id="theme"/>
//    <input type="hidden" name="textInfo" value="x" id="textInfo"/>
//    <input type="hidden" name="param" value="send" id="param"/>
//    <input type="hidden" name="whomId" value="x" id="whomId"/>

    @RequestMapping(value = "/post2", method = RequestMethod.POST)
    protected ModelAndView doPost(
            @RequestParam(value="tempId", required=false) Long tempId,
            @RequestParam(value="post_users[]", required=false) String postUsers,
            @RequestParam(value="post_order_type[]", required=false) String postOrderType,
            @RequestParam(value="process_type", required=false) Integer processTypeParameter,  // scenario - a lot of different process'
            @RequestParam(value="post_process_type[]", required=false) String postProcessType,
            @RequestParam(value="closeDocument", required=false) String closeDocumentString,
            @RequestParam(value="finalDate", required=false) String finalDateString,
            @RequestParam(value="theme", required=false) String theme,
            @RequestParam(value="textInfo", required=false) String textInfo,
            @RequestParam(value="param", required=false) String param,
            @RequestParam(value="whomId", required=false) Long whomId,
            @RequestParam(value="comment", required=false) String comment,
            @RequestParam(value="uploadedFileString", required=false) String uploadedFileString,
            @RequestParam(value="fileList[]", required=false) Part file,
            HttpServletRequest req
    ) throws ServletException, IOException {

        System.out.println("MemorandumCreationController - doPost() begin");

        System.out.println("theme ="+theme);
        System.out.println("textInfo ="+textInfo);
        System.out.println("whomId ="+whomId);
        System.out.println("tempId ="+tempId);
        System.out.println("uploadedFileString ="+uploadedFileString);
        if (file!= null) {
            System.out.println("file=" + file.getSize());
        }

        ModelAndView model;
        AbstractDocumentEdi documentEdi = null;
        ExecutorTask executorTask = null;

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            model = new ModelAndView(PageContainer.DOCUMENT_MEMORANDUM_CREATE_JSP);
            System.out.println("MemorandumCreationController - doPost() after create jsp");
            try {
                System.out.println("MemorandumCreationController - doPost() before get parts");
                req.getParts();
                System.out.println("MemorandumCreationController - doPost() before after param = "+param);

                if (Objects.nonNull(param)) {
                    System.out.println("MemorandumCreationController - doPost() nonNull(param)");

                    java.sql.Timestamp timeStamp = TimeModule.getCurrentDate();
                    User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
                    User whomUser = (Objects.isNull(whomId) ? null : userService.getUserById(whomId));
                    SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

                    try {
//                        System.out.println("MemorandumCreationController - doPost() try");
                        if ((Objects.nonNull(sessionDataElement) && Objects.nonNull(sessionDataElement.getDocumentEdi()))) {
                            documentEdi = sessionDataElement.getDocumentEdi();
                            if (Objects.nonNull(documentEdi)) {
                                System.out.println("MemorandumCreationController - doPost() Check if draft exists");
                                // Check if draft exists
                                executorTask =  executorTaskService.getDraft(currentUser, documentEdi);
                            }
                        }

                        List<UploadedFile> fileList = CommonModule.getFileListFromRequest(req, "fileList[]"); // new files from client
                        System.out.println("MemorandumCreationController - doPost() before switch");
                        switch (param) {

                            case "save":
                                documentEdi = memorandumService.createOrUpdateDocument((Memorandum) documentEdi, timeStamp, currentUser, theme, textInfo, fileList, whomUser, "save");
                                if (Objects.isNull(executorTask)) {
                                    businessProcessService.createDraftExecutorTask(currentUser, documentEdi, timeStamp, new Timestamp(finalDate.getTime()));
                                }
                                sessionDataElement.setElementStatus(ElementStatus.CREATE);
                                break;

                            case "send":
                                System.out.println("MemorandumCreationController - doPost() send");
                                Map<String, Object> sendParams = parseParams(postUsers, postOrderType, processTypeParameter,
                                        postProcessType, closeDocumentString, comment, finalDateString);
                               documentEdi = sendData(req, sessionDataElement, documentEdi, timeStamp, currentUser, theme, textInfo, fileList, whomUser, executorTask, sendParams);
                               break;
                        }
                        System.out.println("MemorandumCreationController - doPost() after switch");

                    } catch (ConstraintViolationException e) {
                        sessionDataElement.setElementStatus(ElementStatus.ERROR);
                        sessionDataElement.setErrorMessage("Документ не создан (ConstraintViolationException): " +
                                e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
                        System.out.println("ERROR in ConstraintViolationException: "+e.getCause().getMessage());
                    } catch (PersistenceException e) {
                        sessionDataElement.setElementStatus(ElementStatus.ERROR);
                        sessionDataElement.setErrorMessage("PersistenceException: " +
                                e.getMessage());
                        System.out.println("ERROR in PersistenceException: "+e.getCause().getMessage());
                    }

                    DocumentCreate.setResultDocumentCreation(req, sessionDataElement, documentEdi);

                }
            }
            catch (IllegalStateException e) {
                model.addObject("infoResult",(e.getCause().getMessage().contains("exceeds its maximum permitted size") ?
                            "Ошибка (превышен максимальный размер файла "+Constant.MAX_FILE_SIZE +" или всех файлов "+Constant.MAX_REQUEST_SIZE :
                            e.getCause().getMessage()));
                System.out.println("ERROR in IllegalStateException: "+e.getCause().getMessage());
                }


        } else {
            model = new ModelAndView(PageContainer.ERROR_JSP);
            model.addObject("error_message", "Access denied");
        }

        System.out.println("MemorandumCreationController - doPost() end");
        return model;
    }

    // TODO - Should be in service or validation parameters doPost
    private Map<String, Object> parseParams(String postUsers, String postOrderType, Integer processTypeParameter,
                            String postProcessType, String closeDocumentString, String comment, String finalDateString) {

        //        @RequestParam(value = "post_users[]", required = false) String postUsers,
        //        @RequestParam(value = "post_order_type[]", required = false) String postOrderType,
        //        @RequestParam(value = "process_type", required = false) String processType,
        //        @RequestParam(value = "post_process_type[]", required = false) String postProcessType,
        //        @RequestParam(value="closeDocument", required=false) String closeDocument,

        Map<String, Object> result = new HashMap();
        String[] usersIdArray = "".equals(postUsers) ? null : postUsers.split(",");
        String[] orderTypeArray = "".equals(postOrderType) ? null : postOrderType.split(",");
        Boolean closeDocument = (Objects.nonNull(closeDocumentString) && "on".equals(closeDocumentString));

        String[] processTypeArray = null;
        ProcessType processTypeCommon = null;
        if (processTypeParameter == Constant.SCENARIO_NUMBER) {
            processTypeArray = postProcessType.split(",");
        } else {
            processTypeCommon = DocumentProperty.MEMORANDUM.getProcessTypeList().get(processTypeParameter);
        }

        if (Objects.nonNull(comment)) {
            comment = CommonModule.getCorrectStringForWeb(comment);
        }

        // TODO - Should be a class
        result.put("usersIdArray", usersIdArray);
        result.put("orderTypeArray", orderTypeArray);
        result.put("closeDocument", closeDocument);
        result.put("processTypeArray", processTypeArray);
        result.put("processTypeCommon", processTypeCommon);
        result.put("comment", comment);
        result.put("finalDate", new java.sql.Timestamp(java.sql.Date.valueOf(finalDateString).getTime())); //java.sql.Date.valueOf("2015-01-21")

        // Delete it
//        System.out.println("MemorandumCreationController - sendData() parseParams: ");
//        for (Map.Entry<String, Object> entry : result.entrySet()) {
//            System.out.println("result.param =  "+entry.getKey() + ":" + entry.getValue().toString());
//        }

        return result;
    }

    private AbstractDocumentEdi sendData(HttpServletRequest req, SessionDataElement sessionDataElement, AbstractDocumentEdi documentEdi,
                                         java.sql.Timestamp timeStamp, User currentUser, String theme, String textInfo, List<UploadedFile> fileList, User whomUser, ExecutorTask executorTask,
                                         Map<String, Object> sendParams){

        System.out.println("MemorandumCreationController - sendData() begin");

        if (Objects.isNull(sendParams.get("usersIdArray"))) {
            sessionDataElement.setElementStatus(ElementStatus.ERROR);
            sessionDataElement.setErrorMessage("Не выбран(ы) получатели документа");
        } else {

            documentEdi = memorandumService.createOrUpdateDocument((Memorandum) documentEdi, timeStamp, currentUser, theme, textInfo, fileList, whomUser, "send");
            businessProcessService.createAndStartBusinessProcess(currentUser, documentEdi, executorTask, timeStamp,
                    (String[]) sendParams.get("usersIdArray"),
                    (String[]) sendParams.get("orderTypeArray"),
                    (String[]) sendParams.get("processTypeArray"),
                    (ProcessType) sendParams.get("processTypeCommon"),
                    (String) sendParams.get("comment"),
                    (java.sql.Timestamp) sendParams.get("finalDate"));
            sessionDataElement.setElementStatus((Boolean)sendParams.get("closeDocument") ? ElementStatus.CLOSE : ElementStatus.STORE);
        }

        System.out.println("MemorandumCreationController - sendData() end");

        return documentEdi;
    }
}
