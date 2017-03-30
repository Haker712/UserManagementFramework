package com.haker.usermanagementframework.adapters;

import android.content.Context;

import com.haker.usermanagementframework.model.User;
import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<User> {

    public RealmBooksAdapter(Context context, RealmResults<User> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}