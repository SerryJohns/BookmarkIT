package com.andela.bookmarkit.ui.forecast;


import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.api.pojos.WeatherItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private List<WeatherItem> weatherItemList = new ArrayList<>();

    public HourlyForecastAdapter() {
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hourly_focus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.ViewHolder holder, int position) {
        holder.bind(weatherItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHour;
        private TextView txtTemperature;
        private ImageView imgIcon;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHour = (TextView) itemView.findViewById(R.id.txt_hour);
            txtTemperature = (TextView) itemView.findViewById(R.id.hourly_temperature);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_hourly_icon);
            view = itemView;
        }

        public void bind(WeatherItem weatherItem) {
            Resources res = itemView.getResources();

            txtTemperature.setText(String.format(res.getString(R.string.temperature_string), weatherItem.temperatureInCelsius()));
            txtHour.setText(weatherItem.getHours());
            Picasso.get().load(weatherItem.getIconURI()).into(imgIcon);
        }
    }

    public void updateData(List<WeatherItem> weatherItems) {
        weatherItemList.clear();
        weatherItemList.addAll(weatherItems);
        notifyDataSetChanged();
    }
}
