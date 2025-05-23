package com.teamAF.app.Controller;

import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private static HomeController instance;

    @Override
    public Object call(Class<?> aClass) {
        if (instance != null) {
            return instance;
        }

        try{
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
