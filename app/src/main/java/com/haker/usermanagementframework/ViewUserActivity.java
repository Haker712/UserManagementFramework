package com.haker.usermanagementframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haker.usermanagementframework.adapters.RealmUsersAdapter;
import com.haker.usermanagementframework.adapters.UsersAdapter;
import com.haker.usermanagementframework.model.User;
import com.haker.usermanagementframework.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haker on 3/30/17.
 */

public class ViewUserActivity extends AppCompatActivity {

    private UsersAdapter usersAdapter;
    private Realm realm;
    private RecyclerView recycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        setupRecycler();

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getUsers());
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        usersAdapter = new UsersAdapter(this);
        recycler.setAdapter(usersAdapter);
    }

    public void setRealmAdapter(RealmResults<User> users) {

        RealmUsersAdapter realmAdapter = new RealmUsersAdapter(this.getApplicationContext(), users, true);
        // Set the data and tell the RecyclerView to draw
        usersAdapter.setRealmAdapter(realmAdapter);
        usersAdapter.notifyDataSetChanged();
    }
}
