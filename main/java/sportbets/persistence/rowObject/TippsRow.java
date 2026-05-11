/*
 * Created on 26.09.2005 by eckhard
 * 
 * for Bulitipper Software Solutions
 *
 */
package sportbets.persistence.rowObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 *  Represents  the whole picture table" for tipp result for tippers of a bet community
 */

public class TippsRow implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(TippsRow.class);
	//	********************** Fields ********************** //

	private Long spielId;

	/**
	 * the Date from the DB
	 */
	private Date anpfiffdate;

	/**
	 * displays the startdate
	 */
	private String anpfiffdateDisplay;

	private Integer heimTore;

	private Integer gastTore;

	// commMemb fields
	private String username;

	// team fields
	private String heimName;

	private String gastName;

	/**
	 * the name of the round
	 */
	private String roundName;

	/**
	 * the name of the group
	 */
	private String groupName;

	// tipp fields

	private Long tippId;

	/**
	 * home team wins
	 */
	private Integer heimTipp;

	/**
	 * a draw
	 */
	private Integer remisTipp;

	/**
	 * guest team wins
	 */
	private Integer gastTipp;

	private Integer winPoints;

	private Integer sumWinPoints;


	//	********************** Constructors ********************** //
	public TippsRow(){
		
	}
	public TippsRow(Long spielId, Date anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName) {
		this.spielId = spielId;
		this.anpfiffdate = anpfiffDate;
		this.heimTore = heimtore;
		this.gastTore = gasttore;
		this.username = username;
		this.heimName = heimName;
		this.gastName = gastName;
		this.roundName = roundName;
	}

	/**
	 * 
	 * @param spielId
	 * @param anpfiffDate
	 * @param heimtore
	 * @param gasttore
	 * @param username
	 * @param heimName
	 * @param gastName
	 * @param roundName
	 * @param groupName
	 */
	public TippsRow(Long spielId, Date anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName, String groupName) {
		this(spielId, anpfiffDate, heimtore, gasttore, username, heimName,
				gastName, roundName);
		this.groupName = groupName;
	}

	/**
	 * 
	 * @param spielId
	 * @param anpfiffDate
	 * @param heimtore
	 * @param gasttore
	 * @param username
	 * @param heimName
	 * @param gastName
	 * @param roundName
	 * @param tippId
	 * @param heimTipp
	 * @param remisTipp
	 * @param gastTipp
	 * @param winPoints
	 */
	public TippsRow(Long spielId, Date anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName, Long tippId, Integer heimTipp,
                    Integer remisTipp, Integer gastTipp, Integer winPoints) {
		this(spielId, anpfiffDate, heimtore, gasttore, username, heimName,
				gastName, roundName);
		this.tippId = tippId;
		this.heimTipp = heimTipp;
		this.remisTipp = remisTipp;
		this.gastTipp = gastTipp;
		this.winPoints = winPoints;

	}

	/**
	 * 
	 * @param spielId
	 * @param anpfiffDate
	 * @param heimtore
	 * @param gasttore
	 * @param username
	 * @param heimName
	 * @param gastName
	 * @param roundName
	 * @param groupName
	 * @param tippId
	 * @param heimTipp
	 * @param remisTipp
	 * @param gastTipp
	 * @param winPoints
	 */
	public TippsRow(Long spielId, Date anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName, String groupName, Long tippId,
                    Integer heimTipp, Integer remisTipp, Integer gastTipp,
                    Integer winPoints) {
		this(spielId, anpfiffDate, heimtore, gasttore, username, heimName,
				gastName, roundName, tippId, heimTipp, remisTipp, gastTipp,
				winPoints);
		this.groupName = groupName;

	}
	
	//	********************** Getter/Setter Methods ********************** //
	
	
	/**
	 * @return Returns the anpfiffdate.
	 */
	public Date getAnpfiffdate() {
		return anpfiffdate;
	}
	/**
	 * @param anpfiffdate The anpfiffdate to set.
	 */
	public void setAnpfiffdate(Date anpfiffdate) {
		this.anpfiffdate = anpfiffdate;
	}


	/**
	 * @return Returns the gastName.
	 */
	public String getGastName() {
		return gastName;
	}
	/**
	 * @param gastName The gastName to set.
	 */
	public void setGastName(String gastName) {
		this.gastName = gastName;
	}
	/**
	 * @return Returns the gastTipp.
	 */
	public Integer getGastTipp() {
		return gastTipp;
	}
	/**
	 * @param gastTipp The gastTipp to set.
	 */
	public void setGastTipp(Integer gastTipp) {
		this.gastTipp = gastTipp;
	}
	/**
	 * @return Returns the gastTore.
	 */
	public Integer getGastTore() {
		return gastTore;
	}
	/**
	 * @param gastTore The gastTore to set.
	 */
	public void setGastTore(Integer gastTore) {
		this.gastTore = gastTore;
	}
	/**
	 * @return Returns the groupName.
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName The groupName to set.
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return Returns the heimName.
	 */
	public String getHeimName() {
		return heimName;
	}
	/**
	 * @param heimName The heimName to set.
	 */
	public void setHeimName(String heimName) {
		this.heimName = heimName;
	}
	/**
	 * @return Returns the heimTipp.
	 */
	public Integer getHeimTipp() {
		return heimTipp;
	}
	/**
	 * @param heimTipp The heimTipp to set.
	 */
	public void setHeimTipp(Integer heimTipp) {
		this.heimTipp = heimTipp;
	}
	/**
	 * @return Returns the heimTore.
	 */
	public Integer getHeimTore() {
		return heimTore;
	}
	/**
	 * @param heimTore The heimTore to set.
	 */
	public void setHeimTore(Integer heimTore) {
		this.heimTore = heimTore;
	}
	/**
	 * @return Returns the remisTipp.
	 */
	public Integer getRemisTipp() {
		return remisTipp;
	}
	/**
	 * @param remisTipp The remisTipp to set.
	 */
	public void setRemisTipp(Integer remisTipp) {
		this.remisTipp = remisTipp;
	}
	/**
	 * @return Returns the roundName.
	 */
	public String getRoundName() {
		return roundName;
	}
	/**
	 * @param roundName The roundName to set.
	 */
	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}
	/**
	 * @return Returns the spielId.
	 */
	public Long getSpielId() {
		return spielId;
	}
	/**
	 * @param spielId The spielId to set.
	 */
	public void setSpielId(Long spielId) {
		this.spielId = spielId;
	}
	/**
	 * @return Returns the tippId.
	 */
	public Long getTippId() {
		return tippId;
	}
	/**
	 * @param tippId The tippId to set.
	 */
	public void setTippId(Long tippId) {
		this.tippId = tippId;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return Returns the winPoints.
	 */
	public Integer getWinPoints() {
		return winPoints;
	}
	/**
	 * @param winPoints The winPoints to set.
	 */
	public void setWinPoints(Integer winPoints) {
		this.winPoints = winPoints;
	}
	
	
	/**
	 * @return Returns the sumWinPoints.
	 */
	public Integer getSumWinPoints() {
		return sumWinPoints;
	}
	/**
	 * @param sumWinPoints The sumWinPoints to set.
	 */
	public void setSumWinPoints(Integer sumWinPoints) {
		this.sumWinPoints = sumWinPoints;
	}
}