package mobileoperator.operator.web;

import mobileoperator.operator.model.binding.UserLoginBindingModel;
import mobileoperator.operator.model.binding.UserRegisterBindingModel;
import mobileoperator.operator.model.model.MobileServicesServiceModel;
import mobileoperator.operator.model.model.UserServiceModel;
import mobileoperator.operator.service.MobileServiceService;
import mobileoperator.operator.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final MobileServiceService mobileServiceService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UserService userService, MobileServiceService mobileServiceService, ModelMapper modelMapper) {
        this.userService = userService;
        this.mobileServiceService = mobileServiceService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
            model.addAttribute("passwordsDontMatch", false);
        }

        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel")
                                          UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                  HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                    bindingResult);

            return "redirect:register";
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                    bindingResult);
            redirectAttributes.addFlashAttribute("passwordsDontMatch", true);
            return "redirect:register";
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        userServiceModel
                .setFullName(userRegisterBindingModel.getFirstName() + " " + userRegisterBindingModel.getLastName());

        this.userService.register(userServiceModel);

        if (httpSession.getAttribute("user") != null) {
            return "redirect:/";
        }

        return "redirect:login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
            model.addAttribute("notFound", false);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm(@Valid @ModelAttribute("userLoginBindingModel")
                                       UserLoginBindingModel userLoginBindingModel,
                               RedirectAttributes redirectAttributes,
                               HttpSession httpSession) {

        UserServiceModel user = this.userService.findByUsername(userLoginBindingModel.getUsername());

        if (user == null || !user.getPassword().equals(userLoginBindingModel.getPassword())) {
            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("notFound", true);
            return "redirect:login";
        }

        httpSession.setAttribute("user", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/search")
    public String showStudentBySurname(@RequestParam("search") String name, Model model) {
        model.addAttribute("searchUsername", name);
        model.addAttribute("search", userService.findAllWithSimilarName(name));
        model.addAttribute("services", this.mobileServiceService.findAllServices());

        return "home-admin";
    }

    @GetMapping("/details")
    public ModelAndView viewServices(@RequestParam("id") String id, ModelAndView modelAndView, HttpSession httpSession) {
        UserServiceModel userServiceModel = this.userService.findUser(id);
        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("InactiveServices", this.mobileServiceService.getInactiveServices(id));
        List<MobileServicesServiceModel> services = getMobileServicesServiceModels(userServiceModel);
        modelAndView.addObject("ActiveServices", services);
        modelAndView.addObject("totalPrice", this.userService.getTotalPrice(id));
        httpSession.setAttribute("selectedUser", userServiceModel);
        modelAndView.setViewName("users-details");
        return modelAndView;
    }

    @GetMapping("/addservice")
    public ModelAndView addService(@RequestParam("id") String id, ModelAndView modelAndView, HttpSession httpSession) {
        UserServiceModel userServiceModel = (UserServiceModel) httpSession.getAttribute("selectedUser");
        userServiceModel = this.userService.addServiceToUser(userServiceModel, id);

        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("InactiveServices", this.mobileServiceService.getInactiveServices(userServiceModel.getId()));
        List<MobileServicesServiceModel> services = getMobileServicesServiceModels(userServiceModel);
        modelAndView.addObject("ActiveServices", services);
        modelAndView.addObject("totalPrice", this.userService.getTotalPrice(userServiceModel.getId()));
        httpSession.setAttribute("selectedUser", userServiceModel);
        modelAndView.setViewName("users-details");

        return modelAndView;
    }

    @GetMapping("/changerole")
    public String changeRole(@RequestParam("id") String id) {
        this.userService.changeRole(id);

        return "redirect:/";
    }

    @GetMapping("/pay")
    public String pay(@RequestParam("id") String id, HttpSession httpSession) {
        this.userService.pay(id);
        UserServiceModel userServiceModel = this.userService.findUser(id);
        httpSession.setAttribute("user", userServiceModel);
        return "redirect:/";
    }

    private List<MobileServicesServiceModel> getMobileServicesServiceModels(UserServiceModel userServiceModel) {
        return userServiceModel.getServices().stream().sorted((f, s) -> {
            int result = 0;
            result = f.getCategory().compareTo(s.getCategory());
            if (result == 0) {
                result = f.getName().compareTo(s.getName());
            }
            return result;
        }).collect(Collectors.toList());
    }
}
