package com.shashank.spendistry.ViewModels;

import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.Repositories.AuthRepository;

import okhttp3.MultipartBody;


public class AuthViewModel extends AndroidViewModel {

    private AuthRepository AuthRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        AuthRepository = new AuthRepository(application);
    }

    public MutableLiveData<String> onLogIn(LinearLayout linearLayout,String email, String password){
        return AuthRepository.onLogIn(linearLayout,email, password);
    }

    public MutableLiveData<Users> fetchUser(String email){
        return AuthRepository.fetchUsers(email);
    }


    public void sendOTP(LinearLayout linearLayout,String email) {
        AuthRepository.sendOTP(linearLayout,email);
    }

    public MutableLiveData<String> verifyOTP(LinearLayout linearLayout,String email, int otp) {
        return AuthRepository.verifyOTP(linearLayout,email,otp);
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

    public void updateUser(ScrollView scrollView, UserDetails userDetails){
        AuthRepository.updateUser(scrollView,userDetails);
    }

    public void deleteAccount(String email){
        AuthRepository.deleteAccount(email);
    }

    public void deleteProfilePic(ScrollView scrollView, String email){
        AuthRepository.deleteProfilePic(scrollView,email);
    }

    public void setNewProfilePic(ScrollView scrollView, String email, MultipartBody.Part part){
        AuthRepository.setNewProfilePic(scrollView, email, part);
    }

    public void setNewPassword(Context context, LinearLayout linearLayout, String email, String newPassword){
        AuthRepository.setNewPassword(context,linearLayout,email,newPassword);
    }

}
