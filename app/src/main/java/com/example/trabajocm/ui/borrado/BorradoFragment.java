package com.example.trabajocm.ui.borrado;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;
import com.example.trabajocm.databinding.FragmentBorradoBinding;

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
                    Uri myUri = mainActivity.selectedImageUri;
                    if(myUri!=null){
                        Toast.makeText(mainActivity, "Borrados todos los datos(fake)", Toast.LENGTH_SHORT).show();
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

}


