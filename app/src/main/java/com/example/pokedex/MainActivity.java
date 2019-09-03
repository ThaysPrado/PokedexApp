package com.example.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String QUERY_URL = "https://pokeapi.co/api/v2/pokemon";

    private ListView mainListView = null;

    JSONAdapter mJSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListView = findViewById(R.id.mainListView);

        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject =  mJSONAdapter.getItem(position);

                Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);

                String[] parts = jsonObject.optString("url").split("pokemon/");
                detailIntent.putExtra("id", parts[1].replace("/", ""));

                startActivity(detailIntent);

            }
        });

        mainListView.setAdapter(mJSONAdapter);

        loadData();

    }


    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(QUERY_URL + "?offset=0&limit=151",
            new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject jsonObject) {
                    //Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                    //Log.d("omg android", jsonObject.toString());

                    mJSONAdapter.updateData(jsonObject.optJSONArray("results"));

                }


                @Override
                public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                    Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("omg android", statusCode + " " + throwable.getMessage());
                }

            });


    }


}
