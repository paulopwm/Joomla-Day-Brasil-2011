package com.JoomlaDay;

import greendroid.app.GDApplication;

public class Launcher extends GDApplication {

    @Override
    public Class<?> getHomeActivityClass() {
        return Main.class;
    }
}