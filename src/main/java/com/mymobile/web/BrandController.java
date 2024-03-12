package com.mymobile.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/brands")
public class BrandController extends GenericController{

    @GetMapping("/all")
    public ModelAndView gerAllBrands(ModelAndView modelAndView) {

        return super.view("brands", modelAndView);
    }

}
