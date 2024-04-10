package com.example.trabajocm.ui.slideshow;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imagen_cargada_edit);


        btn = root.findViewById(R.id.editar_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    Uri myUri = mainActivity.selectedImageUri;
                    if(myUri!=null){
                        Toast.makeText(mainActivity, "Editados todos los metadatos(fake)", Toast.LENGTH_SHORT).show();
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
                String imageUriString = myUri.toString();
                displayImage(myUri, imageUriString);
            }else{
                imageView.setImageResource(R.drawable.imagen_sin_cargar);
            }

        }

        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void displayImage(Uri imageUri, String tmp) {
        try {
            // Cargar imagen en el ImageView
            imageView.setImageURI(imageUri);
            // Obtener el ContentResolver para acceder a los datos del proveedor de contenido
            //ContentResolver contentResolver = requireActivity().getContentResolver();

            // Leer metadatos de la imagen utilizando metadata-extractor
            //InputStream inputStream = contentResolver.openInputStream(imageUri);
            //if (inputStream != null) {
                //Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                // Construir una cadena con los metadatos
                //StringBuilder metadataString = new StringBuilder("METADATOS::\n\n");

                //for (Directory directory : metadata.getDirectories()) {
                    //for (Tag tag : directory.getTags()) {
                        //Log.d("asd", tag.getTagName());
                        //metadataString.append(tag.getTagName()).append(": ").append(tag.getDescription()).append("\n");

                    //}
                //}



                // Mostrar metadatos en TextView
                //metadataTextView.setText(metadataString.toString());
                //inputStream.close();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}