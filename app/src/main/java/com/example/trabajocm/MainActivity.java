// Definición del nombre del paquete
package com.example.trabajocm;

//
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
//Imports para cargar archivos
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;

import com.example.trabajocm.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//Imports para cargar archivos
//hacer algo con la imagen
import android.widget.ImageView; // Añade esta importación
import android.widget.Toast;
//hacer algo imagen
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trabajocm.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    //Variables Cargar Archivos
    private static final int PICK_IMAGE_REQUEST = 1;
    private FloatingActionButton fab;

    private boolean isFabPressed = false; // Bandera para indicar si se presionó el botón flotante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_borrado)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Establecer la bandera como verdadera cuando se presiona el botón flotante
                isFabPressed = true;
                Log.d("MainActivity", "Floating action button pressed. isFabPressed = " + isFabPressed);
                openFileChooser();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //funcionalidad cargar archivos en el botón UPLOAD
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null && isFabPressed) {
            // Mostrar la imagen seleccionada en el ImageView
            Uri selectedImageUri = data.getData();

            // Crear un Bundle para pasar la URI de la imagen al fragmento
            Bundle args = new Bundle();
            args.putString("imageUri", selectedImageUri.toString());

            // Obtener el NavController
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

            // Navegar al fragmento GalleryFragment solo si el botón flotante fue presionado
            navController.navigate(R.id.nav_gallery, args);

            // Restablecer la bandera isFabPressed a false
            isFabPressed = false;
            Log.d("MainActivity", "Navigating to gallery. isFabPressed = " + isFabPressed);
        }
    }



}