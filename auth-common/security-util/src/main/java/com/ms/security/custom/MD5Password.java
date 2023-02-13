package com.ms.security.custom;

import com.ms.common.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5Password implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.encrypt(rawPassword.toString()));
    }
}
