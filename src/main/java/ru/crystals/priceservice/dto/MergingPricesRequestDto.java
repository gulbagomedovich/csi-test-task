package ru.crystals.priceservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class MergingPricesRequestDto {
    private List<PriceDto> currentPrices;
    private List<PriceDto> newPrices;
}
