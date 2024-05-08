package com.fir.gateway.uttls;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        String signature = "{msg=[普通的客户端消息], as[]=[aaa, bbb], af[]=[aaa], t=[1690349913434]}";
        System.out.println(signature);
        signature = signature.replaceAll("\\[|\\]", "");
        System.out.println(signature);
    }

}
