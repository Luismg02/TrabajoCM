package com.example.trabajocm.ui.gallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;


public class GalleryFragment extends Fragment {

    private ImageView imageView;
    private TextView fecha;
    private TextView latitude;
    private TextView longitud;
    private TextView model;
    private TextView make;
    private TextView width;
    private TextView height;
    private TextView metadata_explainer;
    private Button btn;
    private GoogleMap gmap;

    private Button btn2;

    private LinearLayout data_display;
    private LinearLayout map;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();

        fecha = getView().findViewById(R.id.text_fecha);
        latitude = getView().findViewById(R.id.text_latitude);
        longitud = getView().findViewById(R.id.text_longitug);
        model = getView().findViewById(R.id.text_model);
        make = getView().findViewById(R.id.text_make);
        width = getView().findViewById(R.id.text_width);
        height = getView().findViewById(R.id.text_height);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri imageUri = mainActivity.selectedImageUri;
            String fecha_meta = mainActivity.fecha_meta;
            String latitud_meta = mainActivity.latitud_meta;
            String longitud_meta = mainActivity.longitud_meta;
            String model_meta = mainActivity.model_meta;
            String make_meta = mainActivity.make_meta;
            String width_meta = mainActivity.width_meta;
            String height_meta = mainActivity.height_meta;
            fecha.setText("Fecha: " + fecha_meta);
            latitude.setText("Latitud: " + latitud_meta);
            longitud.setText("Longitud: " + longitud_meta);
            model.setText("Modelo: " + model_meta);
            make.setText("Fabricante: " + make_meta);
            width.setText("Ancho: " + width_meta);
            height.setText("Altura: " + height_meta);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout diseñado
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Establecemos todos los elementos necesarios
        imageView = view.findViewById(R.id.imagen_cargada_mostrar);
        fecha = view.findViewById(R.id.text_fecha);
        latitude = view.findViewById(R.id.text_latitude);
        longitud = view.findViewById(R.id.text_longitug);
        model = view.findViewById(R.id.text_model);
        make = view.findViewById(R.id.text_make);
        width = view.findViewById(R.id.text_width);
        height = view.findViewById(R.id.text_height);
        btn = view.findViewById(R.id.btn_coordenadas);
        data_display = view.findViewById(R.id.data_display);
        metadata_explainer = view.findViewById(R.id.metadataTextView);
        map = view.findViewById(R.id.map);
        map.setVisibility(View.GONE);
        btn2 = view.findViewById(R.id.btn_borrar_mapa);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_maps_fragment);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setVisibility(View.VISIBLE);
                data_display.setVisibility(View.GONE);
                metadata_explainer.setVisibility(View.GONE);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // When map is loaded
                        LatLng location = new LatLng(55.6761, 12.5683);
                        googleMap.addMarker(new MarkerOptions().position(location).title("Posicion"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                        //googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            //@Override
                            //public void onMapClick(LatLng latLng) {
                                // When clicked on map

                                // Initialize marker options
                                //MarkerOptions markerOptions=new MarkerOptions();
                                // Set position of marker
                                //markerOptions.position(latLng);
                                // Set title of marker
                                //markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                                // Remove all marker
                                //googleMap.clear();
                                // Animating to zoom the marker
                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                // Add marker on map
                                //googleMap.addMarker(markerOptions);
                            //}
                        //});
                    }
                });
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setVisibility(View.GONE);
                data_display.setVisibility(View.VISIBLE);
                metadata_explainer.setVisibility(View.VISIBLE);
            }
        });

        // Obtenemos la imagen si se ha cargado
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri imageUri = mainActivity.selectedImageUri;
            String fecha_meta = mainActivity.fecha_meta;
            String latitud_meta = mainActivity.latitud_meta;
            String longitud_meta = mainActivity.longitud_meta;
            String model_meta = mainActivity.model_meta;
            String make_meta = mainActivity.make_meta;
            String width_meta = mainActivity.width_meta;
            String height_meta = mainActivity.height_meta;

            if (imageUri != null) {
                data_display.setVisibility(View.VISIBLE);
                imageView.setImageURI(imageUri);
                metadata_explainer.setText(R.string.mostrando_metadatos);
                fecha.setText("Fecha: " + fecha_meta);
                latitude.setText("Latitud: " + latitud_meta);
                longitud.setText("Longitud: " + longitud_meta);
                model.setText("Modelo: " + model_meta);
                make.setText("Fabricante: " + make_meta);
                width.setText("Ancho: " + width_meta);
                height.setText("Altura: " + height_meta);
                //loadAndDisplayMetadata(imageUri);
            } else {
                data_display.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
                metadata_explainer.setText(R.string.metadatos_por_mostrar);
            }

        }

        return view;
    }

    String getMiEtiqueta(String etiqueta, Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().equals(etiqueta)) {
                    return tag.getDescription();
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

    private void loadAndDisplayMetadata(Uri imageUri) {
        try {
            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            ContentResolver contentResolver = requireActivity().getContentResolver();
            // Leer metadatos de la imagen utilizando metadata-extractor
            InputStream inputStream3 = contentResolver.openInputStream(imageUri);
            List<List<String>> lista = new ArrayList<>();

            if (inputStream3 != null) {
                List<org.apache.commons.imaging.formats.tiff.TiffField> listaAux = new ArrayList<>();

                final ImageMetadata metadata = Imaging.getMetadata(inputStream3, imageUri.getPath());
                if (metadata instanceof JpegImageMetadata) {
                    final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                    final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                    if (null != exifMetadata) {
                        listaAux = exifMetadata.getAllFields();
                    }
                }
                for (org.apache.commons.imaging.formats.tiff.TiffField tag : listaAux) {
                    ArrayList<String> item = new ArrayList<>();
                    item.add(tag.getTagName());
                    item.add(tag.getValueDescription());
                    lista.add(item);
                }
                inputStream3.close();
            }

            // Leer metadatos de la imagen utilizando metadata-extractor
            InputStream inputStream = contentResolver.openInputStream(imageUri);

            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                // Mostrar el botón si los valores de latitud y longitud GPS están disponibles
                if (checkGPSAvailability(metadata)) {
                    // Hacer visible el botón
                    btn.setVisibility(View.VISIBLE);
                } else {
                    // Ocultar el botón
                    btn.setVisibility(View.GONE);
                }

                inputStream.close();
            }

            // Leer metadatos de la imagen utilizando exifInterface
            ParcelFileDescriptor parcel_file = contentResolver.openFileDescriptor(imageUri, "r", null);
            FileDescriptor file = parcel_file.getFileDescriptor();

            if (file != null) {

                ExifInterface exif = null;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkExternalStoragePermission();
                        exif = new ExifInterface(file);
                    }
                }

                for (List<String> tag : lista) {
                    switch (tag.get(0).toString()) {
                        case "DateTime":
                            if (!exif.hasAttribute(ExifInterface.TAG_DATETIME)) {
                                exif.setAttribute(ExifInterface.TAG_DATETIME, tag.get(1).toString());
                            }
                            fecha.setText("Fecha: " + exif.getAttribute(ExifInterface.TAG_DATETIME));
                            break;
                        case "GPSLatitude":
                            if (!exif.hasAttribute(ExifInterface.TAG_GPS_LATITUDE)) {
                                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, tag.get(1).toString());
                            }
                            latitude.setText("Latitud: " + exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                            break;
                        case "GPSLongitude":
                            if (!exif.hasAttribute(ExifInterface.TAG_GPS_LONGITUDE)) {
                                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, tag.get(1).toString());
                            }
                            longitud.setText("Longitud: " + exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                            break;
                        case "Model":
                            if (!exif.hasAttribute(ExifInterface.TAG_MODEL)) {
                                exif.setAttribute(ExifInterface.TAG_MODEL, tag.get(1).toString());
                            }
                            model.setText("Modelo: " + exif.getAttribute(ExifInterface.TAG_MODEL));
                            break;
                        case "Make":
                            if (!exif.hasAttribute(ExifInterface.TAG_MAKE)) {
                                exif.setAttribute(ExifInterface.TAG_MAKE, tag.get(1).toString());
                            }
                            make.setText("Fabricante: " + exif.getAttribute(ExifInterface.TAG_MAKE));
                            break;
                        case "ImageWidth":
                            if (!exif.hasAttribute(ExifInterface.TAG_IMAGE_WIDTH)) {
                                exif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, tag.get(1).toString());
                            }
                            width.setText("Ancho: " + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
                            break;
                        case "ImageLength":
                            if (!exif.hasAttribute(ExifInterface.TAG_IMAGE_LENGTH)) {
                                exif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, tag.get(1).toString());
                            }
                            height.setText("Altura: " + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
                            break;
                        default:
                            break;
                    }

                }
                exif.saveAttributes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }

}
