package ru.crystals.priceservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.crystals.priceservice.dto.MergingPricesRequestDto;
import ru.crystals.priceservice.dto.PriceDto;
import ru.crystals.priceservice.service.PriceMergingService;

import java.util.List;

@RestController
@RequestMapping("/merging-prices")
@RequiredArgsConstructor
public class PriceMergingController {
    private final PriceMergingService priceMergingService;

    @PostMapping
    public List<PriceDto> mergePrices(@RequestBody MergingPricesRequestDto request) {
        return priceMergingService.mergePrices(request.getCurrentPrices(), request.getNewPrices());
    }
}
