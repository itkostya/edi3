package com.edi3.web.view.work_area.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/register")
public class RegistrationController {

//    private UserService userService;
//    private UserDetailsService userDetailsService;
//    private GeographyService geographyService;
//    private User currentUser;
//
//    @Autowired
//    public RegistrationController(UserService userService, UserDetailsService userDetailsService, GeographyService geographyService) {
//        this.userService = userService;
//        this.userDetailsService = userDetailsService;
//        this.geographyService = geographyService;
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    protected ModelAndView getUserRegistrationPage(){
//
//        return new ModelAndView("registration");
//    }
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST)
//    protected User registerUser(@RequestBody User user) throws Exception {
//        if (userService.getUserByEmail(user.getEmail()) == null) {
//            currentUser = user;
//            return user;
//        }
//        return null;
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/details")
//    protected ModelAndView getUserDetailsRegistrationPage() throws Exception {
//
//        ModelAndView model = new ModelAndView("registration2");
////      List<Geography> streetsKiev =  geographyService.getGeographyDao().getElements(geographyKiev); // LAZY init
////      Set<Geography> streetsKiev2 = geographyKiev.getSetElements(); // Only FETCH init
//        model.addObject("countryList", geographyService.getCountries());
//        return model;
//    }
//
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/details")
//    protected User registerDetails(@RequestBody UserDetails userDetails) throws Exception {
//        if ( (userDetails.getGender()== null) || (userDetails.getAddress().getId() == null) || (userDetails.getPhoneNumber() == null) || ((userService.getUserByEmail(currentUser.getEmail()) != null)) )
//            return null;
//        else{
//            userService.getUserDao().saveWithDetails(currentUser, userDetails);
//            return currentUser;
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/chousencountry")
//    public List<Geography> changeGeography(@RequestBody Geography country) {
//        return geographyService.getGeographyDao().getElements(country);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getUserByEmail")
//    public User getUserByEmail(@RequestParam String email) {
//        return userService.getUserByEmail(email);
//    }

}
