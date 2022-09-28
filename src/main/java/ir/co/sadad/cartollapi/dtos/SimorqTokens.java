package ir.co.sadad.cartollapi.dtos;

public class SimorqTokens {

    private static SimorqTokens SINGLE_INSTANCE = null;
    private static String accessToken;
    private static String refreshToken;

    public SimorqTokens() {
        this.setAccessToken(null);
        this.setRefreshToken(null);
    }

    public static SimorqTokens getInstance() {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new SimorqTokens();
        }
        return SINGLE_INSTANCE;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        SimorqTokens.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        SimorqTokens.refreshToken = refreshToken;
    }
}
