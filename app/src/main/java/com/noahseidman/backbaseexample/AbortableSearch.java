package com.noahseidman.backbaseexample;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import com.noahseidman.backbaseexample.adapter.MultiTypeDataBoundAdapter;
import com.noahseidman.backbaseexample.interfaces.ProgressCallback;
import com.noahseidman.backbaseexample.models.City;
import java.util.ArrayList;
import java.util.List;

public class AbortableSearch implements Runnable {

  private ArrayList<City> cities;
  public String search;
  private boolean aborted = false;
  private int cityIndex;
  private boolean cityEquivalence;
  private Handler handler;
  private MultiTypeDataBoundAdapter adapter;
  private LinearLayoutManager manager;
  private ProgressCallback progressCallback;

  public AbortableSearch(ArrayList<City> cities, String search) {
    this.cities = cities;
    this.search = search;
  }

  public void setAdapter(MultiTypeDataBoundAdapter adapter) {
    this.adapter = adapter;
  }

  public void setLayoutManager(LinearLayoutManager manager) {
    this.manager = manager;
  }

  public void setProgressCallback(ProgressCallback progressCallback) {
    this.progressCallback = progressCallback;
  }

  public void setHandler(Handler handler) {
    this.handler = handler;
  }

  public void abort() {
    aborted = true;
  }

  @Override
  public void run() {
    handler.post(() -> progressCallback.showProgress(true));
    final List<Object> newCities = new ArrayList<>();
    for (final City city : cities) {
      if (aborted) {
        handler.post(() -> progressCallback.showProgress(false));
        break;
      }
      if (search.length() == 0 || startsWith(city.getName(), search)) {
        newCities.add(city);
      }
    }
    handler.post(() -> adapter.setItems(newCities));
    if (search.length() == 0) {
      handler.post(() -> manager.scrollToPosition(0));
    }
    handler.post(() -> progressCallback.showProgress(false));
  }

  public boolean startsWith(String value, String prefix) {
    if (TextUtils.isEmpty(value) || TextUtils.isEmpty(prefix)) {
      return false;
    }
    int length = prefix.length();
    if (length > value.length()) {
      return false;
    }
    for (int i = 0; i < length; i++) {
      if (Character.toLowerCase(value.charAt(i)) != Character.toLowerCase(prefix.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}