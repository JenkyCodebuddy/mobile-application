package com.jenky.codebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jenky.codebuddy.R;
import com.jenky.codebuddy.models.Achievement;
import java.util.List;
import java.util.Locale;

public class AchievementAdapter extends ArrayAdapter {
    public AchievementAdapter(Context context, int resource, List<Achievement> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Achievement achievement = (Achievement) getItem(position);
        final ViewHolder viewHolder;
        View convertView = view;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.component_achievement, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.progress = (TextView) convertView.findViewById(R.id.progess);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(achievement.getName());
        viewHolder.description.setText(achievement.getDescription());
        viewHolder.progress.setText(String.format(Locale.getDefault(),"%.2f", achievement.getComplete_percentage())+"%");
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView description;
        TextView progress;
        ImageView image;
    }
}
