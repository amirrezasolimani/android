package com.example.p1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private Context context;
    private List<UserItem> userItems;

    public UserInfoAdapter(Context context, List<UserItem> userItems){
        this.context = context;
        this.userItems = userItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_info, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserItem item = userItems.get(position);
        holder.tvKey.setText(item.getKey());
        holder.tvValue.setText(item.getValue());

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(item.getKey().toLowerCase(), item.getValue());
            editor.apply();

            Toast.makeText(context, item.getKey() + " saved!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey, tvValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }

}
