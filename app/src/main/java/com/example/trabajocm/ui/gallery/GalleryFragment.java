package com.example.trabajocm.ui.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajocm.R;
import com.example.trabajocm.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {
    private ImageView imageView;
    private Uri imageUri;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imageViewGallery);
        Bundle arguments = getArguments();
        if(arguments != null){
            String imageUriString = arguments.getString("imageUri");
            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                if (imageUri != null) {
                    // Cargar la imagen en el ImageView
                    imageView.setImageURI(imageUri);
                }
            }
        }

        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}