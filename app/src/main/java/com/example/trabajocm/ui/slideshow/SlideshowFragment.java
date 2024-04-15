package com.example.trabajocm.ui.slideshow;

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;
import com.example.trabajocm.databinding.FragmentSlideshowBinding;

import java.io.InputStream;


public class SlideshowFragment extends Fragment {

    private ImageView imageView;
    private FragmentSlideshowBinding binding;

    private EditText fecha;
    private EditText latitude;
    private EditText longitud;
    private EditText model;
    private EditText make;
    private EditText width;
    private EditText height;
    private Button btn;

    private LinearLayout data_display;


    public void onResume(){
        super.onResume();

        fecha = getView().findViewById(R.id.edit_date);
        latitude = getView().findViewById(R.id.edit_latitud);
        longitud = getView().findViewById(R.id.edit_longitud);
        model = getView().findViewById(R.id.edit_model);
        make = getView().findViewById(R.id.edit_make);
        width = getView().findViewById(R.id.edit_width);
        height = getView().findViewById(R.id.edit_height);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            String fecha_meta = mainActivity.fecha_meta;
            String latitud_meta = mainActivity.latitud_meta;
            String longitud_meta = mainActivity.longitud_meta;
            String model_meta = mainActivity.model_meta;
            String make_meta = mainActivity.make_meta;
            String width_meta = mainActivity.width_meta;
            String height_meta = mainActivity.height_meta;
            fecha.setText(fecha_meta);
            latitude.setText(latitud_meta);
            longitud.setText(longitud_meta);
            model.setText(model_meta);
            make.setText(make_meta);
            width.setText(width_meta);
            height.setText(height_meta);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imagen_cargada_edit);
        fecha = root.findViewById(R.id.edit_date);
        latitude = root.findViewById(R.id.edit_latitud);
        longitud = root.findViewById(R.id.edit_longitud);
        model = root.findViewById(R.id.edit_model);
        make = root.findViewById(R.id.edit_make);
        width = root.findViewById(R.id.edit_width);
        height = root.findViewById(R.id.edit_height);
        data_display = root.findViewById(R.id.data_display_edit);
        btn = root.findViewById(R.id.editar_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    Uri myUri = mainActivity.selectedImageUri;
                    if(myUri!=null){
                        mainActivity.setFechaMeta(fecha.getText().toString());
                        mainActivity.setLatitudMeta(latitude.getText().toString());
                        mainActivity.setLongitudMeta(longitud.getText().toString());
                        mainActivity.setMakeMeta(make.getText().toString());
                        mainActivity.setModelMeta(model.getText().toString());
                        mainActivity.setWidthMeta(width.getText().toString());
                        mainActivity.setHeightMeta(height.getText().toString());
                        Toast.makeText(mainActivity, "Editados todos los metadatos", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mainActivity, "Cargue una imagen antes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri imageUri = mainActivity.selectedImageUri;
            if(imageUri!=null){
                imageView.setImageURI(imageUri);
                String fecha_meta = mainActivity.fecha_meta;
                String latitud_meta = mainActivity.latitud_meta;
                String longitud_meta = mainActivity.longitud_meta;
                String model_meta = mainActivity.model_meta;
                String make_meta = mainActivity.make_meta;
                String width_meta = mainActivity.width_meta;
                String height_meta = mainActivity.height_meta;
                fecha.setText(fecha_meta);
                latitude.setText(latitud_meta);
                longitud.setText(longitud_meta);
                model.setText(model_meta);
                make.setText(make_meta);
                width.setText(width_meta);
                height.setText(height_meta);
                //displayMetadata(imageUri);
            }else{
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
            }

        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void displayMetadata(Uri imageUri) {
        try {
            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            ContentResolver contentResolver = requireActivity().getContentResolver();

            // Leer metadatos de la imagen utilizando exifInterface
            InputStream inputStream2 = contentResolver.openInputStream(imageUri);

            if(inputStream2 != null){

                ExifInterface exif = null;

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    exif = new ExifInterface(inputStream2);
                }

                fecha.setText(exif.getAttribute(ExifInterface.TAG_DATETIME));
                latitude.setText(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                longitud.setText(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                model.setText(exif.getAttribute(ExifInterface.TAG_MODEL));
                make.setText(exif.getAttribute(ExifInterface.TAG_MAKE));
                width.setText( exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
                height.setText( exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}