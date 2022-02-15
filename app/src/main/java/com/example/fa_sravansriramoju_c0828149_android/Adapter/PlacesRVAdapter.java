package com.example.fa_sravansriramoju_c0828149_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fa_sravansriramoju_c0828149_android.MainActivity;
import com.example.fa_sravansriramoju_c0828149_android.Model.Place;
import com.example.fa_sravansriramoju_c0828149_android.PlaceDetailsActivity;
import com.example.fa_sravansriramoju_c0828149_android.R;
import com.example.fa_sravansriramoju_c0828149_android.utilities.DBHelper;

import org.w3c.dom.Text;

import java.util.List;

public class PlacesRVAdapter extends RecyclerView.Adapter<PlacesRVAdapter.PlaceViewHolder> {

    private List<Place> placesList;
    private MainActivity activity;
    private DBHelper myDB;

    public PlacesRVAdapter(MainActivity activity, DBHelper myDB) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card , parent , false);
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final Place item = placesList.get(position);
        holder.mCheckbox.setChecked(toBoolean(item.isVisited()));
        holder.placeName.setText(item.getTitle());
        holder.address.setText(item.getAddress());
        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myDB.updateVisited(item.getId(), 1);
                }else
                    myDB.updateVisited(item.getId(), 0);
            }
        });
        holder.placeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlaceDetailsActivity.class);
                intent.putExtra("place", item);
                getContext().startActivity(intent);
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public Context getContext(){
        return activity;
    }

    public void setPlaces(List<Place> mList){
        this.placesList = mList;
        notifyDataSetChanged();
    }

    public void deletePlace(int position){
        Place item = placesList.get(position);
        myDB.deletePlace(item.getId());
        placesList.remove(position);
        notifyItemRemoved(position);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckbox;
        TextView placeName;
        TextView address;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckbox = itemView.findViewById(R.id.mcheckbox);
            placeName = itemView.findViewById(R.id.placename);
            address = itemView.findViewById(R.id.addressincard);
        }
    }
}
