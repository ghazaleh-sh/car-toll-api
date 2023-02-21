package ir.co.sadad.cartollapi.service.util;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles(profiles = {"qa"})
class UtilityTest {


    @Test
    void getCurrentJalaliDate() {
        assertEquals("1401/09/12", Utility.getCurrentJalaliDate());
        //"2023-01-20T16:15:07.110+03:30"
    }
}