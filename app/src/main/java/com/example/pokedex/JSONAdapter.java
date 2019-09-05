package com.example.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_list, null);
            holder = new ViewHolder();

            holder.pokemonTextView = convertView.findViewById(R.id.pokemonName);
            holder.pokemonImageView = convertView.findViewById(R.id.pokemonImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);

        String pokemonName = "";

        if (jsonObject.has("name")) {
            pokemonName = jsonObject.optString("name");
        }

        holder.pokemonTextView.setText(pokemonName);

        if (jsonObject.has("url")) {
            String imgUrl = getImgUrl(jsonObject.optString("url"));
            Picasso.with(mContext).load(imgUrl).into(holder.pokemonImageView);
        }

        return convertView;
    }

    public void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    private String getImgUrl(String url) {
        String[] parts = url.split("pokemon/");
        String imgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
        imgUrl +=  parts[1].replace("/", "") + ".png";
        return imgUrl;
    }

    private static class ViewHolder {
        public TextView pokemonTextView;
        public ImageView pokemonImageView;
    }

}
