package com.adwait.deviceauthoritycrypto.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

@Service
public class KeyPairGeneratorService {

    public void generateKeyPairAndCertificate() throws Exception {
        // generate key pair
        // this is used to
        KeyPair keyPair = generateKeyPair();

        // generate x509 certificate
        X509Certificate certificate = generateX509Certificate(keyPair);

        // output both private key and certificate as PEM format to screen.
        outputPrivateKeyAndCertificateToScreen(keyPair.getPrivate(), certificate);
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private X509Certificate generateX509Certificate(KeyPair keyPair) throws Exception {
        // issuer info
        X500Name issuer = new X500Name("CN=ADWAIT, OU=DEVELOPMENT, O=A_COMPANY, C=UK");
        X500Name subject = issuer; // self signed

        // validity period
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();

        // unique certificate identifier
        BigInteger serialNumber = new BigInteger(Long.toString(now));

        // builder certificate
        JcaX509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuer, serialNumber, startDate, endDate, subject, keyPair.getPublic()
        );

        // digital signature which is signed by the private key by the CA (in this case this program)
        // this is to ensure validity and authenticity of certificate
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(signer));
    }

    private void outputPrivateKeyAndCertificateToScreen(
            PrivateKey privateKey, X509Certificate certificate
    ) throws Exception {
        try (StringWriter privateWriter = new StringWriter();
             StringWriter certWriter = new StringWriter();
             JcaPEMWriter privatePemWriter = new JcaPEMWriter(privateWriter);
             JcaPEMWriter certPemWriter = new JcaPEMWriter(certWriter)) {

            // Output private key to console
            privatePemWriter.writeObject(privateKey);
            privatePemWriter.flush();
            System.out.println(privateWriter);

            // Output certificate to console
            certPemWriter.writeObject(certificate);
            certPemWriter.flush();
            System.out.println(certWriter);
        }
    }
}
