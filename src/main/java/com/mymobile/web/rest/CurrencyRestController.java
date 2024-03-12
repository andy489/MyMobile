package com.mymobile.web.rest;


import com.mymobile.model.dto.rest.ConvertRequestDto;
import com.mymobile.model.dto.rest.MoneyDto;
import com.mymobile.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyRestController {

    private final CurrencyService currencyService;

    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Converts BGN to a target target")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returned when we successfully converted the target",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MoneyDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no information about this target",
                    content = @Content
            )
    })
    @Parameter(name = "target", description = "The target target", required = true)
    @Parameter(name = "amount", description = "The amount to be converted", required = true)
    @GetMapping("/api/currency/convert")
    public MoneyDto convert(@Valid ConvertRequestDto convertRequestDto) {
        return currencyService.convert(convertRequestDto);
    }

}
