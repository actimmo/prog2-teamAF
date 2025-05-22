package com.teamAF.app.Interfaces;

import com.teamAF.app.Model.enums.MyEnum;

public interface Observer {
    void update(String apiID, MyEnum.RepoResponseCode responseCode);
}
