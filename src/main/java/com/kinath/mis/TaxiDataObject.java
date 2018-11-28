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
}
