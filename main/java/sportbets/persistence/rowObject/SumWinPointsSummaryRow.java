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

    private Integer position;
    private Integer positionLast;
    private String username;
    private Integer sumWinPoints;
    private Integer diffAbsolute;
    private Integer diffRelative;

    //	********************** Constructors ********************** //
    public SumWinPointsSummaryRow(String username, Integer sumWinPoints,
                                  Integer position, Integer positionLast) {
        this.username = username;
        this.sumWinPoints = sumWinPoints;
        this.position = position;
        this.positionLast = positionLast;
    }

    public String getUsername() {
        return username;
    }

    public Integer getSumWinPoints() {
        return sumWinPoints;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getPositionLast() {
        return positionLast;
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

    public void setSumWinPoints(Integer sumWinPointsNow) {
        this.sumWinPoints = sumWinPointsNow;
    }

    public void setPosition(int position) {
        this.position=position;
    }
    @Override
    public String toString() {
        return "SumWinPointsSummaryRow{" +
                "username='" + username + '\'' +
                ", sumWinPoints=" + sumWinPoints +
                ", position=" + position +
                ", positionLast=" + positionLast +
                ", diffAbsolute=" + diffAbsolute +
                ", diffRelative=" + diffRelative +
                '}';
    }

}
