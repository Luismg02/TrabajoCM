package com.example.trabajocm.ui.gallery;

import android.content.ContentResolver;
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

    private void loadAndDisplayMetadata(Uri imageUri, String tmp) {
        try {
            // Cargar imagen en el ImageView
            imageView.setImageURI(imageUri);

            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            ContentResolver contentResolver = requireActivity().getContentResolver();

            // Leer metadatos de la imagen utilizando el ContentResolver
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream != null) {
                ExifInterface exifInterface;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    exifInterface = new ExifInterface(inputStream);
                } else {
                    exifInterface = new ExifInterface(imageUri.getPath());
                }
                inputStream.close();

                // Leer metadata properties
                String metadata = "METADATOS::\n\n";
                metadata += "Fecha/Hora: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME) + "\n";
                metadata += "Ancho De La Imagen: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "\n";
                metadata += "Alto De La Imagen: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "\n";
                // Más metadatos
                metadata += "Modelo de la Cámara: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL) + "\n";
                metadata += "Fabricante: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE) + "\n";
                metadata += "Orientación: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION) + "\n";
                metadata += "GPS ALTITUD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE) + "\n";
                metadata += "GPS LONGITUD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) + "\n";
                metadata += "ISO: " + exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS) + "\n";



                // Mostrar metadatos en TextView
                metadataTextView.setText(metadata);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
