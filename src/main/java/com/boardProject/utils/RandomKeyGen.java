package com.boardProject.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomKeyGen {
    public static String generateVerificationCode(){
        return RandomStringUtils.randomAlphanumeric(24);
    }
}
