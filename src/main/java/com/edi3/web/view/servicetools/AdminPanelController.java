package com.edi3.web.view.servicetools;


//import categories.User;
//import hibernate.impl.categories.UserDaoImpl;
//import impl.categories.UserServiceImpl;
//import model.SessionParameter;
//import service_tools.CreatedataServiceImpl;
//import tools.PageContainer;

import com.edi3.core.categories.User;
import com.edi3.service.i.service_tools.CreatedataService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping(value = {PageContainer.ADMIN_PAGE})
public class AdminPanelController {

    private CreatedataService createdataService;
    private UserService userService;

    @Autowired
    public AdminPanelController(UserService userService, CreatedataService createdataService) {
        this.userService = userService;
        this.createdataService = createdataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet(HttpServletRequest req){
//        System.out.println("AdminPanelController - doGet()");
        boolean isDatabaseEmpty =  userService.isDatabaseEmpty();
        ModelAndView model;

        if (isDatabaseEmpty || SessionParameter.INSTANCE.adminAccessAllowed(req)) {
//            System.out.println("AdminPanelController - isDatabaseEmpty (ADMIN_JSP)");
            model = new ModelAndView(PageContainer.ADMIN_JSP);
            model.addObject("isDatabaseEmpty", isDatabaseEmpty);
            model.addObject("newUsersCount", 0);
        } else {
//            System.out.println("AdminPanelController - ERROR_JSP");
            model = new ModelAndView(PageContainer.ERROR_JSP);
            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
            model.addObject("error_message", (Objects.isNull(currentUser) ? "You should pass authorization first" : String.format("User %s doesn't have admin role", currentUser.getName())));
        }

        return model;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView doPost(@RequestParam(value = "param") Integer param, HttpServletRequest req){
//        System.out.println("AdminPanelController - doPost()");

        switch (param) {
                case 1:
                    // Creation tables only if database is empty
                    if (userService.isDatabaseEmpty()) {
                        System.out.println("AdminPanelController - doPost() createCategories()");
                        createdataService.createCategories();
                        // After creation user has admin access
                        SessionParameter.INSTANCE.setCurrentUser(req, userService.getUserById(8L));
                    }
                    break;
            }

        return doGet(req);
    }
}
