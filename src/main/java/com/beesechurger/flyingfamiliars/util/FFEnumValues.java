package com.beesechurger.flyingfamiliars.util;

public class FFEnumValues
{
    // Familiar movement status:
    public static enum FamiliarStatus
    {
        SITTING,
        FOLLOWING,
        ATTACKING,
        WANDERING,
        LANDING;
    }

    // Familiar body rotation control move types:
    public static enum FamiliarMoveTypes
    {
        HOVER,
        FORWARD,
        NONE;
    }
}
