package com.haker.usermanagementframework.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haker.usermanagementframework.AddUserActivity;
import com.haker.usermanagementframework.R;
import com.haker.usermanagementframework.model.User;
import com.haker.usermanagementframework.realm.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by haker on 3/30/17.
 */

public class UsersAdapter extends RealmRecyclerViewAdapter<User> {

    final Context context;
    public static Realm realm;
    RealmResults<User> results;

    public static Activity activity;

    public UsersAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_user_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        realm = RealmController.getInstance().getRealm();

        final User user = getItem(position);
        Log.i("user name", user.getName());
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textViewUserName.setText(user.getName());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewPhone.setText(user.getPhone());
        viewHolder.textViewAddress.setText(user.getAddress());
        viewHolder.textViewGender.setText(user.getGender());
        viewHolder.textViewNrc.setText(user.getNrc());
        viewHolder.textViewDob.setText(user.getDob());

        viewHolder.linearLayoutUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setMessage("What do you want to do?")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editUser(position);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(position);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayoutUserData;
        public TextView textViewUserName;
        public TextView textViewEmail;
        public TextView textViewPhone;
        public TextView textViewAddress;
        public TextView textViewGender;
        public TextView textViewNrc;
        public TextView textViewDob;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutUserData = (LinearLayout) itemView.findViewById(R.id.linearLayoutUserData);
            textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            textViewEmail= (TextView) itemView.findViewById(R.id.textViewEmail);
            textViewPhone = (TextView) itemView.findViewById(R.id.textViewPhone);
            textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
            textViewGender = (TextView) itemView.findViewById(R.id.textViewGender);
            textViewNrc = (TextView) itemView.findViewById(R.id.textViewNrc);
            textViewDob = (TextView) itemView.findViewById(R.id.textViewDob);
        }
    }

    public void editUser(int position) {
        results = realm.where(User.class).findAll();
        User user = results.get(position);
        Log.i("user-name", user.getName());
        Intent intent = new Intent(activity, AddUserActivity.class);
        intent.putExtra("edit", "yes");

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("address", user.getAddress());
            jsonObject.put("nrc", user.getNrc());
            jsonObject.put("dob", user.getDob());
            jsonObject.put("gender", user.getGender());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);

        intent.putExtra("user_obj_str", jsonArray.toString());
        intent.putExtra("position", position);

        activity.startActivity(intent);
        activity.finish();
    }

    public void deleteUser(int position) {
        results = realm.where(User.class).findAll();

        User user = results.get(position);
        long id = user.getId();
        String name = user.getName();

        realm.beginTransaction();
        results.remove(position);
        realm.commitTransaction();

        notifyDataSetChanged();
        Toast.makeText(context, name + " is deleted.", Toast.LENGTH_SHORT).show();
    }
}
