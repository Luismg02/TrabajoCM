package com.example.trabajocm.ui.borrado;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;
import com.example.trabajocm.databinding.FragmentBorradoBinding;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;

public class BorradoFragment extends Fragment {

    private FragmentBorradoBinding binding;

    private ImageView imageView;
    
    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BorradoViewModel galleryViewModel =
                new ViewModelProvider(this).get(BorradoViewModel.class);

        binding = FragmentBorradoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imagen_cargada_borrado);
        
        btn = root.findViewById(R.id.borrado_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    Uri imageUri = mainActivity.selectedImageUri;
                    if(imageUri!=null){
                        try{
                            // Obtener un ContentResolver para acceder a los datos del proveedor de contenido
                            ContentResolver contentResolver = requireActivity().getContentResolver();
                            // Leer metadatos de la imagen utilizando exifInterface
                            ParcelFileDescriptor parcel_file = contentResolver.openFileDescriptor(imageUri, "rw", null);
                            FileDescriptor file = parcel_file.getFileDescriptor();
                            if(file != null) {

                                ExifInterface exif = null;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        checkExternalStoragePermission();
                                        exif = new ExifInterface(file);
                                    }
                                }

                                exif.setAttribute(ExifInterface.TAG_DATETIME, "patata");
                                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null);
                                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null);
                                exif.setAttribute(ExifInterface.TAG_MAKE, null);
                                exif.setAttribute(ExifInterface.TAG_MODEL, null);
                                exif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, null);
                                exif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, null);
                                exif.saveAttributes();
                                parcel_file.close();
                            }

                                Toast.makeText(mainActivity, "Borrados todos los datos", Toast.LENGTH_SHORT).show();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(mainActivity, "Cargue una imagen antes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Uri myUri = mainActivity.selectedImageUri;
            if(myUri!=null){
                imageView.setImageURI(myUri);
            }else{
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
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


