package mobileoperator.operator.web;

import mobileoperator.operator.model.model.UserServiceModel;
import mobileoperator.operator.service.MobileServiceService;
import mobileoperator.operator.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    private final UserService userService;
    private final MobileServiceService mobileServiceService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, MobileServiceService mobileServiceService, ModelMapper modelMapper) {
        this.userService = userService;
        this.mobileServiceService = mobileServiceService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/")
    public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
        if (httpSession.getAttribute("user") == null) {
            modelAndView.setViewName("index");
        } else {
            UserServiceModel userServiceModel = (UserServiceModel) httpSession.getAttribute("user");
            if (userServiceModel.getRole().equals("OPERATOR")) {
                modelAndView.addObject("users", this.userService.findAllUsers());
                modelAndView.addObject("services", this.mobileServiceService.findAllServices());
                modelAndView.setViewName("home-admin");
            } else {
                modelAndView.addObject("user", userServiceModel);
                modelAndView.addObject("services", this.mobileServiceService
                        .getInactiveServices(userServiceModel.getId()));
                modelAndView.addObject("totalPrice", this.userService.getTotalPrice(userServiceModel.getId()));
                modelAndView.setViewName("home-user");
            }
        }

        return modelAndView;
    }
}
