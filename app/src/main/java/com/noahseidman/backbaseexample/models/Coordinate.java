package com.noahseidman.backbaseexample.models;

public class Coordinate {

  private float lon;
  private float lat;

  public Coordinate(float lon, float lat) {
    this.lon = lon;
    this.lat = lat;
  }

  public float getLatitude() {
    return lat;
  }

  public float getLongitude() {
    return lon;
  }
}
