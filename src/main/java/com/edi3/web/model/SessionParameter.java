package com.edi3.web.model;

//import categories.User;
//import categories.UserRole;
import com.edi3.core.categories.User;
import com.edi3.core.categories.UserRole;
import com.edi3.web.tools.PageContainer;
//import impl.information_registers.UserAccessRightServiceImpl;
//import tools.PageContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;


public enum SessionParameter {

    INSTANCE;

    private final Map<String, UserSettings> activeUsers = new TreeMap<>();

    public User getCurrentUser(HttpServletRequest req) {
//        System.out.println("SessionParameter.getCurrentUser() -> req.getRequestedSessionId() = "+req.getRequestedSessionId());
        UserSettings userSettings = activeUsers.get(req.getRequestedSessionId());
        return Objects.isNull(userSettings) ? null : userSettings.getUser();
    }

    public void setCurrentUser(HttpServletRequest req, User currentUser) {
        activeUsers.put(req.getRequestedSessionId(), new UserSettings(currentUser));
    }

    public UserSettings getUserSettings(HttpServletRequest req) {
        return activeUsers.get(req.getRequestedSessionId());
    }

    public boolean accessAllowed(HttpServletRequest req) {

        if (activeUsers.containsKey(req.getRequestedSessionId())) {
//            System.out.println("SessionParameter accessAllowed() activeUsers.containsKey req.getRequestURI() = "+req.getRequestURI());

            switch (req.getRequestURI()) {
                case PageContainer.USER_PAGE:
                case PageContainer.WORK_AREA_PAGE:
                case PageContainer.DOWNLOAD_PAGE:
                case PageContainer.ADMIN_PAGE:
//                    System.out.println("SessionParameter accessAllowed() activeUsers.containsKey req.getRequestURI() 1");
                    return true;
                case PageContainer.EXECUTOR_TASK_PAGE:
//                    System.out.println("SessionParameter accessAllowed() activeUsers.containsKey req.getRequestURI() 2");
                    // If user get executor task - he can work with it
                    return true;
                default:
//                    System.out.println("SessionParameter accessAllowed() activeUsers.containsKey req.getRequestURI() 3");
                    User currentUser = getCurrentUser(req);
                    if (Objects.nonNull(currentUser) && currentUser.getRole() == UserRole.ADMIN) {
                        return true;
                    }
                    // Doesn't matter what rights set up - only admin can edit users' data
                    else if (PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE.equals(req.getRequestURI())
                            || PageContainer.CATEGORY_USER_ELEMENT_PAGE.equals(req.getRequestURI())
                            || PageContainer.CATEGORY_USER_JOURNAL_PAGE.equals(req.getRequestURI())
                            ) {
                        return false;
                    }

                    if (Objects.isNull(currentUser != null ? currentUser.getId() : null) &&
                            (PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE.equals(req.getRequestURI()) || PageContainer.CATEGORY_POSITION_CHOICE_PAGE.equals(req.getRequestURI()))){
                        return true;
                    }

                    return true; // TODO - replace with text below
//                    Boolean viewAccess = UserAccessRightServiceImpl.INSTANCE.viewAccess(currentUser, PageContainer.getMetadataTypeProperty(req.getRequestURI()));
//                    // It can be simplified to:
//                    // return viewAccess && (!PageContainer.isEditablePage(req.getRequestURI()) || UserAccessRightServiceImpl.INSTANCE.editAccess(currentUser, PageContainer.getMetadataTypeProperty(req.getRequestURI())));
//                    // but it's not convenient during developing
//                    if (viewAccess) {
//                        if (PageContainer.isEditablePage(req.getRequestURI())) {
//                            return UserAccessRightServiceImpl.INSTANCE.editAccess(currentUser, PageContainer.getMetadataTypeProperty(req.getRequestURI()));
//                        }
//                        return true;
//                    }
//                    else
//                        return false;
            }
        }
//        System.out.println("SessionParameter accessAllowed() end");
        return false;
    }

    public boolean adminAccessAllowed(HttpServletRequest req) {
        if (accessAllowed(req)){
            User currentUser = getCurrentUser(req);
//            System.out.println("SessionParameter adminAccessAllowed() Objects.nonNull(currentUser) = "+Objects.nonNull(currentUser) );
            if (Objects.nonNull(currentUser)) {
//                System.out.println("SessionParameter adminAccessAllowed() currentUser.getRole() == UserRole.ADMIN = " + (currentUser.getRole() == UserRole.ADMIN));
            }
            return (Objects.nonNull(currentUser) && currentUser.getRole() == UserRole.ADMIN);
        }
        return false;
    }

}
