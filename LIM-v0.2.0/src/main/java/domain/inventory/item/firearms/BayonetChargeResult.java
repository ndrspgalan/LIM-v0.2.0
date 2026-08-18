package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;

public record BayonetChargeResult(boolean active, boolean impact, double staminaSpent,
                                  LethalityProfile impactProfile, String reason) {}
