package com.k11.automation.coreframework.util;

/**
     * This interface should be implemented to support encrypted password. You
     * should use any algorithm of your choice to encrypt password and implement
     * this interface to provide decrypted password.
     */
    public interface PasswordDecryptor {
        public String getDecryptedPassword(String encriptedPassword);
    }
