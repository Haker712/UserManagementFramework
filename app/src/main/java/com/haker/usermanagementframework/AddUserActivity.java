package com.haker.usermanagementframework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.haker.usermanagementframework.model.User;
import com.haker.usermanagementframework.realm.RealmController;
import com.haker.usermanagementframework.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.functions.Func6;
import rx.subscriptions.CompositeSubscription;
import rx.Observable;
import rx.Observer;

/**
 * Created by haker on 3/29/17.
 */

public class AddUserActivity extends AppCompatActivity {

    @BindViews({R.id.editTextUserName, R.id.editTextEmail, R.id.editTextPhone, R.id.editTextAddress, R.id.editTextDob, R.id.editTextNrc})
    List<EditText> editTextList;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindViews({R.id.radioButtonMale, R.id.radioButtonFemale})
    List<RadioButton> radioButtonList;

    String gender = "male";

    private CompositeSubscription compositeSubscription;

    private Realm realm;

    Intent intent;
    User user;
    int position = 0;
    String message = "Saving";

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat fmtForSubmittedDateStr = new SimpleDateFormat("yyyy-MM-dd");
    String strDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        buttonSave.setEnabled(false);

        intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("edit").equals("yes")) {
                getSupportActionBar().setTitle("Edit User");
                buttonSave.setText("Update");

                try {
                    JSONArray jsonArray = new JSONArray(intent.getStringExtra("user_obj_str"));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    user = new User();
                    user.setId(jsonObject.getLong("id"));
                    user.setName(jsonObject.getString("name"));
                    user.setEmail(jsonObject.getString("email"));
                    user.setPhone(jsonObject.getString("phone"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setNrc(jsonObject.getString("nrc"));
                    user.setDob(jsonObject.getString("dob"));
                    user.setGender(jsonObject.getString("gender"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setDataForUser(user);
            }
        }

        formValidation();

        //get realm instance
        this.realm = RealmController.with(this).getRealm();
    }

    private void setDataForUser(User user) {
        editTextList.get(0).setText(user.getName());
        editTextList.get(1).setText(user.getEmail());
        editTextList.get(2).setText(user.getPhone());
        editTextList.get(3).setText(user.getAddress());
    }

    private void formValidation() {
        /*compositeSubscription.add((Subscription) Observable.combineLatest(getEditTextObservable(editTextList.get(0)), getEditTextObservable(editTextList.get(1)), getEditTextObservable(editTextList.get(2)),
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
        }));*/

        compositeSubscription.add((Subscription) Observable.combineLatest(getEditTextObservable(editTextList.get(0)), getEditTextObservable(editTextList.get(1)), getEditTextObservable(editTextList.get(2)),
                getEditTextObservable(editTextList.get(3)), getEditTextObservable(editTextList.get(4)), getEditTextObservable(editTextList.get(5)),
                new Func6<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4, Boolean aBoolean5, Boolean aBoolean6) {
                        return aBoolean && aBoolean2 && aBoolean3 && aBoolean4 && aBoolean5 && aBoolean6;
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
                buttonSave.setEnabled((Boolean) o);
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

    @OnClick(R.id.editTextDob)
    void chooseDob() {
        DatePickerDialog dateDialog = new DatePickerDialog(AddUserActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            strDate = fmtForSubmittedDateStr.format(myCalendar.getTime());

            editTextList.get(4).setText(strDate);

        }
    };

    @OnClick(R.id.radioButtonMale)
    void clickMale() {
        if (radioButtonList.get(0).isChecked()) {
            radioButtonList.get(0).setChecked(true);
            radioButtonList.get(1).setChecked(false);
            gender = "male";
        }
    }

    @OnClick(R.id.radioButtonFemale)
    void clickFemale() {
        if (radioButtonList.get(1).isChecked()) {
            radioButtonList.get(1).setChecked(true);
            radioButtonList.get(0).setChecked(false);
            gender = "female";
        }
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
            user.setNrc(editTextList.get(4).getText().toString());
            user.setDob(editTextList.get(4).getText().toString());
            user.setGender(gender);

            realm.beginTransaction();

            if (intent != null) {
                if (intent.getStringExtra("edit").equals("yes")) {
                    position = intent.getIntExtra("position", 0);

                    RealmResults<User> results = realm.where(User.class).findAll();
                    results.get(position).setName(user.getName());
                    results.get(position).setEmail(user.getEmail());
                    results.get(position).setPhone(user.getPhone());
                    results.get(position).setAddress(user.getAddress());
                    results.get(position).setNrc(user.getNrc());
                    results.get(position).setDob(user.getDob());
                    results.get(position).setGender(user.getGender());

                    message = "Updating";
                }else {
                    realm.copyToRealm(user);
                    message = "Saving";
                }
            }

            realm.commitTransaction();
            Toast.makeText(this, message + " Successful.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, ViewUserActivity.class));
            finish();
        }
        else {
            editTextList.get(1).requestFocus();
            editTextList.get(1).setError("Please enter valid email.");
        }
    }

}
