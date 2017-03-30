package com.haker.usermanagementframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haker.usermanagementframework.model.User;
import com.haker.usermanagementframework.realm.RealmController;
import com.haker.usermanagementframework.utils.Utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.subscriptions.CompositeSubscription;
import rx.Observable;
import rx.Observer;

/**
 * Created by haker on 3/29/17.
 */

public class AddUserActivity extends AppCompatActivity {

    @BindViews({R.id.editTextUserName, R.id.editTextEmail, R.id.editTextPhone, R.id.editTextAddress})
    List<EditText> editTextList;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    private CompositeSubscription compositeSubscription;

    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        buttonSave.setEnabled(false);

        formValidation();

        //get realm instance
        this.realm = RealmController.with(this).getRealm();
    }

    private void formValidation() {
        compositeSubscription.add((Subscription) Observable.combineLatest(getEditTextObservable(editTextList.get(0)), getEditTextObservable(editTextList.get(1)), getEditTextObservable(editTextList.get(2)),
                getEditTextObservable(editTextList.get(3)), new Func4<Boolean, Boolean, Boolean, Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4) {
                        return aBoolean && aBoolean2 && aBoolean3 && aBoolean4;
                    }
                })
        .debounce(1000, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

                buttonSave.setEnabled(((Boolean) o));
            }
        }));
    }

    @NonNull
    private rx.Observable<Boolean> getEditTextObservable(final EditText editText) {
        return WidgetObservable.text(editText).map(new Func1<OnTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(OnTextChangeEvent onTextChangeEvent) {
                if (!(onTextChangeEvent.text().toString().trim().length() > 0)) {
                    editText.setError("Required field");
                } else {
                }
                return onTextChangeEvent.text().toString().trim().length() > 0;
            }
        });
    }

    @OnClick(R.id.buttonSave)
    void saveUser() {
        CharSequence str = editTextList.get(1).getText().toString();
        if (Utils.isValidEmail(str)) {
            User user = new User();
            user.setId(RealmController.getInstance().getUsers().size() + System.currentTimeMillis());
            user.setName(editTextList.get(0).getText().toString());
            user.setEmail(editTextList.get(1).getText().toString());
            user.setPhone(editTextList.get(2).getText().toString());
            user.setAddress(editTextList.get(3).getText().toString());

            realm.beginTransaction();
            realm.copyToRealm(user);
            realm.commitTransaction();
            Toast.makeText(this, "Saving Successful.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, ViewUserActivity.class));
            finish();
        }
        else {
            editTextList.get(1).requestFocus();
            editTextList.get(1).setError("Please enter valid email.");
        }
    }

}
