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
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "taxi_data")
public class TaxiDataObject
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taxiDataId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Column(name = "pickup_time")
    private Date pickupTime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Column(name = "dropoff_time")
    private Date dropOffTime;
    private double pickupLat;
    private double pickupLon;
    private double dropoffLat;
    private double dropoffLon;

    private String pickupAddress;
    private String dropOffAddress;

    public TaxiDataObject()
    {
    }

    public Date getPickupTime()
    {
        return pickupTime;
    }

    public void setPickupTime( Date pickupTime )
    {
        this.pickupTime = pickupTime;
    }

    public Date getDropOffTime()
    {
        return dropOffTime;
    }

    public void setDropOffTime( Date dropOffTime )
    {
        this.dropOffTime = dropOffTime;
    }

    public double getPickupLat()
    {
        return pickupLat;
    }

    public void setPickupLat( double pickupLat )
    {
        this.pickupLat = pickupLat;
    }

    public double getPickupLon()
    {
        return pickupLon;
    }

    public void setPickupLon( double pickupLon )
    {
        this.pickupLon = pickupLon;
    }

    public double getDropoffLat()
    {
        return dropoffLat;
    }

    public void setDropoffLat( double dropoffLat )
    {
        this.dropoffLat = dropoffLat;
    }

    public double getDropoffLon()
    {
        return dropoffLon;
    }

    public void setDropoffLon( double dropoffLon )
    {
        this.dropoffLon = dropoffLon;
    }

    public LocalDateTime _getLocalPickupTime()
    {
        return LocalDateTime.ofInstant( pickupTime.toInstant(), ZoneId.systemDefault() );
    }

    public LocalDateTime _getLocalDropOffTime()
    {
        return LocalDateTime.ofInstant( dropOffTime.toInstant(), ZoneId.systemDefault() );
    }

    public String getPickupAddress()
    {
        return pickupAddress;
    }

    public void setPickupAddress( String pickupAddress )
    {
        this.pickupAddress = pickupAddress;
    }

    public String getDropOffAddress()
    {
        return dropOffAddress;
    }

    public void setDropOffAddress( String dropOffAddress )
    {
        this.dropOffAddress = dropOffAddress;
    }

    public long getDuration()
    {
        return _getLocalPickupTime().until( _getLocalDropOffTime(), ChronoUnit.DAYS );
    }

    public String getPickupPostalCode()
    {
        return ObjectMapper.getPostalCode( pickupAddress );
    }

    public String getDropOffPostalCode()
    {
        return ObjectMapper.getPostalCode( dropOffAddress );
    }

    @Override
    public String toString()
    {
        return "TaxiDataObject{" + "taxiDataId=" + taxiDataId + ", pickupTime=" + pickupTime + ", dropOffTime=" + dropOffTime + ", pickupLat=" + pickupLat + ", pickupLon=" + pickupLon + ", dropoffLat=" + dropoffLat + ", dropoffLon=" + dropoffLon + ", pickupAddress='" + pickupAddress + '\'' + ", dropOffAddress='" + dropOffAddress + '\'' + '}';
    }
}
