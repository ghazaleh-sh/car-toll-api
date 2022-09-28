package ir.co.sadad.cartollapi.enumurations;

import java.util.HashMap;
import java.util.Map;

public enum PlateChar {

    PERSIAN_ALEF("الف", "01"),
    PERSIAN_B("ب", "02"),
    PERSIAN_P("پ", "03"),
    PERSIAN_T("ت", "04"),
    PERSIAN_S("ث", "05"),
    PERSIAN_JIM("ج", "06"),
    PERSIAN_CHE("چ", "07"),
    PERSIAN_H("ح", "08"),
    PERSIAN_KH("خ", "09"),
    PERSIAN_D("د", "10"),
    PERSIAN_ZAL("ذ", "11"),
    PERSIAN_R("ر", "12"),
    PERSIAN_Z("ز", "13"),
    PERSIAN_JE("ژ", "14"),
    PERSIAN_SIN("س", "15"),
    PERSIAN_SHIN("ش", "16"),
    PERSIAN_SAD("ص", "17"),
    PERSIAN_ZAD("ض", "18"),
    PERSIAN_TA("ط", "19"),
    PERSIAN_ZA("ظ", "20"),
    PERSIAN_EIN("ع", "21"),
    PERSIAN_GHEIN("غ", "22"),
    PERSIAN_F("ف", "23"),
    PERSIAN_GH("ق", "24"),
    PERSIAN_K("ک", "25"),
    PERSIAN_G("گ", "26"),
    PERSIAN_L("ل", "27"),
    PERSIAN_M("م", "28"),
    PERSIAN_N("ن", "29"),
    PERSIAN_V("و", "30"),
    PERSIAN_HE("ه", "31"),
    PERSIAN_Y("ی", "32"),
    DISABLED_PEOPLE("معلولین", "33"),
    CEREMONIES("تشریفات", "34"),
    A("A", "51"),
    B("B", "52"),
    C("C", "53"),
    D("D", "54"),
    E("E", "55"),
    F("F", "56"),
    G("G", "57"),
    H("H", "58"),
    I("I", "59"),
    J("J", "60"),
    K("K", "61"),
    L("L", "62"),
    M("M", "63"),
    N("N", "64"),
    O("O", "65"),
    P("P", "66"),
    Q("Q", "67"),
    R("R", "68"),
    S("S", "69"),
    T("T", "70"),
    U("U", "71"),
    V("V", "72"),
    W("W", "73"),
    X("X", "74"),
    Y("Y", "75"),
    Z("Z", "76");


    private static final Map<String, String> PLATE_MAP = new HashMap<>();

    static {
        for (PlateChar pchar : values()) {
            PLATE_MAP.put(pchar.getKey(), pchar.getValue());
        }
    }

    private String key;
    private String value;

    PlateChar(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public static String getByKey(String key) {
        return PLATE_MAP.get(key);
    }

    public static Map<String, String> getPlateMap() {
        return PLATE_MAP;
    }

    public String toString() {
        return name() + "=" + value;
    }
}
