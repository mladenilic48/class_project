package com.ITtraining.project.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	public static String getPassEncoded(String pass) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(pass);
	}

	// ovo se pokrece pri prvom pokretanju da bi nam ispisao u kozoli enkriptovanu vrednost koju cemo
	// kopirati u db na mestu sifre, da bi skrili od ljudskog oka
	public static void main(String[] args) {
		System.out.println(getPassEncoded("pass"));
	}

}
