package com.teamAF.app.Interfaces;

import com.teamAF.app.Model.enums.MyEnum;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(String apiID, MyEnum.RepoResponseCode responseCode);
}
