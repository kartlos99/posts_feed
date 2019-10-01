package com.example.postsfeedapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.postsfeedapp.adapters.MyAdapter;
import com.example.postsfeedapp.models.PostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainPage";
    private static final String URL = "http://jsonstub.com/feed";
    private Context context;
    private RecyclerView recyclerView;
    private AppCompatButton btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        btn = findViewById(R.id.btn_reload);

        chackstate();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chackstate();
            }
        });
    }

    private void chackstate(){
        if (isNetworkAvailable()) {
            sendRequest();
            btn.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(context, "Check For Internet Connection!", Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.VISIBLE);
        }
    }

    private void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest postsRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // respone OK
                Log.d(TAG, "resp.received");
                ArrayList<PostModel> posts = new ArrayList<>();

                try {
                    JSONArray dataArray = response.getJSONArray("posts");

                    for (int i = 0; i < dataArray.length(); i++) {
                        PostModel postModel = new PostModel(
                                dataArray.getJSONObject(i).getInt("id"),
                                dataArray.getJSONObject(i).getString("name"),
                                dataArray.getJSONObject(i).getString("message"),
                                dataArray.getJSONObject(i).getString("photoUrl")
                        );
                        posts.add(postModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showPosts(getUniquePosts(posts));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Toast.makeText(context, error.getMessage() + " -", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "resp.ERROR: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> myHeaders = new HashMap<>();
                myHeaders.put("JsonStub-User-Key", "a6504fb9-780f-483e-9021-19135c3cfc97");
                myHeaders.put("JsonStub-Project-Key", "58df32c7-1d41-4932-8f00-d2d4a61ae791");
                myHeaders.put("Content-Type", "application/json");
                return myHeaders;
            }

        };

        queue.add(postsRequest);
    }

    private void showPosts(ArrayList<PostModel> data) {
        MyAdapter adapter = new MyAdapter(context, data);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<PostModel> getUniquePosts(ArrayList<PostModel> data) {
        ArrayList<PostModel> filtered = new ArrayList<>();

        Map<String, PostModel> filterMap = new HashMap<>();
        for (PostModel model : data) {
            filterMap.put(String.valueOf(model.getId()), model);
        }

        for (String key : filterMap.keySet()) {
            filtered.add(filterMap.get(key));
        }

        return filtered;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
