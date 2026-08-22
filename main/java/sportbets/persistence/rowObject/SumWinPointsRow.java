/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.persistence.rowObject;

import java.util.Objects;

public class SumWinPointsRow {
    //	********************** Fields ********************** //

    private String username;

    private Long sumWinPoints;

    //	********************** Constructors ********************** //
    public SumWinPointsRow() {

    }
    public SumWinPointsRow(String username, Long sumWinPoints) {
        this.username = username;
        this.sumWinPoints = sumWinPoints;
    }

    public String getUsername() {
        return username;
    }

    public Long getSumWinPoints() {
        return sumWinPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SumWinPointsRow that = (SumWinPointsRow) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "SumWinPointsRow{" +
                "username='" + username + '\'' +
                ", sumWinPoints=" + sumWinPoints +
                '}';
    }
}
