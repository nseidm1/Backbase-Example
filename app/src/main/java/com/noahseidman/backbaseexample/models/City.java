package com.noahseidman.backbaseexample.models;

import android.databinding.BaseObservable;
import com.noahseidman.backbaseexample.Bindings;
import com.noahseidman.backbaseexample.R;
import com.noahseidman.backbaseexample.adapter.DataBoundViewHolder;
import com.noahseidman.backbaseexample.adapter.DynamicBinding;
import com.noahseidman.backbaseexample.adapter.LayoutBinding;

public class City extends BaseObservable implements LayoutBinding, DynamicBinding {

  String country;
  String name;
  String comparableName;
  int _id;
  Coordinate coord;
  boolean visible = true;

  public City(String country, String name, int _id, Coordinate coord) {
    this.country = country;
    this.name = name;
    this._id = _id;
    this.coord = coord;
  }

  public String getCountry() {
    return country;
  }

  public boolean getVisibility() {
    return visible;
  }

  public void setVisible(boolean visible) {
    if (this.visible == visible) {
      return;
    }
    this.visible = visible;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.comparableName = name.toLowerCase();
  }

  public String getComparableName() {
    return comparableName;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public Coordinate getCoord() {
    return coord;
  }

  public void setCoord(Coordinate coord) {
    this.coord = coord;
  }

  @Override
  public int getLayoutId() {
    return R.layout.city;
  }

  @Override
  public boolean equals(Object o) {
    City city = (City) o;
    return city._id == _id;
  }

  @Override
  public void bind(DataBoundViewHolder holder) {
    Bindings.setVisibility(holder.itemView, visible);
  }
}
