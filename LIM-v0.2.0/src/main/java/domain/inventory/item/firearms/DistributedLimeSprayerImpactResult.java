package domain.inventory.item.firearms;

/** Una única exposición de Cal Viva distribuida anatómicamente: 9 % HEAD y 91 % BODY. */
public record DistributedLimeSprayerImpactResult(
        LimeSprayerImpactResult head,
        LimeSprayerImpactResult body
) {}
