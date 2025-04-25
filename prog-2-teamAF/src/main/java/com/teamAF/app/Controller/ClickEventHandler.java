package com.teamAF.app.Controller;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);

}
