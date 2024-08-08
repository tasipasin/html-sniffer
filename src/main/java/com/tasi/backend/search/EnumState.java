
package com.tasi.backend.search;

/**
 * State Enum.
 */
public enum EnumState {

    /** Active state (when the search is still running). */
    ACTIVE("active"),
    /** Done state (when the search is finished). */
    DONE("done");

    /** Name used in enum. */
    private final String mName;

    /**
     * State Enum.
     */
    EnumState(String name) {
        this.mName = name;
    }

    /**
     * Returns the name of the state.
     * @return the name of the state.
     */
    public String getName() {
        return this.mName;
    }
}
