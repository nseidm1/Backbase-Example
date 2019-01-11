package com.noahseidman.backbaseexample.interfaces;

import com.noahseidman.backbaseexample.adapter.MultiTypeDataBoundAdapter;
import com.noahseidman.backbaseexample.models.City;

public interface ICity extends MultiTypeDataBoundAdapter.ActionCallback {

  public void onCityClick(City city);
}
