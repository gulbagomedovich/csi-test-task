package ru.crystals.priceservice.service;

import ru.crystals.priceservice.dto.PriceDto;

import java.util.List;

public interface PriceMergingService {
    List<PriceDto> mergePrices(List<PriceDto> currentPrices, List<PriceDto> newPrices);
}
