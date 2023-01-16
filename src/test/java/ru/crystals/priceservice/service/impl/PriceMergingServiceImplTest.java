package ru.crystals.priceservice.service.impl;

import org.junit.jupiter.api.Test;
import ru.crystals.priceservice.dto.PriceDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PriceMergingServiceImplTest {
    private final PriceMergingServiceImpl priceMergingService = new PriceMergingServiceImpl();

    @Test
    void current_prices_is_null() {
        List<PriceDto> newPrices = Collections.singletonList(
                new PriceDto(
                        null,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        11000L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(null, newPrices);
        assertEquals(newPrices, mergedPrices);
    }

    @Test
    void current_prices_is_empty() {
        List<PriceDto> newPrices = Collections.singletonList(
                new PriceDto(
                        null,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        11000L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(Collections.emptyList(), newPrices);
        assertEquals(newPrices, mergedPrices);
    }

    @Test
    void new_prices_is_null() {
        List<PriceDto> currentPrices = Collections.singletonList(
                new PriceDto(
                        1L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        11000L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(currentPrices, null);
        assertEquals(currentPrices, mergedPrices);
    }

    @Test
    void new_prices_is_empty() {
        List<PriceDto> currentPrices = Collections.singletonList(
                new PriceDto(
                        1L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        11000L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(currentPrices, Collections.emptyList());
        assertEquals(currentPrices, mergedPrices);
    }

    @Test
    void current_prices_is_null_and_new_prices_is_null() {
        List<PriceDto> mergePrices = priceMergingService.mergePrices(null, null);

        assertNotNull(mergePrices);
        assertEquals(0, mergePrices.size());
    }

    @Test
    void current_prices_is_empty_and_new_prices_is_empty() {
        List<PriceDto> mergePrices = priceMergingService.mergePrices(Collections.emptyList(), Collections.emptyList());

        assertNotNull(mergePrices);
        assertEquals(0, mergePrices.size());
    }

    @Test
    void current_prices_is_null_and_new_prices_is_empty() {
        List<PriceDto> mergePrices = priceMergingService.mergePrices(null, Collections.emptyList());

        assertNotNull(mergePrices);
        assertEquals(0, mergePrices.size());
    }

    @Test
    void current_prices_is_empty_and_new_prices_is_null() {
        List<PriceDto> mergePrices = priceMergingService.mergePrices(Collections.emptyList(), null);

        assertNotNull(mergePrices);
        assertEquals(0, mergePrices.size());
    }

    @Test
    void merging_different_prices_with_different_expiration_dates() {
        List<PriceDto> currentPrices = Arrays.asList(
                new PriceDto(
                        1L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        11000L
                ),
                new PriceDto(
                        2L,
                        "122856",
                        2,
                        1,
                        LocalDateTime.of(2013, 1, 10, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 20, 23, 59, 59),
                        99000L
                ),
                new PriceDto(
                        3L,
                        "6654",
                        1,
                        2,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 0, 0, 0),
                        5000L
                )
        );

        List<PriceDto> newPrices = Arrays.asList(
                new PriceDto(
                        null,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 20, 0, 0, 0),
                        LocalDateTime.of(2013, 2, 20, 23, 59, 59),
                        11000L
                ),
                new PriceDto(
                        null,
                        "122856",
                        2,
                        1,
                        LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 25, 23, 59, 59),
                        92000L
                ),
                new PriceDto(
                        null,
                        "6654",
                        1,
                        2,
                        LocalDateTime.of(2013, 1, 12, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 13, 23, 59, 59),
                        4000L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(currentPrices, newPrices);

        assertNotNull(mergedPrices);
        assertEquals(6, mergedPrices.size());

        assertEquals(1L, mergedPrices.get(0).getId());
        assertEquals("122856", mergedPrices.get(0).getProductCode());
        assertEquals(1, mergedPrices.get(0).getNumber());
        assertEquals(1, mergedPrices.get(0).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                mergedPrices.get(0).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 2, 20, 23, 59, 59),
                mergedPrices.get(0).getEnd()
        );
        assertEquals(11000L, mergedPrices.get(0).getValue());

        assertEquals(2L, mergedPrices.get(1).getId());
        assertEquals("122856", mergedPrices.get(1).getProductCode());
        assertEquals(2, mergedPrices.get(1).getNumber());
        assertEquals(1, mergedPrices.get(1).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 10, 0, 0, 0),
                mergedPrices.get(1).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                mergedPrices.get(1).getEnd()
        );
        assertEquals(99000L, mergedPrices.get(1).getValue());

        assertNull(mergedPrices.get(2).getId());
        assertEquals("122856", mergedPrices.get(2).getProductCode());
        assertEquals(2, mergedPrices.get(2).getNumber());
        assertEquals(1, mergedPrices.get(2).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                mergedPrices.get(2).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 25, 23, 59, 59),
                mergedPrices.get(2).getEnd()
        );
        assertEquals(92000L, mergedPrices.get(2).getValue());

        assertEquals(3L, mergedPrices.get(3).getId());
        assertEquals("6654", mergedPrices.get(3).getProductCode());
        assertEquals(1, mergedPrices.get(3).getNumber());
        assertEquals(2, mergedPrices.get(3).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                mergedPrices.get(3).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 12, 0, 0, 0),
                mergedPrices.get(3).getEnd()
        );
        assertEquals(5000L, mergedPrices.get(3).getValue());

        assertNull(mergedPrices.get(4).getId());
        assertEquals("6654", mergedPrices.get(4).getProductCode());
        assertEquals(1, mergedPrices.get(4).getNumber());
        assertEquals(2, mergedPrices.get(4).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 12, 0, 0, 0),
                mergedPrices.get(4).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 13, 23, 59, 59),
                mergedPrices.get(4).getEnd()
        );
        assertEquals(4000L, mergedPrices.get(4).getValue());

        assertNull(mergedPrices.get(5).getId());
        assertEquals("6654", mergedPrices.get(5).getProductCode());
        assertEquals(1, mergedPrices.get(5).getNumber());
        assertEquals(2, mergedPrices.get(5).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 13, 23, 59, 59),
                mergedPrices.get(5).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 31, 0, 0, 0),
                mergedPrices.get(5).getEnd()
        );
        assertEquals(5000L, mergedPrices.get(5).getValue());
    }

    @Test
    void merging_the_same_prices_with_different_expiration_dates() {
        List<PriceDto> currentPrices = Arrays.asList(
                new PriceDto(
                        1L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 10, 0, 0, 0),
                        8000L
                ),
                new PriceDto(
                        2L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 10, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 20, 0, 0, 0),
                        8700L
                ),
                new PriceDto(
                        3L,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 20, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                        9000L
                )
        );

        List<PriceDto> newPrices = Arrays.asList(
                new PriceDto(
                        null,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 5, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                        8000L
                ),
                new PriceDto(
                        null,
                        "122856",
                        1,
                        1,
                        LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                        LocalDateTime.of(2013, 1, 25, 0, 0, 0),
                        8500L
                )
        );

        List<PriceDto> mergedPrices = priceMergingService.mergePrices(currentPrices, newPrices);

        assertNotNull(mergedPrices);
        assertEquals(3, mergedPrices.size());

        assertEquals(1L, mergedPrices.get(0).getId());
        assertEquals("122856", mergedPrices.get(0).getProductCode());
        assertEquals(1, mergedPrices.get(0).getNumber());
        assertEquals(1, mergedPrices.get(0).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                mergedPrices.get(0).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                mergedPrices.get(0).getEnd()
        );
        assertEquals(8000L, mergedPrices.get(0).getValue());

        assertNull(mergedPrices.get(1).getId());
        assertEquals("122856", mergedPrices.get(1).getProductCode());
        assertEquals(1, mergedPrices.get(1).getNumber());
        assertEquals(1, mergedPrices.get(1).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 15, 0, 0, 0),
                mergedPrices.get(1).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 25, 0, 0, 0),
                mergedPrices.get(1).getEnd()
        );
        assertEquals(8500L, mergedPrices.get(1).getValue());

        assertEquals(3L, mergedPrices.get(2).getId());
        assertEquals("122856", mergedPrices.get(2).getProductCode());
        assertEquals(1, mergedPrices.get(1).getNumber());
        assertEquals(1, mergedPrices.get(1).getDepart());
        assertEquals(
                LocalDateTime.of(2013, 1, 25, 0, 0, 0),
                mergedPrices.get(2).getBegin()
        );
        assertEquals(
                LocalDateTime.of(2013, 1, 31, 23, 59, 59),
                mergedPrices.get(2).getEnd()
        );
        assertEquals(9000L, mergedPrices.get(2).getValue());
    }
}