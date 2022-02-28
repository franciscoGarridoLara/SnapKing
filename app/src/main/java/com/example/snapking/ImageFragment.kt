package com.example.snapking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.squareup.picasso.Picasso


class ImageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_image, container, false)

        var ivFoto = view.findViewById<ImageView>(R.id.ivFoto)

        Picasso.get()
            .load("https://firebasestorage.googleapis.com/v0/b/snap-king.appspot.com/o/logo.png?alt=media&token=44b79190-b123-441c-b7f6-070efec5b920")
            .into(ivFoto)

        return view



    }







}