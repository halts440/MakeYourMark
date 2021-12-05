package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SellerSignup extends AppCompatActivity {

    EditText businessName, email, password, location;
    ImageView business_logo;
    Button create_btn;
    String postUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_signup);

        businessName = findViewById(R.id.business_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        location = findViewById(R.id.location);
        create_btn = findViewById(R.id.create_btn);
        business_logo = findViewById(R.id.business_logo);
        postUrl = "http://"+getString(R.string.my_ip_address)+"/mym/CheckEmailBName.php";

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( businessName.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty() || location.getText().toString().isEmpty() ) {
                    Toast.makeText(SellerSignup.this, "Fill out all fields", Toast.LENGTH_LONG).show();
                }
                else {
                    // check if there is any other account with same email address
                    RequestQueue requestQueue = Volley.newRequestQueue(SellerSignup.this );
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String response_id = object.getString("id");
                                if( response_id == "1" ) {
                                    Toast.makeText(SellerSignup.this, "Email or business name already used", Toast.LENGTH_LONG).show();
                                }
                                else if( response_id == "2" ) {
                                    // create account
                                }
                                else {
                                    // do nothing
                                }
                            } catch (JSONException e) {
                                Toast.makeText(SellerSignup.this, "Some issue occurred", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SellerSignup.this, "Some issue occurred", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        // set parameters for POST request
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("email",email.getText().toString() );
                            params.put("business_name", businessName.getText().toString() );
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}