// Definición del nombre del paquete
package com.example.trabajocm;

//

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
//Imports para cargar archivos
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trabajocm.ui.gallery.GalleryFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//Imports para cargar archivos
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trabajocm.databinding.ActivityMainBinding;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //Variables Cargar Archivos
    private static final int PICK_IMAGE_REQUEST = 1;
    private FloatingActionButton fab;

    public Uri selectedImageUri;
    public String fecha_meta;
    public String latitud_meta;
    public String longitud_meta;
    public String model_meta;
    public String make_meta;
    public String width_meta;
    public String height_meta;

    public NavController navController;
    public NavHostFragment navHostFragment;

    // Bandera para indicar si se presionó el botón flotante
    private boolean isFabPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_borrado)
                .setOpenableLayout(drawer)
                .build();

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        fab = binding.appBarMain.fab;
        fab.setOnClickListener(new View.OnClickListener() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics del elemento de acción aquí.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
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

        // Dentro del método onActivityResult
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null && isFabPressed) {
            // Mostrar la imagen seleccionada en el ImageView
           selectedImageUri = data.getData();
           try {
                ContentResolver contentResolver = this.getContentResolver();
                // Leer metadatos de la imagen utilizando metadata-extractor
                InputStream inputStream = contentResolver.openInputStream(selectedImageUri);
                List<List<String>> lista = new ArrayList<>();
                if (inputStream != null) {
                    List<TiffField> listaAux = new ArrayList<>();

                    final ImageMetadata metadata = Imaging.getMetadata(inputStream, selectedImageUri.getPath());
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
                    inputStream.close();
                }

                for (List<String> tag : lista) {
                    switch (tag.get(0).toString()) {
                        case "DateTime":
                            fecha_meta = tag.get(1).toString();
                            break;
                        case "GPSLatitude":
                            latitud_meta = tag.get(1).toString();
                            break;
                        case "GPSLongitude":
                            longitud_meta = tag.get(1).toString();
                            break;
                        case "Model":
                            model_meta = tag.get(1).toString();
                            break;
                        case "Make":
                            make_meta = tag.get(1).toString();
                            break;
                        case "ImageWidth":
                            width_meta = tag.get(1).toString();
                            break;
                        case "ImageLength":
                            height_meta = tag.get(1).toString();
                            break;
                        default:
                            break;
                    }

                }

            }catch(Exception e) {
                e.printStackTrace();
            }
            // Navegar al fragmento GalleryFragment solo si el botón flotante fue presionado
            navController.navigate(R.id.nav_gallery, null);

            // Restablecer la bandera isFabPressed a false
            isFabPressed = false;
            Log.d("MainActivity", "Navigating to gallery. isFabPressed = " + isFabPressed);
        }



    }

    public void setFechaMeta(String nuevaFecha) {
        fecha_meta = nuevaFecha;
    }

    public void setLatitudMeta(String nuevaLatitud) {
        latitud_meta = nuevaLatitud;
    }
    public void setLongitudMeta(String nuevaLongitud) {
        longitud_meta = nuevaLongitud;
    }
    public void setModelMeta(String nuevoModel) {
        model_meta = nuevoModel;
    }public void setMakeMeta(String nuevoMake) {
        make_meta = nuevoMake;
    }public void setWidthMeta(String nuevaWidth) {
        width_meta = nuevaWidth;
    }
    public void setHeightMeta(String nuevoHeight) {
        height_meta = nuevoHeight;
    }



}
