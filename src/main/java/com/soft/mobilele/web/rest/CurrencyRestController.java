package com.soft.mobilele.web.rest;


import com.soft.mobilele.model.dto.rest.ConvertRequestDto;
import com.soft.mobilele.model.dto.rest.MoneyDto;
import com.soft.mobilele.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyRestController {

    private final CurrencyService currencyService;

    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/api/currency/convert")
    public MoneyDto convert(@Valid ConvertRequestDto convertRequestDto){
        return currencyService.convert(convertRequestDto);
    }




}
