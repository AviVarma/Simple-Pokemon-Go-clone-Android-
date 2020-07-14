package com.example.findthelyrics2

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.collections.ArrayList
import kotlin.random.Random

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var collectedLyrics = ArrayList<String>()
    private val markerNumber = 6
    private lateinit var fileName: String


    /**
     * Permissions object, open a new tab to ask for location permissions
     */
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    /**
     * Initialise the activity with the google maps fragment attached to the parent activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialise current location provider.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Add an options menu to the maps toolbar with designed buttons
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    /**
     * Add functionality to the buttons that have been loaded by the menu layout xml.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_guess -> {
                guessIntent()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Check if a lyric has been collected before allowing the change of activity by intent.
     */
    private fun guessIntent(){
        if(collectedLyrics.size > 0){
            val guessIntent = Intent(applicationContext, GuessActivity::class.java)
            guessIntent.putExtra("lyricsCollected", collectedLyrics)
            guessIntent.putExtra("markerNumber", markerNumber)
            guessIntent.putExtra("fileName", fileName)
            startActivity(guessIntent)
        } else{
            Snackbar.make(findViewById(R.id.map),getString(R.string.display_text_collect_first),
                Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Swansea, Wales.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Use current location to collect the markers placed on the map.
     * Compare the distance between the current location and the marker before allowing user to
     * pick up the marker by removing the marker.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()
        mMap.isMyLocationEnabled = true

        // default current location to prevent null exceptions
        val target = Location("lastLocation")
        target.longitude = -3.879181
        target.latitude = 51.619332

        lastLocation = target
        val minDistance = 20.0

        // display current location
        FusedLocationProviderClient(applicationContext).lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16F))
            }
        }

        // check game mode and populate the markers with the correct lyrics
        var markers: ArrayList<Marker>
        val state = intent.extras!!.getBoolean("state")
        when (state) {
            true -> markers = makeMarkers(mMap, getString(R.string.path_classic), markerNumber)
            false -> markers = makeMarkers(mMap, getString(R.string.path_current), markerNumber)
        }


        // compare the current location with each marker to see if marker can be picked up.
        mMap.setOnMarkerClickListener {
            FusedLocationProviderClient(applicationContext).lastLocation.addOnSuccessListener(this) { location ->
                markers.forEach {
                    val target = Location("target")
                    target.longitude = it.position.longitude
                    target.latitude = it.position.latitude

                    // Got last known location. In some rare situations this can be null.
                    if (location == null) {
                        lastLocation = location
                    }
                    if (location.distanceTo(target) <= minDistance) {
                        collectedLyrics.add(it.snippet)
                        it.remove()
                        Snackbar.make(
                            findViewById(R.id.map), getString(
                                R.string.response_picked_up_lyric
                            ), Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            true
        }

    }

    /**
     * Ask user for location permissions to set up the google maps for current location usage.
     */
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        when(isLocationEnabled()){
            false -> Snackbar.make(findViewById(R.id.map),
                getString(R.string.response_turn_on_location), Snackbar.LENGTH_LONG).show()
        }
    }


    /**
     * Load the file and append each line of the file into an array list
     */
    private fun loadLyrics(stream: InputStream): ArrayList<String>{
        val lyrics = ArrayList<String>()

        try { // open the file for reading
            val inStream: InputStream = stream
            // if file the available for reading
            if (inStream != null) { // prepare the file for reading
                val inputReader = InputStreamReader(inStream)
                val bufferReader = BufferedReader(inputReader)
                var line: String
                // read every line of the file into the line-variable, on line at the time
                do {
                    line = bufferReader.readLine()
                    lyrics.add(line)
                } while (line != null)
            }
        } catch (ex: Exception) { // print stack trace.
        } finally { // close the file.
            stream.close()
        }
        return lyrics
    }

    /**
     * From the file converted into am ArrayList, return the random line.
     */
    private fun randomLyric(lyrics: ArrayList<String>): String{
        val randomLyricIndex = Random.nextInt(0, lyrics.size)
        return lyrics[randomLyricIndex]
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    /**
     * List file names in the directory and list the songs. Take a random song from the list and
     * place random lines (lyrics) from the song into each marker's snippet.
     * return: markers
     */
    private fun makeMarkers(mMap: GoogleMap, path: String, markerNum: Int): ArrayList<Marker>{
        val songsList: List<String>? = assets.list(path)?.asList()
        val markers = ArrayList<Marker>()

        val randomNum = Random.nextInt(0, songsList!!.size)
        fileName = songsList[randomNum]

        Log.i("FILE NAME",fileName)

        val file = assets.open(path +"/"+ songsList[randomNum])
        val lyrics = loadLyrics(file)

        // make the markers
        for(x in 1..markerNum){
            val latitudeBounds = Random.nextDouble(51.6182604, 51.6194262)
            val longitudeBounds = Random.nextDouble(-3.8823517, -3.8794275)
            val latLong = LatLng(latitudeBounds,longitudeBounds)

            val marker: Marker =  mMap.addMarker(MarkerOptions().position(latLong)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_music_note_black_24dp)))
            marker.snippet = randomLyric(lyrics)

            markers.add(marker)
        }

        return markers
    }
}
