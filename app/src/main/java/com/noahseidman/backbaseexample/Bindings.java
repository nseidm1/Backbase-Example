package com.noahseidman.backbaseexample;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class Bindings {


  @BindingAdapter("layout")
  public static void setManager(RecyclerView recyclerView, LayoutManager layoutManager) {
    recyclerView.setLayoutManager(layoutManager);
  }

  @BindingAdapter("visibility")
  public static void setVisibility(View view, boolean visibility) {
    if (visibility) {
      if (view.getTag() == null) {
        return;
      }
      LayoutParams lp = view.getLayoutParams();
      lp.height = (int) view.getTag();
      view.setLayoutParams(lp);
    } else {
      view.setTag(view.getLayoutParams().height);
      LayoutParams lp = view.getLayoutParams();
      lp.height = 0;
      view.setLayoutParams(lp);
    }
  }
}