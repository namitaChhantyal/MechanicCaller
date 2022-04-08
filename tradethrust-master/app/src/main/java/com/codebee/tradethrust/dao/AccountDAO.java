package com.codebee.tradethrust.dao;

import com.codebee.tradethrust.model.User;

/**
 * Created by csangharsha on 5/18/18.
 */

public interface AccountDAO {

    User getUser();
    boolean savePhoneNumber(String phoneNumber);

}
