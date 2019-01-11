package com.noahseidman.backbaseexample;

import android.content.Intent;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.noahseidman.backbaseexample.adapter.MultiTypeDataBoundAdapter;
import com.noahseidman.backbaseexample.databinding.ActivityMainBinding;
import com.noahseidman.backbaseexample.interfaces.ICity;
import com.noahseidman.backbaseexample.interfaces.ProgressCallback;
import com.noahseidman.backbaseexample.models.City;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ProgressCallback {

  private ICity cityCallback = city -> {
    String mapLocation =
        "geo:" + city.getCoord().getLatitude() + "," + city.getCoord().getLongitude();
    Log.d(MainActivity.class.getSimpleName(), "Map Location: " + mapLocation);
    Uri gmmIntentUri = Uri.parse(mapLocation);
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    mapIntent.setPackage("com.google.android.apps.maps");
    if (mapIntent.resolveActivity(getPackageManager()) != null) {
      startActivity(mapIntent);
    }
  };

  private MultiTypeDataBoundAdapter adapter = new MultiTypeDataBoundAdapter(cityCallback);

  private ActivityMainBinding binding;
  private Executor executor = Executors.newCachedThreadPool();
  private Handler handler = new Handler(Looper.getMainLooper());
  private ArrayList<City> cities;
  private AbortableSearch currentSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    setSupportActionBar(binding.toolbar);
    binding.setLayout(new LinearLayoutManager(this));
    binding.setAdapter(adapter);
    binding.progress.setVisibility(View.VISIBLE);
    executor.execute(() -> {
      try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<City>>() {
        }.getType();
        String json = getJson();
        Log.d(MainActivity.class.getSimpleName(), "JSON: " + json);
        cities = gson.fromJson(getJson(), listType);
        Collections.sort(cities, (o1, o2) -> {
          int nameCompare = o1.getName().compareToIgnoreCase(o2.getName());
          if (nameCompare != 0) {
            return nameCompare;
          } else {
            return o1.getCountry().compareToIgnoreCase(o2.getCountry());
          }
        });
        Log.d(MainActivity.class.getSimpleName(), "Size: " + cities.toString());
        handler.post(() -> {
          for (City city : cities) {
            city.setVisible(true);
            adapter.addItem(city);
          }
          binding.progress.setVisibility(View.GONE);
        });
      } catch (IOException e) {
        e.printStackTrace();
      }

    });
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);

    final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
    myActionMenuItem.setOnActionExpandListener(new OnActionExpandListener() {
      @Override
      public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
      }

      @Override
      public boolean onMenuItemActionCollapse(MenuItem item) {
        if (currentSearch != null) {
          currentSearch.abort();
        }
        return true;
      }
    });
    final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(final String s) {
        if (currentSearch != null) {
          if (currentSearch.search.equals(s)) {
            return false;
          }
          currentSearch.abort();
        }
        currentSearch = new AbortableSearch(cities, s);
        currentSearch.setAdapter(adapter);
        currentSearch.setLayoutManager(binding.getLayout());
        currentSearch.setProgressCallback(MainActivity.this::showProgress);
        currentSearch.setHandler(handler);
        executor.execute(currentSearch);
        return false;
      }
    });
    return super.onCreateOptionsMenu(menu);
  }

  private String getJson() throws IOException {
    AssetManager assetManager = getAssets();
    InputStream inputStream = assetManager.open("cities.json");
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    StringBuilder buf = new StringBuilder();
    String str;
    while ((str = in.readLine()) != null) {
      buf.append(str);
    }
    in.close();
    return buf.toString();
  }

  @Override
  public void showProgress(boolean show) {
    binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
  }
}
