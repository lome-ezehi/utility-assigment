package com.utilityinternational.entity.enums;

/**
 * The category of utility that a {@code Resource}, {@code CustomerUsage}
 * line item or {@code MonthlyBill} subtype relates to.
 *
 * <p>This enum lives in its own dedicated <strong>enum package</strong>
 * ({@code com.utilityinternational.entity.enums}) as required, keeping all
 * domain enumerations together and decoupled from the entity classes that
 * use them. Utility International offers a bundled service of electricity,
 * natural gas and water, which maps directly onto these three constants.</p>
 *
 * <p>Each constant carries a human-readable {@code label} (a {@code String})
 * and the unit in which consumption is measured. When persisted, the enum is
 * stored using its name as a {@code String} via
 * {@code @Enumerated(EnumType.STRING)} on the owning entities, so that the
 * database column holds readable values such as {@code "ELECTRICITY"} rather
 * than a brittle ordinal index.</p>
 */
public enum UtilityType {

    ELECTRICITY("Electricity", "kWh"),
    GAS("Natural Gas", "therm"),
    WATER("Water", "cubic metre");

    private final String label;
    private final String unitOfMeasure;

    UtilityType(String label, String unitOfMeasure) {
        this.label = label;
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getLabel() {
        return label;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }
}
