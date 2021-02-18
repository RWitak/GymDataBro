/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class EditRowHolder {
    protected final GymBroDatabase database;

    public EditRowHolder(Context context) {
        this.database = GymBroDatabase.getDatabase(context);
    }
}
