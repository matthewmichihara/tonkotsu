package com.fourpool.tonkotsu;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

public class TonkotsuApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(new TonkotsuModule(this));
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static TonkotsuApplication get(Context context) {
        return (TonkotsuApplication) context.getApplicationContext();
    }
}
