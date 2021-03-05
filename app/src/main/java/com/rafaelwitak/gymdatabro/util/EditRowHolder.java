/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;

// TODO: 05.03.2021 Reuse! And refactor database usage.
public class EditRowHolder {
    protected final GymBroDatabase database;

    public EditRowHolder(Context context) {
        this.database = GymBroDatabase.getDatabase(context);
    }
}
