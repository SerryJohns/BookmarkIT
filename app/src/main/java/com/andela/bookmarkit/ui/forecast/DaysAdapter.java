package com.andela.bookmarkit.ui.forecast;


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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.andela.bookmarkit.ui.base.Utils.getDayString;
import static com.andela.bookmarkit.ui.base.Utils.getRandomColor;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private List<Date> daysList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DaysAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_day_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.ViewHolder holder, int position) {
        holder.bind(daysList.get(position));
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDayIcon;
        private TextView txtDayTitle;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDayIcon = (ImageView) itemView.findViewById(R.id.img_day_icon);
            txtDayTitle = (TextView) itemView.findViewById(R.id.txt_day_title);
            view = itemView;
        }

        public void bind(final Date date) {
            String dayLabel = getDayString(date);
            txtDayTitle.setText(dayLabel);
            imgDayIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), getRandomColor()), PorterDuff.Mode.SRC_IN);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(date);
                }
            });
        }
    }

    public void updateData(List<Date> dates) {
        daysList.clear();
        daysList.addAll(dates);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Date date);
    }
}
