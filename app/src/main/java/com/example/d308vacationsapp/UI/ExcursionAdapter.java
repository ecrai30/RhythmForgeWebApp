package com.example.d308vacationsapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationsapp.R;
import com.example.d308vacationsapp.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private static List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {

    private final TextView excursionItemView;
    private final TextView excursionItemView2;

    private ExcursionViewHolder(View itemView) {
        super(itemView);
        excursionItemView = itemView.findViewById(R.id.textView3);
        excursionItemView2 = itemView.findViewById(R.id.textView4);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                final Excursion current = mExcursions.get(position);
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("id", current.getExcursionId());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("price", current.getPrice());
                intent.putExtra("vacID", current.getVacationId());
            }
        });

    }
}
    public ExcursionAdapter(Context context){
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.excursion_list_item,parent,false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if(mExcursions!=null){
            Excursion current=mExcursions.get(position);
            String name= current.getExcursionName();
            int vacID = current.getVacationId();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(vacID));

        }
        else{
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView.setText("No vacation id");


        }

    }

    public void setExcursions(List<Excursion> excursions){
        mExcursions=excursions;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if(mExcursions!=null) {
            return mExcursions.size();
        }
        else{
            return 0;
        }
    }

}