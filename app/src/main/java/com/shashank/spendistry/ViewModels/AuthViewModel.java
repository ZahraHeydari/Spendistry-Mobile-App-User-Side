package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.Repositories.AuthRepository;


public class AuthViewModel extends AndroidViewModel {

    private AuthRepository AuthRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        AuthRepository = new AuthRepository(application);
    }

    public MutableLiveData<String> onLogIn(String email, String password){
        return AuthRepository.onLogIn(email, password);
    }

    public MutableLiveData<Users> fetchUser(String email){
        return AuthRepository.fetchUsers(email);
    }

    public MutableLiveData<Users> addUser(Users users){
        return AuthRepository.createUser(users);
    }

    public void updateAccount(String email,Auth auth){
        AuthRepository.updateAccount(email,auth);
    }

    public MutableLiveData<Auth> createAccount( Auth auth){
        return AuthRepository.createAccount( auth);
    }

    public void deleteAccount(String email){
        AuthRepository.deleteAccount(email);
    }

}
