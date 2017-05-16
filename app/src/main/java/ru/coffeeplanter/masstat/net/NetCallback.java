package ru.coffeeplanter.masstat.net;

import android.os.Parcelable;

/**
 * Interface for processing data, got from server.
 */

public interface NetCallback extends Parcelable {

    void success();

    void failure();

}
