package com.hcl.kandy.cpass;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.hcl.kandy.cpass.activities.HomeActivity;
import com.rbbn.cpaas.mobile.CPaaS;
import com.rbbn.cpaas.mobile.authentication.api.Authentication;
import com.rbbn.cpaas.mobile.authentication.api.ConnectionCallback;
import com.rbbn.cpaas.mobile.utilities.Configuration;
import com.rbbn.cpaas.mobile.utilities.Globals;
import com.rbbn.cpaas.mobile.utilities.exception.MobileError;
import com.rbbn.cpaas.mobile.utilities.exception.MobileException;
import com.rbbn.cpaas.mobile.utilities.logging.LogLevel;
import com.rbbn.cpaas.mobile.utilities.services.ServiceInfo;
import com.rbbn.cpaas.mobile.utilities.services.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private CPaaS mCpaas;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setCpass(String baseUrl, String mAccessToken, String idToken, HomeActivity.CpassListner cpassListner) {
        Context context = getApplicationContext();

        Configuration.getInstance().setRestServerUrl(baseUrl);
        Configuration.getInstance().setLogLevel(LogLevel.TRACE);
        ConfigurationHelper.setConfigurations(baseUrl);
        Globals.setApplicationContext(context);

        mCpaas = initKandyService(mAccessToken, idToken, cpassListner);

    }

    public static CPaaS initKandyService(String accessToken, String idToken, HomeActivity.CpassListner cpassListner) {
        Log.d("CpassSubscribe", "initKAndyService()");
        int lifetime = 3600; //in seconds

        List<ServiceInfo> services = new ArrayList<>();
        services.add(new ServiceInfo(ServiceType.SMS, true));
        services.add(new ServiceInfo(ServiceType.CHAT, true));

        CPaaS mCpaas = new CPaaS(services);
        Authentication authentication = mCpaas.getAuthentication();
        authentication.setToken(accessToken);

        try {
            authentication.connect(idToken, lifetime, new ConnectionCallback() {
                @Override
                public void onSuccess(String channelInfo) {
                    Log.d("CpassSubscribe", channelInfo);
                    cpassListner.onCpassSuccess();

                }

                @Override
                public void onFail(MobileError mobileError) {
                    if (mobileError != null)
                        Log.d("CpassSubscribe", mobileError.getErrorMessage());
                    else
                        Log.d("CpassSubscribe", "error");
                    cpassListner.onCpassFail();
                }
            });
        } catch (MobileException e) {
            e.printStackTrace();
        }

        return mCpaas;
    }

    public CPaaS getCpass() {
        return mCpaas;
    }
}
