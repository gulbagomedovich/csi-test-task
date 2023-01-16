package ru.crystals.priceservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.crystals.priceservice.dto.PriceDto;
import ru.crystals.priceservice.service.PriceMergingService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PriceMergingServiceImpl implements PriceMergingService {
    @Override
    public List<PriceDto> mergePrices(List<PriceDto> currentPrices, List<PriceDto> newPrices) {
        if (CollectionUtils.isEmpty(currentPrices) && CollectionUtils.isEmpty(newPrices)) {
            return Collections.emptyList();
        }

        if (CollectionUtils.isEmpty(currentPrices)) {
            return newPrices;
        }

        if (CollectionUtils.isEmpty(newPrices)) {
            return currentPrices;
        }

        Map<PriceDto, List<PriceDto>> mergedPrices = Stream.concat(currentPrices.stream(), newPrices.stream())
                .sorted(Comparator.comparing(PriceDto::getBegin))
                .collect(Collectors.toMap(Function.identity(), Arrays::asList, this::merge));

        return mergedPrices.values().stream()
                .flatMap(Collection::stream)
                .sorted(
                        Comparator.comparing(PriceDto::getProductCode)
                                .thenComparing(PriceDto::getNumber)
                                .thenComparing(PriceDto::getDepart)
                                .thenComparing(PriceDto::getBegin)
                )
                .collect(Collectors.toList());
    }

    private List<PriceDto> merge(List<PriceDto> list1, List<PriceDto> list2) {
        List<PriceDto> mergedPrices = new LinkedList<>(list1);
        int currentIndex = list1.size() - 1;

        PriceDto current = list1.get(currentIndex);
        PriceDto next = list2.get(0);

        if (pricesOverlap(current, next)) {
            // Если в текущий интервал не входит следующий
            if (current.getEnd().isBefore(next.getEnd())) {
                if (Objects.equals(current.getValue(), next.getValue())) {
                    current.setEnd(next.getEnd());
                } else {
                    if (Objects.isNull(next.getId())) {
                        current.setEnd(next.getBegin());
                    } else {
                        next.setBegin(current.getEnd());
                    }

                    mergedPrices.add(next);
                }
            } else if (!Objects.equals(current.getValue(), next.getValue()) && Objects.isNull(next.getId())) {
                PriceDto price = new PriceDto();
                price.setProductCode(current.getProductCode());
                price.setNumber(current.getNumber());
                price.setDepart(current.getDepart());
                price.setBegin(next.getEnd());
                price.setEnd(current.getEnd());
                price.setValue(current.getValue());

                current.setEnd(next.getBegin());
                mergedPrices.addAll(Arrays.asList(next, price));
            }
        } else {
            mergedPrices.add(next);
        }

        // Удаляем текущий обработанный прайс с нулевым или отрицательным сроком действия
        Duration duration = Duration.between(current.getBegin(), current.getEnd());
        if (duration.isNegative() || duration.isZero()) {
            mergedPrices.remove(currentIndex);
        }

        return mergedPrices;
    }

    private boolean pricesOverlap(PriceDto price1, PriceDto price2) {
        return price1.getBegin().isBefore(price2.getEnd()) && price1.getEnd().isAfter(price2.getBegin());
    }
}
