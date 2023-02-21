package ir.co.sadad.cartollapi.enumurations;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum NajiPlateChar {

    PERSIAN_B("ب", "02"),
    PERSIAN_T("ت", "03"),
    PERSIAN_JIM("ج", "04"),
    PERSIAN_D("د", "05"),
    PERSIAN_SIN("س", "06"),
    PERSIAN_SAD("ص", "07"),
    PERSIAN_TA("ط", "08"),
    PERSIAN_EIN("ع", "09"),
    PERSIAN_GH("ق", "10"),
    PERSIAN_L("ل", "11"),
    PERSIAN_M("م", "12"),
    PERSIAN_N("ن", "13"),
    PERSIAN_V("و", "14"),
    PERSIAN_HE("ه", "15"),
    PERSIAN_Y("ی", "16"),
    PERSIAN_ZH("ژ", "19"); //Maloolin

    private static final Map<String, String> PLATE_MAP = new HashMap<>();
    private final String key;
    private final String value;

    static {
        for (NajiPlateChar pchar : values()) {
            PLATE_MAP.put(pchar.getKey(), pchar.getValue());
        }
    }

    NajiPlateChar(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getByKey(String key) {
        return PLATE_MAP.get(key);
    }

    public static Map<String, String> getPlateMap() {
        return PLATE_MAP;
    }

}
