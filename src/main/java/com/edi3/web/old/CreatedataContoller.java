package com.edi3.web.view.work_area.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by kostya on 10/3/2016.
 */

@Controller
@RequestMapping(value = "/createdata")
public class CreatedataContoller {

//    private GeographyService geographyService;
//
//    @Autowired
//    public CreatedataContoller(GeographyService geographyService) {
//        this.geographyService = geographyService;
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    protected ModelAndView handleRequestInternal() {
//        ModelAndView model = new ModelAndView("createdata");
//        return model;
//    }
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST)
//    public void createData() {
//
////        Geography world = new Geography("World");
////        geographyService.registerGeography(world);
//
//        Geography countryUkraine = new Geography("Ukraine");
//        geographyService.registerGeography(countryUkraine);
//            Geography cityKiev = new Geography("Kiev", countryUkraine);
//            geographyService.registerGeography(cityKiev);
//                geographyService.registerGeography(new Geography("Khreschatik", cityKiev));
//                geographyService.registerGeography(new Geography("Grushevskogo", cityKiev));
//                geographyService.registerGeography(new Geography("Evropeiskaya", cityKiev));
//
//                    geographyService.registerGeography(new Geography("1", geographyService.getGeographyDao().getGeographyByName("Khreschatik")));
//                    geographyService.registerGeography(new Geography("2", geographyService.getGeographyDao().getGeographyByName("Khreschatik")));
//                    geographyService.registerGeography(new Geography("3", geographyService.getGeographyDao().getGeographyByName("Khreschatik")));
//
//                    geographyService.registerGeography(new Geography("1a", geographyService.getGeographyDao().getGeographyByName("Grushevskogo")));
//                    geographyService.registerGeography(new Geography("1b", geographyService.getGeographyDao().getGeographyByName("Grushevskogo")));
//                    geographyService.registerGeography(new Geography("1c", geographyService.getGeographyDao().getGeographyByName("Grushevskogo")));
//
//                    geographyService.registerGeography(new Geography("10", geographyService.getGeographyDao().getGeographyByName("Evropeiskaya")));
//                    geographyService.registerGeography(new Geography("20", geographyService.getGeographyDao().getGeographyByName("Evropeiskaya")));
//                    geographyService.registerGeography(new Geography("30", geographyService.getGeographyDao().getGeographyByName("Evropeiskaya")));
//
//            Geography cityDnipro = new Geography("Dnipro", countryUkraine);
//            geographyService.registerGeography(cityDnipro);
//                geographyService.registerGeography(new Geography("Dmitriya Yavornickogo", cityDnipro));
//                geographyService.registerGeography(new Geography("Alexandra Polya", cityDnipro));
//                geographyService.registerGeography(new Geography("Sicheslavskaya Naberezhnaya", cityDnipro));
//
//                    geographyService.registerGeography(new Geography("5", geographyService.getGeographyDao().getGeographyByName("Dmitriya Yavornickogo")));
//                    geographyService.registerGeography(new Geography("6", geographyService.getGeographyDao().getGeographyByName("Dmitriya Yavornickogo")));
//
//                    geographyService.registerGeography(new Geography("8", geographyService.getGeographyDao().getGeographyByName("Alexandra Polya")));
//                    geographyService.registerGeography(new Geography("10", geographyService.getGeographyDao().getGeographyByName("Alexandra Polya")));
//
//                    geographyService.registerGeography(new Geography("100", geographyService.getGeographyDao().getGeographyByName("Sicheslavskaya Naberezhnaya")));
//                    geographyService.registerGeography(new Geography("200", geographyService.getGeographyDao().getGeographyByName("Sicheslavskaya Naberezhnaya")));
//
//            Geography cityLviv = new Geography("Lviv", countryUkraine);
//            geographyService.registerGeography(cityLviv);
//                geographyService.registerGeography(new Geography("Svobody", cityLviv));
//                geographyService.registerGeography(new Geography("Ivana Franka", cityLviv));
//                geographyService.registerGeography(new Geography("Shevchenka", cityLviv));
//
//                    geographyService.registerGeography(new Geography("1", geographyService.getGeographyDao().getGeographyByName("Svobody")));
//                    geographyService.registerGeography(new Geography("2", geographyService.getGeographyDao().getGeographyByName("Svobody")));
//
//                    geographyService.registerGeography(new Geography("3", geographyService.getGeographyDao().getGeographyByName("Ivana Franka")));
//                    geographyService.registerGeography(new Geography("4", geographyService.getGeographyDao().getGeographyByName("Ivana Franka")));
//
//                    geographyService.registerGeography(new Geography("5", geographyService.getGeographyDao().getGeographyByName("Shevchenka")));
//                    geographyService.registerGeography(new Geography("6", geographyService.getGeographyDao().getGeographyByName("Shevchenka")));
//
//        Geography countryUSA = new Geography("USA");
//        geographyService.registerGeography(countryUSA);
//            Geography cityNY = new Geography("New York", countryUSA);
//            geographyService.registerGeography(cityNY);
//                geographyService.registerGeography(new Geography("Atlantic", cityNY));
//                geographyService.registerGeography(new Geography("Beverli", cityNY));
//                geographyService.registerGeography(new Geography("Rodgers", cityNY));
//
//                    geographyService.registerGeography(new Geography("10", geographyService.getGeographyDao().getGeographyByName("Atlantic")));
//                    geographyService.registerGeography(new Geography("20", geographyService.getGeographyDao().getGeographyByName("Atlantic")));
//
//                    geographyService.registerGeography(new Geography("30", geographyService.getGeographyDao().getGeographyByName("Beverli")));
//                    geographyService.registerGeography(new Geography("40", geographyService.getGeographyDao().getGeographyByName("Beverli")));
//
//                    geographyService.registerGeography(new Geography("50", geographyService.getGeographyDao().getGeographyByName("Rodgers")));
//                    geographyService.registerGeography(new Geography("60", geographyService.getGeographyDao().getGeographyByName("Rodgers")));
//
//            Geography cityLA = new Geography("Los Angeles", countryUSA);
//            geographyService.registerGeography(cityLA);
//                geographyService.registerGeography(new Geography("Linkoln", cityLA));
//                geographyService.registerGeography(new Geography("President", cityLA));
//                geographyService.registerGeography(new Geography("Normandie", cityLA));
//
//                    geographyService.registerGeography(new Geography("100", geographyService.getGeographyDao().getGeographyByName("Linkoln")));
//                    geographyService.registerGeography(new Geography("200", geographyService.getGeographyDao().getGeographyByName("Linkoln")));
//
//                    geographyService.registerGeography(new Geography("300", geographyService.getGeographyDao().getGeographyByName("President")));
//                    geographyService.registerGeography(new Geography("400", geographyService.getGeographyDao().getGeographyByName("President")));
//
//                    geographyService.registerGeography(new Geography("500", geographyService.getGeographyDao().getGeographyByName("Normandie")));
//                    geographyService.registerGeography(new Geography("600", geographyService.getGeographyDao().getGeographyByName("Normandie")));
//
//            Geography cityMiami = new Geography("Miami", countryUSA);
//            geographyService.registerGeography(cityMiami);
//                geographyService.registerGeography(new Geography("Biskein", cityMiami));
//                geographyService.registerGeography(new Geography("North-West 8", cityMiami));
//                geographyService.registerGeography(new Geography("South-West 10", cityMiami));
//
//                    geographyService.registerGeography(new Geography("1000", geographyService.getGeographyDao().getGeographyByName("Biskein")));
//                    geographyService.registerGeography(new Geography("2000", geographyService.getGeographyDao().getGeographyByName("Biskein")));
//
//                    geographyService.registerGeography(new Geography("3000", geographyService.getGeographyDao().getGeographyByName("North-West 8")));
//                    geographyService.registerGeography(new Geography("4000", geographyService.getGeographyDao().getGeographyByName("North-West 8")));
//
//                    geographyService.registerGeography(new Geography("5000", geographyService.getGeographyDao().getGeographyByName("South-West 10")));
//                    geographyService.registerGeography(new Geography("6000", geographyService.getGeographyDao().getGeographyByName("South-West 10")));
//    }
}
