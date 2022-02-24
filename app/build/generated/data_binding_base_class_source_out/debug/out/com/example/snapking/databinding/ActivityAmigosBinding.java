// Generated by view binder compiler. Do not edit!
package com.example.snapking.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.snapking.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAmigosBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText editTextTextPersonName;

  @NonNull
  public final FrameLayout fondorecicl;

  @NonNull
  public final ImageButton lupa;

  @NonNull
  public final RecyclerView recicle;

  @NonNull
  public final TextView tvAmigos;

  private ActivityAmigosBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText editTextTextPersonName, @NonNull FrameLayout fondorecicl,
      @NonNull ImageButton lupa, @NonNull RecyclerView recicle, @NonNull TextView tvAmigos) {
    this.rootView = rootView;
    this.editTextTextPersonName = editTextTextPersonName;
    this.fondorecicl = fondorecicl;
    this.lupa = lupa;
    this.recicle = recicle;
    this.tvAmigos = tvAmigos;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAmigosBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAmigosBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_amigos, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAmigosBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.editTextTextPersonName;
      EditText editTextTextPersonName = ViewBindings.findChildViewById(rootView, id);
      if (editTextTextPersonName == null) {
        break missingId;
      }

      id = R.id.fondorecicl;
      FrameLayout fondorecicl = ViewBindings.findChildViewById(rootView, id);
      if (fondorecicl == null) {
        break missingId;
      }

      id = R.id.lupa_;
      ImageButton lupa = ViewBindings.findChildViewById(rootView, id);
      if (lupa == null) {
        break missingId;
      }

      id = R.id.recicle;
      RecyclerView recicle = ViewBindings.findChildViewById(rootView, id);
      if (recicle == null) {
        break missingId;
      }

      id = R.id.tvAmigos;
      TextView tvAmigos = ViewBindings.findChildViewById(rootView, id);
      if (tvAmigos == null) {
        break missingId;
      }

      return new ActivityAmigosBinding((ConstraintLayout) rootView, editTextTextPersonName,
          fondorecicl, lupa, recicle, tvAmigos);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}