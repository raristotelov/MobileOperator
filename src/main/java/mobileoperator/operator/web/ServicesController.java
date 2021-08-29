package mobileoperator.operator.web;

import mobileoperator.operator.model.binding.ServiceAddBindingModel;
import mobileoperator.operator.model.model.MobileServicesServiceModel;
import mobileoperator.operator.service.MobileServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/services")
public class ServicesController {
    private final MobileServiceService mobileServiceService;
    private final ModelMapper modelMapper;

    @Autowired
    public ServicesController(MobileServiceService mobileServiceService, ModelMapper modelMapper) {
        this.mobileServiceService = mobileServiceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public String create(Model model) {
        if(!model.containsAttribute("serviceAddBindingModel")){
            model.addAttribute("serviceAddBindingModel", new ServiceAddBindingModel());
        }

        return "service-create";
    }

    @PostMapping("/create")
    public String createConfirm(@Valid @ModelAttribute("serviceAddBindingModel")
                                        ServiceAddBindingModel serviceAddBindingModel,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("serviceAddBindingModel", serviceAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.serviceAddBindingModel",
                    bindingResult);

            return "redirect:create";
        }

        this.mobileServiceService.save(this.modelMapper.map(serviceAddBindingModel, MobileServicesServiceModel.class));

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") String id){
        this.mobileServiceService.delete(id);

        return "redirect:/";
    }
}
