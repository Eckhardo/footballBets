package sportbets.persistence.entity.tipps.enums;

public enum TotoTrend {

   HOME_VICTORY(1), DRAW(0), GUEST_VICTORY(2);

    private final int trendCode;


    TotoTrend(int trendCode) {
        this.trendCode = trendCode;
    }

    public int getTrendCode() {
        return trendCode;
    }
}
