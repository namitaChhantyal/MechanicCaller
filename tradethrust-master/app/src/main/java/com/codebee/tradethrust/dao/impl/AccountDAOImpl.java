package com.codebee.tradethrust.dao.impl;

import com.codebee.tradethrust.dao.AccountDAO;
import com.codebee.tradethrust.model.User;

import java.util.Date;

/**
 * Created by csangharsha on 5/18/18.
 */

public class AccountDAOImpl implements AccountDAO {

    private static User user;

    public AccountDAOImpl(){
        setUser();
    }

    private void setUser() {
        user = new User();
        user.setFirstName("Surya");
        user.setSecondName("Shrestha");
        user.setEmail("suryas@tradethrust.com");
        user.setPhoneNumber("9841123456");
        user.setPassword("password");
        user.setCompanyName("Morang Auto Works");
        user.setCompanyAddress("Kathmandu, Nepal");
        user.setCompanyPhoneNumber("01-4563467");
        user.setCurrentRole("Field Staff");
        user.setCurrentRoleSince(new Date());
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean savePhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        return true;
    }
}
