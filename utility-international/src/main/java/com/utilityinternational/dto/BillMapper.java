package com.utilityinternational.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.utilityinternational.entity.ElectricityBill;
import com.utilityinternational.entity.GasBill;
import com.utilityinternational.entity.MonthlyBill;
import com.utilityinternational.entity.WaterBill;
import com.utilityinternational.entity.enums.UtilityType;

/**
 * Maps {@link MonthlyBill} entities to {@link BillDTO}s.
 *
 * <p>This class demonstrates two of the required techniques:</p>
 * <ul>
 *   <li><strong>Functional programming</strong> — the conversion of a single
 *       bill is held as a {@link java.util.function.Function} ({@code TO_DTO}),
 *       a reusable, side-effect-free function object.</li>
 *   <li>The <strong>{@code stream().map(...).collect(Collectors.toList())}</strong>
 *       pipeline — {@link #toDtoList(List)} streams a list of entities, maps
 *       each through the function and collects them into a list of DTOs.</li>
 * </ul>
 */
public final class BillMapper {

    /**
     * A pure {@link Function} from entity to DTO, expressed as a lambda
     * (functional programming). Reused by the stream pipeline below and
     * usable anywhere a {@code Function<MonthlyBill, BillDTO>} is expected.
     */
    public static final Function<MonthlyBill, BillDTO> TO_DTO = bill -> new BillDTO(
            bill.getId(),
            bill.getCustomer().getFullName(),
            resolveUtilityType(bill).getLabel(),
            bill.getStatus().name(),          // enum -> String
            bill.getGeneratedOn(),
            bill.disputeDeadline(),
            bill.getAmountDue());

    private BillMapper() {
        // Utility class — no instances.
    }

    /** Convert a single entity to a DTO by applying the functional mapper. */
    public static BillDTO toDto(MonthlyBill bill) {
        return TO_DTO.apply(bill);
    }

    /**
     * Convert a list of entities to a list of DTOs using the required
     * <strong>{@code .stream().map(...).collect(Collectors.toList())}</strong>
     * pipeline with a method reference (functional programming).
     */
    public static List<BillDTO> toDtoList(List<MonthlyBill> bills) {
        return bills.stream()
                .map(BillMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Derive the utility category from the concrete bill subtype. */
    private static UtilityType resolveUtilityType(MonthlyBill bill) {
        if (bill instanceof ElectricityBill) {
            return UtilityType.ELECTRICITY;
        } else if (bill instanceof GasBill) {
            return UtilityType.GAS;
        } else if (bill instanceof WaterBill) {
            return UtilityType.WATER;
        }
        throw new IllegalArgumentException("Unknown bill subtype: " + bill.getClass().getName());
    }
}
