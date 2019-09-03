package com.example.pokedex;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private static final String QUERY_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String QUERY_SPRITE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private TextView pokemonTextView;

    private TextView heightTextView;
    private TextView weightTextView;
    private TextView ability1TextView;
    private TextView ability2TextView;
    private TextView ability3TextView;

    private Button btnType1;
    private Button btnType2;

    private String id = "";

    Map<String, String> mapType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        makeMapType();

        ImageView pokemonImage = findViewById(R.id.pokemonImage);

        pokemonTextView = findViewById(R.id.pokemonName);
        heightTextView = findViewById(R.id.heightTextView);
        weightTextView = findViewById(R.id.weightTextView);
        ability1TextView = findViewById(R.id.ability1);
        ability2TextView = findViewById(R.id.ability2);
        ability3TextView = findViewById(R.id.ability3);

        btnType1 = findViewById(R.id.type1);
        btnType2 = findViewById(R.id.type2);

        id = this.getIntent().getExtras().getString("id");
        Picasso.with(this).load(QUERY_SPRITE_URL + id + ".png").into(pokemonImage);

        loadData();
    }

    private void makeMapType() {
        mapType = new HashMap<String, String>();

        mapType.put("bug", "#A8B820");
        mapType.put("dragon", "#7038F8");
        mapType.put("ice", "#98D8D8");
        mapType.put("fire", "#F08030");
        mapType.put("water", "#6890F0");
        mapType.put("grass", "#78C850");
        mapType.put("fighting", "#C03028");
        mapType.put("flying", "#A890F0");
        mapType.put("ghost", "#705898");
        mapType.put("ground", "#E0C068");
        mapType.put("rock", "#B8A038");
        mapType.put("psychic", "#F85888");
        mapType.put("poison", "#A040A0");
        mapType.put("normal", "#A8A878");
        mapType.put("electric", "#F8D030");
    }

    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(QUERY_URL + id,
            new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject jsonObject) {
                    //Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                    //Log.d("omg android", jsonObject.toString());
                    loadContent(jsonObject);

                }


                @Override
                public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                    Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("omg android", statusCode + " " + throwable.getMessage());
                }

            });
    }

    private void loadContent(JSONObject jsonObject) {
        try {

            pokemonTextView.setText(jsonObject.getString("name"));
            heightTextView.setText(jsonObject.getString("height"));
            weightTextView.setText(jsonObject.getString("weight"));

            JSONArray teste = jsonObject.getJSONArray("abilities");

            int max = teste.length();

            JSONObject ability1 = teste.getJSONObject(0).getJSONObject("ability");
            ability1TextView.setText(ability1.getString("name"));

            if (max == 2) {
                JSONObject ability2 = teste.getJSONObject(1).getJSONObject("ability");
                ability2TextView.setText(ability2.getString("name"));
                ability3TextView.setVisibility(View.GONE);
            } else if (max == 3) {
                JSONObject ability2 = teste.getJSONObject(1).getJSONObject("ability");
                ability2TextView.setText(ability2.getString("name"));

                JSONObject ability3 = teste.getJSONObject(2).getJSONObject("ability");
                ability3TextView.setText(ability3.getString("name"));
            } else{
                ability2TextView.setVisibility(View.GONE);
                ability3TextView.setVisibility(View.GONE);
            }

            JSONArray types = jsonObject.getJSONArray("types");

            max = types.length();

            JSONObject firstType = types.getJSONObject(0).getJSONObject("type");
            String type1 = firstType.getString("name");
            btnType1.setText(type1);
            btnType1.setBackgroundColor(Color.parseColor(mapType.get(type1)));

            if (max > 1) {
                JSONObject secondType = types.getJSONObject(1).getJSONObject("type");
                String type2 = secondType.getString("name");
                btnType2.setText(type2);
                btnType2.setBackgroundColor(Color.parseColor(mapType.get(type2)));
            } else {
                btnType2.setVisibility(View.GONE);
            }

        } catch(Exception e) {
            Log.e("omg Android", e.getMessage());
        }

    }

}
