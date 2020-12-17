package me.poma123.globalwarming.api;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This enum holds the two states of the
 * configurable world filter.
 *
 * @author poma123
 */
public enum WorldFilterType {

    WHITELIST("whitelist"),
    BLACKLIST("blacklist");

    private final String name;

    @ParametersAreNonnullByDefault
    WorldFilterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
