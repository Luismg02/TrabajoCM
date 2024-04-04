package com.example.trabajocm.ui.gallery;

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.io.InputStream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


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

    String getMiEtiqueta(String etiqueta,  Metadata metadata)
    {
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().equals(etiqueta)) {
                    return tag.getDescription();
                    //metadataString.append(tag.getTagName()).append(": ").append(tag.getDescription()).append("\n");
                }
            }
        }
        return null;
    }
    private void loadAndDisplayMetadata(Uri imageUri, String tmp) {
        try {
            // Cargar imagen en el ImageView
            imageView.setImageURI(imageUri);
            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            ContentResolver contentResolver = requireActivity().getContentResolver();

            // Leer metadatos de la imagen utilizando metadata-extractor
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                // Construir una cadena con los metadatos
                StringBuilder metadataString = new StringBuilder("METADATOS::\n\n");

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        Log.d("asd", tag.getTagName());
                            metadataString.append(tag.getTagName()).append(": ").append(tag.getDescription()).append("\n");

                    }
                }



                // Mostrar metadatos en TextView
                metadataTextView.setText(metadataString.toString());
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
