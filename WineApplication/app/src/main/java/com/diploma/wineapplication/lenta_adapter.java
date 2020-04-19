package com.diploma.wineapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class lenta_adapter extends RecyclerView.Adapter<lenta_adapter.lentaViewHolder>
implements Filterable {

    private List<LentaModel> list;
    private List<LentaModel> ListFiltered;

    public lenta_adapter(List<LentaModel> list) {
        this.list = list;
        this.ListFiltered = list;
    }

    @Override
    public lentaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new lentaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final lentaViewHolder holder, int position) {
        final LentaModel lenta = list.get(position);
        holder.Color.setText(lenta.Color);
        holder.Country.setText(lenta.Country);
        holder.Maker.setText(lenta.Maker);
        holder.Name.setText(lenta.Name);
        holder.Sort.setText(lenta.Sort);
        holder.Sweetness.setText(lenta.Sweetness);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WineActivity.class);
                intent.putExtra("color", lenta.Color);
                intent.putExtra("country", lenta.Country);
                intent.putExtra("description", lenta.Description);
                intent.putExtra("maker", lenta.Maker);
                intent.putExtra("name", lenta.Name);
                intent.putExtra("region", lenta.Region);
                intent.putExtra("sort", lenta.Sort);
                intent.putExtra("sweetness", lenta.Sweetness);
                intent.putExtra("year", lenta.Year);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                v.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(),0,0,"Посмотреть");
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ListFiltered = list;
                }
                else {
                    List<LentaModel> filteredList = new ArrayList<>();
                    for (LentaModel row : list) {

                        if (row.Name.toLowerCase().contains(charString.toLowerCase()) || row.Original_name.toUpperCase().contains(charString.toUpperCase())
                               /* || row.Color.contains(charSequence)
                                || row.Country.contains(charSequence)
                                || row.Sort.contains(charSequence)
                                || row.Color.contains(charSequence)*/) {
                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                filterResults.count = ListFiltered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<LentaModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return ListFiltered.size();
    }

    class lentaViewHolder extends RecyclerView.ViewHolder {
        TextView Color, Country, Description, Maker, Name, Region, Sort, Sweetness, Year;


        public lentaViewHolder (View itemView){
            super(itemView);
            Color = itemView.findViewById(R.id.textColor);
            Country = itemView.findViewById(R.id.textCountry);
            //Description = itemView.findViewById(R.id.textDescription);
            Maker = itemView.findViewById(R.id.textMaker);
            Name = itemView.findViewById(R.id.textName);
            //Region = itemView.findViewById(R.id.textRegion);
            Sort = itemView.findViewById(R.id.textSort);
            Sweetness = itemView.findViewById(R.id.textSweetness);
            //Year = itemView.findViewById(R.id.textYear);
        }
    }
}
