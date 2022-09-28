package ir.co.sadad.cartollapi.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.MalformedURLException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * configs for key to communicate with Naji services
 */

@Configuration
@Slf4j
public class CryptoConfig {


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${secretKeyLocation}")
    private String keyLocation;

//    @Bean
//    public PrivateKey privateKey() throws
//            KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
//        char[] password = "P@ssw0rd".toCharArray();
//        KeyStore keystore = keyStore();
//        return (PrivateKey) keystore.getKey("naji", password);
//    }
//
//    @Bean
//    public KeyStore keyStore() throws
//            KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException {
//        KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
//        keystore.load(keyStoreResource.getInputStream(), "P@ssw0rd".toCharArray());
//        return keystore;
//    }


    private InputStream keyClassPathResource(boolean isPrivate) throws IOException {
        return new ClassPathResource(isPrivate ? "private.pem" : "public.pem", this.getClass().getClassLoader()).getInputStream();
    }

    private Resource keyFileResource(boolean isPrivate) throws MalformedURLException {
        return new FileUrlResource(this.keyLocation + File.separator + (isPrivate ? "private.pem" : "public.pem"));
    }

    @Bean
    public PrivateKey readPrivateKey() throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        InputStream classPathResource = this.keyClassPathResource(true);
        Resource fileResource = this.keyFileResource(true);
        log.info("key location is {}", fileResource.exists());
        if (fileResource.exists()) {
            try (FileReader keyReader =
                         new FileReader(fileResource.getURL().getFile());

                 PemReader pemReader = new PemReader(keyReader)) {

                PemObject pemObject = pemReader.readPemObject();
                byte[] content = pemObject.getContent();
                PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
                return (PrivateKey) factory.generatePrivate(privKeySpec);
            }
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource));
            PemReader pemReader = new PemReader(reader);
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return (PrivateKey) factory.generatePrivate(privKeySpec);
        }


    }


    @Bean
    public PublicKey publicKey() throws GeneralSecurityException, IOException {
        InputStream classPathResource = this.keyClassPathResource(false);
        Resource fileResource = this.keyFileResource(false);
        String content;
        if (fileResource.exists()) {
            InputStream certPem = new FileInputStream(new File(fileResource.getURL().getFile()));
            content = IOUtils.toString(certPem);
        } else
            content = IOUtils.toString(classPathResource);
        return getPublicKeyFromString(content);
    }


    private RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace(System.getProperty("line.separator"), "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }


}
