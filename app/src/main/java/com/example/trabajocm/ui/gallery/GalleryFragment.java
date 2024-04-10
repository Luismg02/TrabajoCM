package com.example.trabajocm.ui.gallery;

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


public class GalleryFragment extends Fragment {

    private ImageView imageView;
    private TextView metadataTextView;

    private Button miBoton;

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
        miBoton = view.findViewById(R.id.miBoton);


        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri myUri = mainActivity.selectedImageUri;
            if(myUri!=null){
                String imageUriString = myUri.toString();
                loadAndDisplayMetadata(myUri, imageUriString);
            }else{
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
            }

        }

        // Obtener la URI de la imagen de los argumentos
        //String imageUriString = getArguments().getString("imageUri");
        //Uri myUri = Uri.parse(imageUriString);


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

    private boolean checkGPSAvailability(Metadata metadata) {
        String gpsLatitude = getMiEtiqueta("GPS Latitude", metadata);
        String gpsLongitude = getMiEtiqueta("GPS Longitude", metadata);
        return (gpsLatitude != null && gpsLongitude != null);
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

                String fecha = getMiEtiqueta("Date/Time", metadata);
                metadataString.append("Fecha/Hora").append(": ").append(fecha).append("\n");
                String gpslatitude = getMiEtiqueta("GPS Latitude", metadata);
                metadataString.append("Latitud GPS").append(": ").append(gpslatitude).append("\n");
                String gpslongitud = getMiEtiqueta("GPS Longitude", metadata);
                metadataString.append("Longitud GPS").append(": ").append(gpslongitud).append("\n");
                String modelo = getMiEtiqueta("Model", metadata);
                metadataString.append("Modelo").append(": ").append(modelo).append("\n");
                String fabricante = getMiEtiqueta("Make", metadata);
                metadataString.append("Fabricante").append(": ").append(fabricante).append("\n");
                String ancho = getMiEtiqueta("Image Width", metadata);
                metadataString.append("Ancho De La Imagen").append(": ").append(ancho).append("\n");
                String alto = getMiEtiqueta("Image Height", metadata);
                metadataString.append("Alto De La Imagen").append(": ").append(alto).append("\n");


                // Mostrar metadatos en TextView
                metadataTextView.setText(metadataString.toString());

                // Mostrar el botón si los valores de latitud y longitud GPS están disponibles
                if (checkGPSAvailability(metadata)) {
                    // Hacer visible el botón
                    miBoton.setVisibility(View.VISIBLE);
                } else {
                    // Ocultar el botón
                    miBoton.setVisibility(View.GONE);
                }

                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
