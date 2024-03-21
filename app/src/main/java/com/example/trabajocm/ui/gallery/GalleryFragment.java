package com.example.trabajocm.ui.gallery;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trabajocm.R;

import java.io.File;
import java.io.IOException;

public class GalleryFragment extends Fragment {

    private ImageView imageView;
    private TextView metadataTextView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        imageView = view.findViewById(R.id.imagenCargadoUsuario); // Cambio aquí
        metadataTextView = view.findViewById(R.id.metadataTextView);

        // Obtener la URI de la imagen de los argumentos
        String imageUriString = getArguments().getString("imageUri");
        Uri myUri = Uri.parse(imageUriString);
        loadAndDisplayMetadata(myUri, imageUriString);

        return view;
    }

    private void loadAndDisplayMetadata(Uri imageUri, String tmp) {
        try {
            // Cargar imagen en el ImageView
            imageView.setImageURI(imageUri);

            // Create ExifInterface instance to access image metadata
            File file = new File(imageUri.getPath());//create path from uri
            ExifInterface exifInterface = null;


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                exifInterface = new ExifInterface(file);
            }
            // Read metadata properties
            String metadata = "Image Metadata:\n\n";
            metadata += "Date/Time: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME) + "\n";
            metadata += "Image Width: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "\n";
            metadata += "Image Height: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "\n";
            // Add more metadata properties as needed

            // Mostrar metadatos en TextView
            metadataTextView.setText(metadata);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
