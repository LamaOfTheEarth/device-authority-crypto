package com.adwait.deviceauthoritycrypto.component;

import com.adwait.deviceauthoritycrypto.service.KeyPairGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

   @Autowired
   private final KeyPairGeneratorService keyPairGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        keyPairGeneratorService.generateKeyPairAndCertificate();
        System.out.println("Key pair and certificate generated successfully.");
    }
}
