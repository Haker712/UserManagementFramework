package com.haker.usermanagementframework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haker.usermanagementframework.R;
import com.haker.usermanagementframework.model.User;
import com.haker.usermanagementframework.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by haker on 3/30/17.
 */

public class UsersAdapter extends RealmRecyclerViewAdapter<User> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public UsersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_user_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        realm = RealmController.getInstance().getRealm();

        final User user = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textViewUserName.setText(user.getName());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewPhone.setText(user.getPhone());
        viewHolder.textViewAddress.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUserName;
        public TextView textViewEmail;
        public TextView textViewPhone;
        public TextView textViewAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            textViewEmail= (TextView) itemView.findViewById(R.id.textViewEmail);
            textViewPhone = (TextView) itemView.findViewById(R.id.textViewPhone);
            textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
        }
    }
}
