
package com.tasi.backend.search;

import com.google.gson.annotations.SerializedName;

/**
 * State Enum.
 */
public enum EnumState {
    /** Active state (when the search is still running). */
    @SerializedName("active")
    ACTIVE,
    /** Done state (when the search is finished). */
    @SerializedName("done")
    DONE;
}
