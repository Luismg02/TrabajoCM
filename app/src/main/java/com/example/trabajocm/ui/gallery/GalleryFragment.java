package com.example.trabajocm.ui.gallery;

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
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

import androidx.fragment.app.Fragment;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;


public class GalleryFragment extends Fragment {

    private ImageView imageView;
    private TextView fecha;
    private TextView latitude;
    private TextView longitud;
    private TextView model;
    private TextView make;
    private TextView width;
    private TextView height;

    private Button btn;

    private LinearLayout data_display;

    public GalleryFragment() {
        // Required empty public constructor
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

        // Obtenemos la imagen si se ha cargado
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri imageUri = mainActivity.selectedImageUri;
            if(imageUri!=null){
                data_display.setVisibility(View.VISIBLE);
                imageView.setImageURI(imageUri);
                loadAndDisplayMetadata(imageUri);
            }else{
                data_display.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
            }

        }


        return view;
    }

    String getMiEtiqueta(String etiqueta,  Metadata metadata)
    {
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
            ContentResolver contentResolver3 = requireActivity().getContentResolver();
            // Leer metadatos de la imagen utilizando metadata-extractor
            InputStream inputStream3 = contentResolver3.openInputStream(imageUri);
            List<org.apache.commons.imaging.formats.tiff.TiffField> listaAux = new ArrayList<>();
            List<List<String>> lista = new ArrayList<>();
            final ImageMetadata metadata1 = Imaging.getMetadata(inputStream3, imageUri.getPath());
            if (metadata1 instanceof JpegImageMetadata) {
                final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata1;
                final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                if (null != exifMetadata) {
                    listaAux = exifMetadata.getAllFields();
                }
            }
            for (org.apache.commons.imaging.formats.tiff.TiffField tag: listaAux){
                ArrayList<String> item= new ArrayList<>();
                item.add(tag.getTagName());
                item.add(tag.getValueDescription());
                lista.add(item);
            }
            inputStream3.close();

            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            ContentResolver contentResolver = requireActivity().getContentResolver();
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
            InputStream inputStream2 = contentResolver.openInputStream(imageUri);

            if(inputStream2 != null){

                ExifInterface exif = null;

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    exif = new ExifInterface(inputStream2);
                }

                for(List<String> tag : lista){
                    switch(tag.get(0).toString()){
                        case "DateTime":
                            if(!exif.hasAttribute(ExifInterface.TAG_DATETIME)){
                                exif.setAttribute(ExifInterface.TAG_DATETIME, tag.get(1).toString());
                            }
                            fecha.setText("Fecha: " + exif.getAttribute(ExifInterface.TAG_DATETIME));
                            break;
                        case "GPSLatitude":
                            if(!exif.hasAttribute(ExifInterface.TAG_GPS_LATITUDE)){
                                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, tag.get(1).toString());
                            }
                            latitude.setText("Latitud: " +  exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                            break;
                        case "GPSLongitude":
                            if(!exif.hasAttribute(ExifInterface.TAG_GPS_LONGITUDE)){
                                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, tag.get(1).toString());
                            }
                            longitud.setText("Longitud: " +  exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                            break;
                        case "Model":
                            if(!exif.hasAttribute(ExifInterface.TAG_MODEL)){
                                exif.setAttribute(ExifInterface.TAG_MODEL, tag.get(1).toString());
                            }
                            model.setText("Modelo: " +  exif.getAttribute(ExifInterface.TAG_MODEL));
                            break;
                        case "Make":
                            if(!exif.hasAttribute(ExifInterface.TAG_MAKE)){
                                exif.setAttribute(ExifInterface.TAG_MAKE, tag.get(1).toString());
                            }
                            make.setText("Fabricante: " +  exif.getAttribute(ExifInterface.TAG_MAKE));
                            break;
                        case "ImageWidth":
                            if(!exif.hasAttribute(ExifInterface.TAG_IMAGE_WIDTH)){
                                exif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, tag.get(1).toString());
                            }
                            width.setText("Ancho: " +  exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
                            break;
                        case "ImageLength":
                            if(!exif.hasAttribute(ExifInterface.TAG_IMAGE_LENGTH)){
                                exif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, tag.get(1).toString());
                            }
                            height.setText("Altura: " +  exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
                            break;
                        default:
                            break;
                    }

                }
                exif.saveAttributes();
                inputStream2.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
