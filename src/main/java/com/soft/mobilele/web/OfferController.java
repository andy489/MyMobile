package com.soft.mobilele.web;

import com.soft.mobilele.model.dto.OfferAddDto;
import com.soft.mobilele.model.dto.OfferSearchDto;
import com.soft.mobilele.model.user.MobileleUserDetails;
import com.soft.mobilele.service.BrandService;
import com.soft.mobilele.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/offers")
public class OfferController extends GenericController {

    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";

    private final OfferService offerService;

    private final BrandService brandService;

    public OfferController(OfferService offerService, BrandService brandService) {
        this.offerService = offerService;
        this.brandService = brandService;
    }

    @ModelAttribute(name = "offerAddModel")
    public OfferAddDto initRouteAddDto() {
        return new OfferAddDto();
    }

    @GetMapping("/add")
    public ModelAndView addOffer(ModelAndView modelAndView) {

        modelAndView.addObject("brands", brandService.getAllBrands());

        return super.view("offer-add", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addOffer(
            @Valid @ModelAttribute(name = "offerAddModel") OfferAddDto offerAddDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal MobileleUserDetails owner) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("offerAddModel", offerAddDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "offerAddModel", bindingResult);

            return super.redirect("/offers/add");
        }

        offerService.addOffer(offerAddDTO, owner.getUsername());

        return super.redirect("/offers");
    }

    @GetMapping
    public ModelAndView allOffers(
            @Valid @ModelAttribute(name = "offerSearchModel") OfferSearchDto offerSearchDTO,
            BindingResult bindingResult,
            ModelAndView modelAndView,
            @PageableDefault(page = 0, size = 10, sort = "price", direction = Sort.Direction.ASC)
            Pageable pageRequest) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("offerSearchModel", offerSearchDTO);
            modelAndView.addObject(BINDING_RESULT_PATH + "offerSearchModel", bindingResult);

            modelAndView.addObject("offersSearch", Page.empty());

            return super.view("offers", modelAndView);
        }

        modelAndView.addObject("offersSearch", offerService.getOffers(offerSearchDTO, pageRequest));

        return super.view("offers", modelAndView);
    }

    @GetMapping("/{id}/details")
    public ModelAndView offerDetails(@PathVariable("id") String offerId, ModelAndView modelAndView,
                                     @AuthenticationPrincipal MobileleUserDetails viewer) {

        modelAndView.addObject("offerDetails", offerService.getOfferDetails(offerId, viewer));

        return super.view("details", modelAndView);
    }


    @PreAuthorize("@offerService.isOwner(#offerId, #principal.username)")
    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") String offerId,
                               @AuthenticationPrincipal MobileleUserDetails principal) {

        offerService.deleteOffer(offerId);

        return super.redirect("/offers");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView onProductNotFound(NoSuchElementException noSuchElementException) {

//        modelAndView.addObject("productId", noSuchElementException.getProductId());

        return super.view("/error/404");
    }

}
