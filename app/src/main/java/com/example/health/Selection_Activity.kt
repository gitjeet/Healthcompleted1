package com.example.health

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Selection_Activity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object{
        const val TAG = "MainActivity"
        const val ANONYMOUS= "anonymous"
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed $connectionResult")
        Toast.makeText(this,"Google Play Services Error", Toast.LENGTH_SHORT).show()
    }
    private var username :String? = null
    private var userPhotourl : String? = null

    private var fireBaseAuth : FirebaseAuth? =null
    private var fireBaseUser : FirebaseUser? = null

    private var googleApiClient : GoogleApiClient?=null
    private var googleSignInClient : GoogleSignInClient? = null
    private lateinit var btn_next :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selction_)


        btn_next=findViewById(R.id.btn_next)
        btn_next.setOnClickListener {
            startActivity(Intent(this@Selection_Activity,MainActivity::class.java))
        }
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build()

        username = MainActivity.ANONYMOUS
        fireBaseAuth= FirebaseAuth.getInstance()
        fireBaseUser=fireBaseAuth!!.currentUser

        if (fireBaseUser ==null){
            startActivity(Intent(this@Selection_Activity,SignInActivity::class.java))
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

        googleSignInClient = GoogleSignIn.getClient(this@Selection_Activity, gso)
    }
}