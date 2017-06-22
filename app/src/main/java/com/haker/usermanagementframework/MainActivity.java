package com.haker.usermanagementframework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindViews({R.id.buttonAddUser, R.id.buttonViewUser})
    List<Button> buttonList;

    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };
    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ButterKnife.apply(buttonList, DISABLE);//for disable all buttons
        ButterKnife.apply(buttonList, ENABLED, true);//for enable all buttons with boolean value
    }

    @OnClick(R.id.buttonAddUser)
    void addUser() {
        startActivity(new Intent(this, AddUserActivity.class).putExtra("edit", "no"));
    }

    @OnClick(R.id.buttonViewUser)
    void viewUser() {
        startActivity(new Intent(this, ViewUserActivity.class));
    }
}
