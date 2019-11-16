package com.edi3.service.impl.service_tools;

//import categories.*;
//import hibernate.impl.categories.AbstractCategoryImpl;
//import hibernate.impl.categories.DepartmentImpl;
//import hibernate.impl.categories.PositionImpl;
//import hibernate.impl.categories.UserImpl;
//import impl.information_registers.UserAccessRightServiceImpl;

import com.edi3.core.categories.Department;
import com.edi3.core.categories.Position;
import com.edi3.core.categories.User;
import com.edi3.core.categories.UserRole;
import com.edi3.dao.DepartmentDao;
import com.edi3.dao.PositionDao;
import com.edi3.dao.UserDao;
import com.edi3.service.CreatedataService;

public class CreatedataServiceImpl implements CreatedataService {

    private DepartmentDao departmentDao;
    private PositionDao positionDao;
    private UserDao userDao;

    public void createCategories() {

        // Departments
        Department departmentIt = new Department("It");
        departmentDao.save(departmentIt);

        Department departmentHr = new Department("HR");
        departmentDao.save(departmentHr);

        // Positions
        Position positionItDirector = new Position("It Director");
        positionDao.save(positionItDirector);
        Position positionProgrammer = new Position("Programmer");
        positionDao.save(positionProgrammer);
        Position positionAdmin = new Position("System Engineer");
        positionDao.save(positionAdmin);

        Position positionHrDirector = new Position("HR Director");
        positionDao.save(positionHrDirector);
        Position positionHrManager = new Position("HR Manager");
        positionDao.save(positionHrManager);

        // Users
        User user;
        user = new User("admin", false, null, false, "Gates", "William", "Henry", "admin", "MS", "12345", null, null, UserRole.ADMIN);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("kostya", false, null, false, "Zhurov", "Kostyantin", "Oleksandrovich", "kostya", "DC", null, null, null, null);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("itkostya", false, null, false, "Журов", "Константин", "Александрович", "itkostya", null, "123", positionProgrammer, departmentIt, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("it_director", false, null, false, "Чегалкин", "Сергей", "Викторович", "it_director", null, "123", positionItDirector, departmentIt, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("it_admin1", false, null, false, "Капитошка", "Виталий", "Владимирович", "it_admin1", null, "123", positionAdmin, departmentIt, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("it_admin2", false, null, false, "Рябчиков", "Сергей", "Владимирович", "it_admin2", null, "123", positionAdmin, departmentIt, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("hr_director", false, null, false, "Drocenko", "Ekaterina", "Valerievna", "hr_director", null, "234", positionHrDirector, departmentHr, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("hr_manager1", false, null, false, "Kruglenko", "Olga", "Aleksandrovna", "hr_manager1", null, "234", positionHrManager, departmentHr, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
        user = new User("hr_manager2", false, null, false, "Karpova", "Julia", "Aleksandrovna", "hr_manager2", null, "234", positionHrManager, departmentHr, UserRole.USER);
        userDao.save(user);
//        UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights(user);
//
//        AbstractCategoryImpl.INSTANCE.save(new Contractor("Сильпо-Фуд ООО", false, null, false, "407201926538", "407201926538"));
//        AbstractCategoryImpl.INSTANCE.save(new Contractor("РТЦ Варус-8 ТОВ (Днепр)", false, null, false, "33184262", "331842604637"));
//        AbstractCategoryImpl.INSTANCE.save(new Contractor("АТБ маркет", false, null, false, "30487219", "304872104175"));
//
//        AbstractCategoryImpl.INSTANCE.save(new CostItem("Коммуналка"));
//        AbstractCategoryImpl.INSTANCE.save(new CostItem("Маркетинг и реклама"));
//        AbstractCategoryImpl.INSTANCE.save(new CostItem("Затраты на персонал"));
//
//        AbstractCategoryImpl.INSTANCE.save(new Currency("USD", false, 840L, false));
//        AbstractCategoryImpl.INSTANCE.save(new Currency("EUR", false, 978L, false));
//        AbstractCategoryImpl.INSTANCE.save(new Currency("UAH", false, 980L, false));
//
//        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("АКВАМИНЕРАЛЕ ООО"));
//        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("Национальный продукт ООО"));
//        AbstractCategoryImpl.INSTANCE.save(new LegalOrganization("Чегалкин ЧП"));
//
//        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Ноябрь 2017", Date.valueOf("2017-11-01"), Date.valueOf("2017-11-30") ));
//        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Декабрь 2017", Date.valueOf("2017-12-01"), Date.valueOf("2017-12-31")));
//        AbstractCategoryImpl.INSTANCE.save(new PlanningPeriod("Январь 2018", Date.valueOf("2018-01-01"), Date.valueOf("2018-01-31")));

    }

    public DepartmentDao getDepartmentDao() {
        return departmentDao;
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public PositionDao getPositionDao() {
        return positionDao;
    }

    public void setPositionDao(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
