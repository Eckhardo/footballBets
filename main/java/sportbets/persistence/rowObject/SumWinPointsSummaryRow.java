/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.persistence.rowObject;

public class SumWinPointsSummaryRow {
    //	********************** Fields ********************** //
    // commMemb fields
    private String username;

    private Integer sumWinPointsNow;

    private Integer sumWinPointsLast;
    private Integer userPositionLast;
    private Integer diffAbsolute;
    private Integer diffRelative;

    //	********************** Constructors ********************** //
    public SumWinPointsSummaryRow(String username, Integer sumWinPointsNow,
                                  Integer sumWinPointsLast,Integer userPositionLast) {
        this.username = username;
        this.sumWinPointsNow = sumWinPointsNow;
        this.sumWinPointsLast = sumWinPointsLast;
        this.userPositionLast=userPositionLast;
    }

    public String getUsername() {
        return username;
    }

    public Integer getSumWinPointsNow() {
        return sumWinPointsNow;
    }

    public Integer getSumWinPointsLast() {
        return sumWinPointsLast;
    }

    public Integer getUserPositionLast() {
        return userPositionLast;
    }

    public Integer getDiffAbsolute() {
        return diffAbsolute;
    }

    public Integer getDiffRelative() {
        return diffRelative;
    }

    public void setDiffAbsolute(Integer diffAbsolute) {
        this.diffAbsolute = diffAbsolute;
    }

    public void setDiffRelative(Integer diffRelative) {
        this.diffRelative = diffRelative;
    }

    public void setSumWinPointsNow(Integer sumWinPointsNow) {
        this.sumWinPointsNow = sumWinPointsNow;
    }

    @Override
    public String toString() {
        return "SumWinPointsSummaryRow{" +
                "username='" + username + '\'' +
                ", sumWinPointsNow=" + sumWinPointsNow +
                ", sumWinPointsLast=" + sumWinPointsLast +
                ", userPositionLast=" + userPositionLast +
                ", diffAbsolute=" + diffAbsolute +
                ", diffRelative=" + diffRelative +
                '}';
    }
}
