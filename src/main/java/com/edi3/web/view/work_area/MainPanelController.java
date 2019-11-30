package com.edi3.web.view.work_area;

//import categories.User;
//import documents.Memorandum;
//import documents.Message;
//import enumerations.FolderStructure;
//import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
//import impl.business_processes.ExecutorTaskServiceImpl;
//import impl.categories.UserServiceImpl;
//import impl.information_registers.UserAccessRightServiceImpl;
//import model.SessionParameter;
//import tools.PageContainer;

import com.edi3.core.categories.User;
import com.edi3.core.documents.Memorandum;
import com.edi3.core.documents.Message;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.service.i.business_process.ExecutorTaskFolderStructureService;
import com.edi3.service.i.business_process.ExecutorTaskService;
import com.edi3.service.i.categories.UserService;
import com.edi3.web.model.SessionParameter;
import com.edi3.web.tools.PageContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/*
 *
 */
@Controller
@RequestMapping(value = {PageContainer.WORK_AREA_PAGE})
@MultipartConfig
public class MainPanelController {

    private ExecutorTaskFolderStructureService executorTaskFolderStructureService;
    private ExecutorTaskService executorTaskService;
    private UserService userService;

    @Autowired
    public MainPanelController(ExecutorTaskFolderStructureService executorTaskFolderStructureService, ExecutorTaskService executorTaskService, UserService userService) {
        this.executorTaskFolderStructureService = executorTaskFolderStructureService;
        this.executorTaskService = executorTaskService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet(HttpServletRequest req,
                                 @RequestParam(value = "bookMark", required=false) String bookMark) {
        System.out.println("MainPanelController doGet()");

        ModelAndView model;
        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            model = new ModelAndView(PageContainer.WORK_AREA_JSP);
            setAttributesDependOnBookMark(model, req, bookMark);
        } else {
            model = new ModelAndView(PageContainer.ADMIN_JSP);
            model.addObject("error_message", "Access denied");
        }
        return model;

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView doPost(HttpServletRequest req,
         @RequestParam(value = "bookMark", required=false) String bookMark,
         @RequestParam(value = "sortColumn", required=false) String sortColumn
    ) {
        System.out.println("MainPanelController doPost()");
        System.out.println("MainPanelController doPost() bookMark = "+bookMark);
        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            SessionParameter.INSTANCE.getUserSettings(req).getMapFilter("MainPanelServlet").keySet().stream().filter(s -> Objects.nonNull(req.getParameter((s+"FilterString")))).
                    forEach( s1 -> SessionParameter.INSTANCE.getUserSettings(req).setMapFilterParameter("MainPanelServlet", s1, req.getParameter(s1+"FilterString")));

            if (Objects.isNull(bookMark)) {
                bookMark = SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", "bookMark");
            }

            StringBuilder sortColumnNumber = new StringBuilder(Objects.isNull(sortColumn) ? "" : sortColumn);
            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameterChanged("MainPanelServlet", bookMark, sortColumnNumber);
            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameter("MainPanelServlet", "bookMark", bookMark);

            return doGet(req, bookMark);

        } else {
            ModelAndView model = new ModelAndView(PageContainer.ERROR_JSP);
            model.addObject("error_message", "Access denied");
            return model;
        }
    }

    private void setAttributesDependOnBookMark(ModelAndView model, HttpServletRequest req, String bookMark) {
        User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap(Memorandum.class.getName());
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap(Message.class.getName());

        if (Objects.isNull(bookMark)) {
            bookMark = SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", "bookMark");
        }

        model.addObject("userPresentation", currentUser.getFio());
        model.addObject("bookMark", bookMark);
        model.addObject("memorandumCount", SessionParameter.INSTANCE.getUserSettings(req).getMapDocumentPropertyParameter(Memorandum.class.getName(), FolderStructure.INBOX)); // documentPropertyMap.get("Memorandum").get(FolderStructure.INBOX));
        model.addObject("messageCount", SessionParameter.INSTANCE.getUserSettings(req).getMapDocumentPropertyParameter(Message.class.getName(),FolderStructure.INBOX));    // documentPropertyMap.get("Message").get(FolderStructure.INBOX));

        switch (bookMark) {
            case "reviewTasksList":
                model.addObject("reviewTasksList",
                        executorTaskService.getReviewTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                model.addObject("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "controlledTasksList":
                model.addObject("controlledTasksList",
                        executorTaskService.getControlledTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                model.addObject("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "markedTasksList":
                System.out.println("MainPanelController setAttributesDependOnBookMark() in markedTasksList");
                model.addObject("markedTasksList",
                        executorTaskFolderStructureService.getMarkedTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                model.addObject("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "rightsList":
                //req.setAttribute("userAccessRightList",  UserAccessRightServiceImpl.INSTANCE.getUserRights(currentUser)); // TODO
                break;
            case "coworkersList":
                model.addObject("coworkersList", userService.getCoworkers(
                        SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                break;
        }

        model.addObject("filterString", SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark));

    }


}
