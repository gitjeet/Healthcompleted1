package com.example.health

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout


    companion object{
        const val TAG = "MainActivity"
        const val ANONYMOUS= "anonymous"
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed $connectionResult")
        Toast.makeText(this,"Google Play Services Error",Toast.LENGTH_SHORT).show()
    }
    private var username :String? = null
    private var userPhotourl : String? = null

    private var fireBaseAuth :FirebaseAuth? =null
    private var fireBaseUser : FirebaseUser? = null

    private var googleApiClient : GoogleApiClient?=null
    private var googleSignInClient : GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)




        //Builder Design Pattern
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build()

        username = ANONYMOUS
        fireBaseAuth= FirebaseAuth.getInstance()
        fireBaseUser=fireBaseAuth!!.currentUser

        if (fireBaseUser ==null){
            startActivity(Intent(this@MainActivity,SignInActivity::class.java))
            finish()
        }else{
            username = fireBaseUser!!.displayName
            if (fireBaseUser!!.photoUrl !=null){
                userPhotourl = fireBaseUser!!.photoUrl.toString()
            }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        val pagerAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){

            R.id.sign_out_item ->{
                fireBaseAuth!!.signOut()
                fireBaseAuth = null
                username = ANONYMOUS
                userPhotourl = null

                googleSignInClient!!.revokeAccess().addOnCompleteListener(this@MainActivity){
                    startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                    finish()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

}