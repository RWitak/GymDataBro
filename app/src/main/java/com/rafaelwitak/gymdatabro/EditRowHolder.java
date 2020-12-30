/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

public class EditRowHolder {
    protected final GymBroDatabase database;

    public EditRowHolder(Context context) {
        this.database = GymBroDatabase.getDatabase(context);
    }
}
