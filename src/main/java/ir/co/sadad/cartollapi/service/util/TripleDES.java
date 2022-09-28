package ir.co.sadad.cartollapi.service.util;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

@Component
public class TripleDES {

    private static final String UNICODE_FORMAT = "UTF8";
    private static final String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    private static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    private PrivateKey privateKey;

    @Autowired
    private PublicKey publicKey;


    private static byte[] encode(byte[] input, byte[] key) throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        init();
        Cipher encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
        encrypter.init(Cipher.ENCRYPT_MODE, buildKey(key));
        return encrypter.doFinal(input);
    }

    private static Key buildKey(byte[] key) {
        init();
        String ALGORITHM = "DESede";
        return new SecretKeySpec(key, ALGORITHM);
    }

    public static byte[] encrypt(String plainText, String workingkey) throws IllegalBlockSizeException,
            BadPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            NoSuchPaddingException,
            InvalidKeyException,
            UnsupportedEncodingException {

        byte[] key = Base64.decode(workingkey);
        byte[] encryptedByte = TripleDES.encode(plainText.getBytes(UNICODE_FORMAT), key);
        return Base64.encode(encryptedByte);
    }

    @SneakyThrows
    public byte[] sign(String input) {

        /*byte[] id = new byte[]{0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20};
        byte[] derDigestInfo = new byte[id.length + hashBytes.length];
        System.arraycopy(id, 0, derDigestInfo, 0, id.length);
        System.arraycopy(hashBytes, 0, derDigestInfo, id.length, hashBytes.length);*/

        // SIGN HASH
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
//        return Base64.toBase64String(signatureBytes);
        return signatureBytes;
    }


    public Boolean verify(String input, byte[] signatureBytes) throws Exception {

        //INITIALIZE SIGNATURE
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        //VERIFY SIGNATURE
        boolean verified = signature.verify(signatureBytes);

        //DISPLAY VERIFICATION
        System.out.println("VERIFIED  = " + verified);

        //RETURN SIGNATURE
        return verified;

    }

}