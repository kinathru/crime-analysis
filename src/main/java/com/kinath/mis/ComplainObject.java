package com.kinath.mis;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "complain")
public class ComplainObject
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long complainId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Column(name = "cmp_from_time")
    private Date complainFromTime;

    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Column(name = "cmp_to_time")
    private Date complainToTime;

    @Temporal( TemporalType.TIMESTAMP )
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Column(name = "report_time")
    private Date complainReportTime;

    private String offenseCode;
    private String crimeStatus;
    private String locationCity;
    private String locationDesc;
    private String premiseDesc;
    private double lat;
    private double lon;
    private double xCord;
    private double yCord;

    public ComplainObject()
    {
    }

    public Date getComplainFromTime()
    {
        return complainFromTime;
    }

    public void setComplainFromTime( Date complainFromTime )
    {
        this.complainFromTime = complainFromTime;
    }

    public Date getComplainToTime()
    {
        return complainToTime;
    }

    public void setComplainToTime( Date complainToTime )
    {
        this.complainToTime = complainToTime;
    }

    public Date getComplainReportTime()
    {
        return complainReportTime;
    }

    public void setComplainReportTime( Date complainReportTime )
    {
        this.complainReportTime = complainReportTime;
    }

    public String getOffenseCode()
    {
        return offenseCode;
    }

    public void setOffenseCode( String offenseCode )
    {
        this.offenseCode = offenseCode;
    }

    public String getCrimeStatus()
    {
        return crimeStatus;
    }

    public void setCrimeStatus( String crimeStatus )
    {
        this.crimeStatus = crimeStatus;
    }

    public String getLocationDesc()
    {
        return locationDesc;
    }

    public void setLocationDesc( String locationDesc )
    {
        this.locationDesc = locationDesc;
    }

    public String getPremiseDesc()
    {
        return premiseDesc;
    }

    public void setPremiseDesc( String premiseDesc )
    {
        this.premiseDesc = premiseDesc;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat( double lat )
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon( double lon )
    {
        this.lon = lon;
    }

    public String getLocationCity()
    {
        return locationCity;
    }

    public void setLocationCity( String locationCity )
    {
        this.locationCity = locationCity;
    }

    public double getxCord()
    {
        return xCord;
    }

    public void setxCord( double xCord )
    {
        this.xCord = xCord;
    }

    public double getyCord()
    {
        return yCord;
    }

    public void setyCord( double yCord )
    {
        this.yCord = yCord;
    }

    public LocalDateTime _getLocalCmpFromTime()
    {
        return LocalDateTime.ofInstant( complainFromTime.toInstant(), ZoneId.systemDefault() );
    }

    public LocalDateTime _getLocalCmpToTime()
    {
        return LocalDateTime.ofInstant( complainToTime.toInstant(), ZoneId.systemDefault() );
    }
}
