package com.mymobile.web;

import org.springframework.web.servlet.ModelAndView;

import java.time.Year;

public abstract class GenericController {

    private static final String REDIRECT = "redirect:";

    protected ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.addObject("yearStr", String.valueOf(Year.now().getValue()));
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    protected ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("yearStr", String.valueOf((Year.now().getValue())));
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    protected ModelAndView redirect(String redirectUrl) {
        return new ModelAndView(REDIRECT + redirectUrl);
    }
}