package com.hcl.kandy.cpass.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.hcl.kandy.cpass.App;
import com.hcl.kandy.cpass.R;
import com.hcl.kandy.cpass.fragments.BaseFragment;
import com.hcl.kandy.cpass.remote.RestApiClient;
import com.hcl.kandy.cpass.remote.RestApiInterface;
import com.hcl.kandy.cpass.remote.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    public static String access_token = "access_token";
    public static String id_token = "id_token";
    public static String base_url = "base_url";
    public static String login_type = "login_type";
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    int PERMISSION_ALL = 1;
    LinearLayout llPasswordGrant, llClientCredentials;
    boolean isPasswordGrantLoginType;
    private RestApiInterface mRestApiInterface;
    private TextView mEtUserName;
    private TextView mEtUserPassword;
    private TextView mEtClient;
    private EditText mBaseUrl;
    private EditText mClientId, mClientSecret;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_main, container, false);

        if (!checkPermission()) {
            getPerMission();
        }
        inflate.findViewById(R.id.button_login).setOnClickListener(this);
        mEtUserName = inflate.findViewById(R.id.et_user_name);
        mEtUserPassword = inflate.findViewById(R.id.et_user_password);
        mEtClient = inflate.findViewById(R.id.et_user_client);
        mBaseUrl = inflate.findViewById(R.id.et_url);
        llPasswordGrant = inflate.findViewById(R.id.ll_password_grant);
        llClientCredentials = inflate.findViewById(R.id.ll_client_credentials);
        mClientId = inflate.findViewById(R.id.et_client_id);
        mClientSecret = inflate.findViewById(R.id.et_client_secret);
        isPasswordGrantLoginType = true;
        ((RadioGroup) inflate.findViewById(R.id.rg_login_type_selection))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rb_password_grant) {
                            isPasswordGrantLoginType = true;
                            llPasswordGrant.setVisibility(View.VISIBLE);
                            llClientCredentials.setVisibility(View.GONE);
                        } else {
                            isPasswordGrantLoginType = false;
                            llClientCredentials.setVisibility(View.VISIBLE);
                            llPasswordGrant.setVisibility(View.GONE);
                        }
                    }
                });

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean validate() {
        if (isPasswordGrantLoginType) {
            if (TextUtils.isEmpty(mBaseUrl.getText().toString()))
                return false;
            else if (TextUtils.isEmpty(mEtUserName.getText().toString()))
                return false;
            else if (TextUtils.isEmpty(mEtUserPassword.getText().toString()))
                return false;
            else if (TextUtils.isEmpty(mEtClient.getText().toString()))
                return false;
            else
                return true;
        } else {
            if (TextUtils.isEmpty(mBaseUrl.getText().toString()))
                return false;
            else if (TextUtils.isEmpty(mClientId.getText().toString()))
                return false;
            else if (TextUtils.isEmpty(mClientSecret.getText().toString()))
                return false;
            else
                return true;
        }
    }

    private void OnLoginClick() {

        if (!checkPermission()) {
            getPerMission();
            return;
        }


        if (!validate()) {
            showMessage("All fields are mandatory");
            return;

        }

        Retrofit client = RestApiClient.getClient("https://" + mBaseUrl.getText().toString());
        if (client == null) {
            showMessage("Please enter correct Fields");
            return;
        }
        mRestApiInterface = client.create(RestApiInterface.class);
        if (mRestApiInterface == null) {
            showMessage("Please enter correct Fields");
            return;
        }
        showProgressBar("Login..");

        Call<LoginResponse> responseCall;
        if (isPasswordGrantLoginType) {
            responseCall = mRestApiInterface.loginAPI(
                    mEtUserName.getText().toString(),
                    mEtUserPassword.getText().toString(),
                    mEtClient.getText().toString(),
                    "password",
                    "openid");
        } else {
            responseCall = mRestApiInterface.loginAPIProject(
                    mClientId.getText().toString(),
                    mClientSecret.getText().toString(),
                    "client_credentials",
                    "openid");
        }

        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call,
                                   @NonNull Response<LoginResponse> response) {
                LoginResponse body = response.body();

                if (body != null) {
                    showMessage("Login Success");
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(access_token, body.getAccessToken());
                    intent.putExtra(id_token, body.getIdToken());
                    intent.putExtra(base_url, mBaseUrl.getText().toString());
                    intent.putExtra(login_type, isPasswordGrantLoginType);

                    if (! getActivity().isFinishing()) {

                        App app = (App) getActivity().getApplicationContext();
                        app.setCpass( mBaseUrl.getText().toString(), body.getAccessToken(), body.getIdToken(), new CpassListner() {
                            @Override
                            public void onCpassSuccess() {
                                hideProgressBAr();
                                startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void onCpassFail() {
                                hideProgressBAr();
                                showMessage("Try again..");

                            }
                        });
                    } else {
                        hideProgressBAr();
                        Log.d("HCL", "login failed");
                        showMessage("Try again..");
                    }

                } else {
                    hideProgressBAr();
                    Log.d("HCL", "login failed");
                    showMessage("Login Failed");

                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                call.cancel();
                if (!getActivity().isFinishing()) {
                    hideProgressBAr();
                    Log.d("HCL", "login failed");
                    showMessage("Try again..");

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                OnLoginClick();
                break;
        }
    }

    private boolean checkPermission() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void getPerMission() {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
                String permissionsDenied = "";
                for (String per : PERMISSIONS) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        permissionsDenied += "\n" + per;
                    }
                }
            }
        }
    }


    private void showMessage(String message) {
        Snackbar.make(mEtUserName, message, Snackbar.LENGTH_SHORT).show();
    }

    public interface CpassListner {
        void onCpassSuccess();
        void onCpassFail();
    }

}
