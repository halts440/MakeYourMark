package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity {

    EditText lEmail, lPassword;
    Button lSignIn, lSignUp;
    RequestQueue queue;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        lEmail = findViewById(R.id.lEmail);
        lPassword = findViewById(R.id.lPassword);
        lSignIn = findViewById(R.id.lSignIn);
        lSignUp = findViewById(R.id.lSignUp);
        RequestQueue queue = Volley.newRequestQueue(this);
        intent = new Intent();

        lSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postUrl = "http://"+
                        getResources().getString(R.string.my_ip_address)+
                        "/mym/CheckUserNamePassword.php";
                Log.d("Data", ""+postUrl);

                // save data that is to be send in an object
                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", ""+lEmail.getText().toString() );
                    postData.put("password", ""+lPassword.getText().toString() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Data", "I was here1");
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data", "I was here1");
                        // now go to main page
                        try {
                            // if user was found
                            Log.d("Data", "I was here4 "+response.getString("id") );
                            if( response.getString("id") == "1" ) {
                                Log.d("Data", "I was here2");
                                JSONObject data = response.getJSONObject("data");
                                try {

                                    String user_id = data.getString("user_id");
                                    String type = data.getString("type");
                                    if( type == "b" )
                                        intent = new Intent(SignIn.this, BuyerMainPage.class);
                                    else
                                        intent = new Intent(SignIn.this, SellerMainPage.class);
                                    intent.putExtra("user_id", user_id);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    Toast.makeText(SignIn.this, "Server issue while authenticating", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SignIn.this, "Server issue while authenticating", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignIn.this, "An issue occurred while authenticating", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("Data", "I was here3");
                queue.add(jsonObjectRequest);
                queue.start();
            }
        });

        lSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUpOptions.class);
                startActivity(intent);
            }
        });

    }
}