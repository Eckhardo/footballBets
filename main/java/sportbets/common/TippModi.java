package sportbets.common;

public enum TippModi {

    TIPPMODUS_TOTO("TOTO_TIPP"),
    TIPPMODUS_POINT("POINT_TIPP"),
    TIPPMODUS_RESULT("RESULT_TIPP"),;

    private final String displayName;
    TippModi(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
