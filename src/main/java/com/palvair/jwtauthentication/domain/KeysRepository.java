package com.palvair.jwtauthentication.domain;


import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public interface KeysRepository {

    void saveClePublique(X509EncodedKeySpec keySpec, String path);

    void saveClePrivee(PKCS8EncodedKeySpec keySpec, String path);

    String getClePublique(String path);

    String getClePrivee(String path);
}
