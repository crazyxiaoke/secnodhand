package com.app.secnodhand.entity;

import com.app.secnodhand.base.BaseBean;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MyLocationData;

public class LocationInfo extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String city;
    private String province;
    private String address;
    private String street;
    private String streetNumber;
    private double longitude;
    private double latitude;
    private float accuracy;
    private float direction;

	public static LocationInfo fromBDLocationToLocationInfo(BDLocation location) {
		LocationInfo locationInfo = new LocationInfo();
		locationInfo.city = location.getCity();
		locationInfo.province = location.getProvince();
		locationInfo.address = location.getAddrStr();
		locationInfo.longitude = location.getLongitude();
		locationInfo.latitude = location.getLatitude();
		locationInfo.accuracy = location.getRadius();
		locationInfo.direction =location.getDirection();
		locationInfo.street = location.getStreet();
		locationInfo.streetNumber = location.getStreetNumber();
		return locationInfo;
	}

    public static MyLocationData convertToLocationData(LocationInfo locationInfo) {
        if (locationInfo == null)
            return new MyLocationData.Builder().accuracy(0).direction(0)
                    .latitude(0).longitude(0).build();
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(locationInfo.accuracy).direction(100)
                .latitude(locationInfo.latitude)
                .longitude(locationInfo.longitude).build();
        return locData;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }




}
