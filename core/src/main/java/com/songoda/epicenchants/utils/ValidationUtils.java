package com.songoda.epicenchants.utils;

import co.aikar.commands.*;
import org.jetbrains.annotations.NotNull;

public class ValidationUtils {
    @NotNull
    public static Number parseAndValidateNumber(CommandExecutionContext c, Number minValue, Number maxValue) throws InvalidCommandArgument {
        final Number val = ACFUtil.parseNumber(c.popFirstArg(), c.hasFlag("suffixes"));
        validateMinMax(c, val, minValue, maxValue);
        return val;
    }

    private static void validateMinMax(CommandExecutionContext c, Number val) throws InvalidCommandArgument {
        validateMinMax(c, val, null, null);
    }

    private static void validateMinMax(CommandExecutionContext c, Number val, Number minValue, Number maxValue) throws InvalidCommandArgument {
        minValue = c.getFlagValue("min", minValue);
        maxValue = c.getFlagValue("max", maxValue);
        if (maxValue != null && val.doubleValue() > maxValue.doubleValue()) {
            throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_MOST, "{max}", String.valueOf(maxValue));
        }
        if (minValue != null && val.doubleValue() < minValue.doubleValue()) {
            throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_LEAST, "{min}", String.valueOf(minValue));
        }
    }
}
