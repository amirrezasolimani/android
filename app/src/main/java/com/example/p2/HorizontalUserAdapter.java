package com.example.p2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class HorizontalUserAdapter extends RecyclerView.Adapter<HorizontalUserAdapter.Holder> {

    private List<karbar> items;
    private OnUserClickListener listener;

    public HorizontalUserAdapter(List<karbar> items, OnUserClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName;

        Holder(View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvName = itemView.findViewById(R.id.tvName);
        }

        void bind(final User user) {
            // Simple initials for avatar
            String[] parts = user.getName().split(" ");
            String initials = "";
            for (String part : parts) {
                if (!part.isEmpty()) initials += part.charAt(0);
            }
            if (initials.length() > 2) initials = initials.substring(0, 2);

            tvAvatar.setText(initials.toUpperCase());
            tvName.setText(user.getName());

            itemView.setOnClickListener(v -> listener.onUserClicked(user));
        }
    }
}
