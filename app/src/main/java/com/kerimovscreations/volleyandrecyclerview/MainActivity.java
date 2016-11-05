package com.kerimovscreations.volleyandrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Post> posts;
    RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lookup the recyclerview in activity layout
        this.rvContacts = (RecyclerView) findViewById(R.id.rvPosts);

        posts = new ArrayList<Post>();


        String url = "https://jsonplaceholder.typicode.com/posts";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject item = (JSONObject) response
                                        .get(i);
                                Log.d("Volley item", item.toString());

                                String title = item.getString("title");
                                int id = item.getInt("id");
                                int userId = item.getInt("userId");
                                String body = item.getString("body");

                                posts.add(new Post(userId, id, title, body));
                            }
                            completeData(posts);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Err", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(req);
    }

    public void completeData(final ArrayList<Post> posts) {
        // Initialize contacts
        //posts = Post.createContactsList(20);
        // Create adapter passing in the sample user data
        PostsAdapter adapter = new PostsAdapter(this, posts);

        adapter.setOnItemClickListener(new PostsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = posts.get(position).getTitle();
                Toast.makeText(MainActivity.this, name + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        // Attach the adapter to the recyclerview to populate items
        this.rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        this.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
