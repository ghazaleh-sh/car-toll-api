package ir.co.sadad.cartollapi.service.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"qa"})
@SpringBootTest
@Slf4j
public class TripleDESTest {


    @Autowired
    private TripleDES tripleDES;


    @Test
    public void shouldEncryptInput() throws Exception {
        Assertions.assertNotNull(tripleDES);
        byte[] signature = tripleDES.sign("hello world");
        String hello_world = Base64.toBase64String(signature);
        log.info("the sign: {}", hello_world);

        log.info("the verfied = {}", tripleDES.verify("hello world", signature));

    }


}