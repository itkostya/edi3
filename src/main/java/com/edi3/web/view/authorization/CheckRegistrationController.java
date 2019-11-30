package com.edi3.web.view.authorization;

//import categories.User;
//import documents.DocumentProperty;
//import hibernate.impl.categories.UserImpl;
//import impl.categories.UserServiceImpl;
//import model.SessionParameter;
//import tools.PageContainer;

import com.edi3.core.categories.User;
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


@Controller
@RequestMapping(value = {PageContainer.USER_PAGE})
public class CheckRegistrationController {

    private UserService userService;

    @Autowired
    public CheckRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet() {

        if (userService.isDatabaseEmpty()){
            //resp.sendRedirect(PageContainer.ADMIN_PAGE);
//            System.out.println("CheckRegistrationController (user page) redirect");
            return new ModelAndView("redirect:"+PageContainer.ADMIN_PAGE);
        }else {
            ModelAndView model = new ModelAndView(PageContainer.USER_JSP);
            model.addObject("userList", userService.getUsers());
            return model;
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView doPost(@RequestParam(value = "login") String login,
                          @RequestParam(value = "pass") String password,
                          HttpServletRequest req){

//        System.out.println("CheckRegistrationController doPost()");

        User userClient = new User(login, password);
        ModelAndView model;

        if (userService.isUserExist(userClient)) {
            if (userService.isPasswordCorrect(userClient)) {
//                System.out.println("CheckRegistrationController isPasswordCorrect()");

                User databaseUser =  userService.getUserByLogin(userClient.getLogin());
                SessionParameter.INSTANCE.setCurrentUser(req, databaseUser);

                model = SessionParameter.INSTANCE.adminAccessAllowed(req) ?
                        new ModelAndView("redirect:"+PageContainer.ADMIN_PAGE) :
                        new ModelAndView("redirect:"+PageContainer.WORK_AREA_PAGE);
//                System.out.println("CheckRegistrationController doPost() userClient = "+userClient+"SessionParameter.INSTANCE.adminAccessAllowed(req) = "+ SessionParameter.INSTANCE.adminAccessAllowed(req));

                // TODO - Check is it necessary? After redirection it has http://localhost:8080/work_area/UserPresentation = something & Bookmark = 1 - I don't want it
//                model.addObject("UserPresentation", databaseUser.getFio());
//                model.addObject("bookmark", "1");
//                req.setAttribute("DocumentPropertyList", DocumentProperty.values());

            } else {
//                System.out.println("CheckRegistrationController !isPasswordCorrect()");
                model = new ModelAndView(PageContainer.ERROR_JSP);
                model.addObject("error_message", String.format("Password for user %s is not correct", userClient.getLogin()));
            }
        } else {
//            System.out.println("CheckRegistrationController !isUserExist()");
            model = new ModelAndView(PageContainer.ERROR_JSP);
            model.addObject("error_message", String.format("User %s does not exist", userClient.getLogin()));
        }
        return model;
    }
}
