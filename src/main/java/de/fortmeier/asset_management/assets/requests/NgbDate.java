package de.fortmeier.asset_management.assets.requests;

/**
 * Record for getting the right format of the parameters borrowedAt and returnAt from the frontend.
 * @param year
 * @param month
 * @param day
 */
public record NgbDate(
        int year,
        int month,
        int day) {
}
