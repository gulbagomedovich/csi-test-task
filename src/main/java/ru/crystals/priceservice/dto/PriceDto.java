package ru.crystals.priceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {
    private Long id;
    @EqualsAndHashCode.Include
    private String productCode;
    @EqualsAndHashCode.Include
    private Integer number;
    @EqualsAndHashCode.Include
    private Integer depart;
    private LocalDateTime begin;
    private LocalDateTime end;
    private Long value;
}
