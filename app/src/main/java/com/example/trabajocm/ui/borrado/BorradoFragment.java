package com.example.trabajocm.ui.borrado;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajocm.MainActivity;
import com.example.trabajocm.R;
import com.example.trabajocm.databinding.FragmentBorradoBinding;

public class BorradoFragment extends Fragment {

    private FragmentBorradoBinding binding;

    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BorradoViewModel galleryViewModel =
                new ViewModelProvider(this).get(BorradoViewModel.class);

        binding = FragmentBorradoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = root.findViewById(R.id.imagen_cargada_borrado);

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
