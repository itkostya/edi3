package com.edi3.web.view.servicetools;


//import categories.User;
//import hibernate.impl.categories.UserImpl;
//import impl.categories.UserServiceImpl;
//import model.SessionParameter;
//import service_tools.CreatedataServiceImpl;
//import tools.PageContainer;

import com.edi3.service.CreatedataService;
import com.edi3.service.UserService;
import com.edi3.web.tools.PageContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;

@Controller
@RequestMapping(value = {PageContainer.ADMIN_PAGE})
public class AdminPanelController extends HttpServlet {

    private CreatedataService createdataService;
    private UserService userService;

    @Autowired
    public AdminPanelController(UserService userService, CreatedataService createdataService) {
        this.userService = userService;
        this.createdataService = createdataService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet(){

        boolean isDatabaseEmpty =  userService.isDatabaseEmpty();
        System.out.println("AdminPanelController - doGet()");

        if (isDatabaseEmpty) {
            ModelAndView model = new ModelAndView(PageContainer.ADMIN_JSP);
            model.addObject("isDatabaseEmpty", isDatabaseEmpty);
            model.addObject("newUsersCount", 0);
            return model;
        } else {
            //User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);

        }

        return null;
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    protected void doPost(@RequestParam(value = "param") Integer param){

        System.out.println("AdminPanelController - doPost()");

        switch (param) {
                case 1:
                    // Creation tables only if database is empty
                    if (userService.isDatabaseEmpty()) {
                        System.out.println("AdminPanelController - doPost() createCategories()");
                        createdataService.createCategories();
                        //CreatedataServiceImpl.createCategories();
                        // After creation user has admin access
                       // SessionParameter.INSTANCE.setCurrentUser(req, UserImpl.INSTANCE.getUserById(8L));
                    }
                    break;
            }

        doGet();

    }
}
