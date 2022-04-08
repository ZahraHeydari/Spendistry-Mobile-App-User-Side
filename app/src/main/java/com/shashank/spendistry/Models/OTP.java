package com.shashank.spendistry.Models;

public class OTP {
    private int otp;
    private String email;
    private String message;
    private String mobile;

    public OTP(String email, int otp) {
        this.otp = otp;
        this.email = email;
    }

    public OTP(String email) {
        this.email = email;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
