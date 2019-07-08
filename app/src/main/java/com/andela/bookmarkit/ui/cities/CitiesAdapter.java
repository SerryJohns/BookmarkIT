package com.andela.bookmarkit.ui.cities;


import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.local.City;

import java.util.ArrayList;
import java.util.List;

import static com.andela.bookmarkit.ui.base.Utils.getRandomColor;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    private List<City> citiesList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public CitiesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_city_item, parent, false);
        return new CitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesAdapter.ViewHolder holder, int position) {
        holder.bind(citiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView cityName;
        private TextView cityDescription;
        private ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cityName = (TextView) itemView.findViewById(R.id.city_name);
            cityDescription = (TextView) itemView.findViewById(R.id.city_description);
            imgView = (ImageView) itemView.findViewById(R.id.city_icon);
            this.itemView = itemView;
        }

        public void bind(@NonNull final City city) {
            cityName.setText(city.getName());
            cityDescription.setText(city.getDescription());
            imgView.setColorFilter(ContextCompat.getColor(itemView.getContext(), getRandomColor()), PorterDuff.Mode.SRC_IN);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(city);
                }
            });
        }
    }

    public void updateData(List<City> cities) {
        citiesList.clear();
        citiesList.addAll(cities);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(City city);
    }
}
