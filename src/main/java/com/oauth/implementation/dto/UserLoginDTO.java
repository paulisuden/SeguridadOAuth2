package com.oauth.implementation.dto;

/*
Este objeto se utiliza para recibir la información ingresada
por el usuario y luego esa información se envía de manera segura al backend para que se
valide el inicio de sesión.
 */

public class UserLoginDTO {
	
	private String username;
	
	private String password;
	
	private int otp; //código de autenticación OTP (One-Time Password) en caso de que se use autenticación de dos factores (2FA).

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}
}
