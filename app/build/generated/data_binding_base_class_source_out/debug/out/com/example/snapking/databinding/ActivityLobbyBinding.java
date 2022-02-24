// Generated by view binder compiler. Do not edit!
package com.example.snapking.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLobbyBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CircularImageView avatar;

  @NonNull
  public final ImageButton btnReady;

  @NonNull
  public final FrameLayout fondorecicl;

  @NonNull
  public final ImageButton lupa1;

  @NonNull
  public final ImageButton plusCircle;

  @NonNull
  public final RecyclerView recicle;

  @NonNull
  public final TextView textView;

  private ActivityLobbyBinding(@NonNull ConstraintLayout rootView,
      @NonNull CircularImageView avatar, @NonNull ImageButton btnReady,
      @NonNull FrameLayout fondorecicl, @NonNull ImageButton lupa1, @NonNull ImageButton plusCircle,
      @NonNull RecyclerView recicle, @NonNull TextView textView) {
    this.rootView = rootView;
    this.avatar = avatar;
    this.btnReady = btnReady;
    this.fondorecicl = fondorecicl;
    this.lupa1 = lupa1;
    this.plusCircle = plusCircle;
    this.recicle = recicle;
    this.textView = textView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLobbyBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLobbyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_lobby, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLobbyBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.avatar;
      CircularImageView avatar = ViewBindings.findChildViewById(rootView, id);
      if (avatar == null) {
        break missingId;
      }

      id = R.id.btnReady;
      ImageButton btnReady = ViewBindings.findChildViewById(rootView, id);
      if (btnReady == null) {
        break missingId;
      }

      id = R.id.fondorecicl;
      FrameLayout fondorecicl = ViewBindings.findChildViewById(rootView, id);
      if (fondorecicl == null) {
        break missingId;
      }

      id = R.id.lupa_1;
      ImageButton lupa1 = ViewBindings.findChildViewById(rootView, id);
      if (lupa1 == null) {
        break missingId;
      }

      id = R.id.plus_circle;
      ImageButton plusCircle = ViewBindings.findChildViewById(rootView, id);
      if (plusCircle == null) {
        break missingId;
      }

      id = R.id.recicle;
      RecyclerView recicle = ViewBindings.findChildViewById(rootView, id);
      if (recicle == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new ActivityLobbyBinding((ConstraintLayout) rootView, avatar, btnReady, fondorecicl,
          lupa1, plusCircle, recicle, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}