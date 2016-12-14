package net.jacobmason.velocityvortexscorekeeper;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jacob on 12/14/16.
 */

public class ApplicationController extends Application {
    private static ApplicationController instance;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("ApplicationController", "Created");
        instance = this;
    }

    public static ApplicationController get_instance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
}
