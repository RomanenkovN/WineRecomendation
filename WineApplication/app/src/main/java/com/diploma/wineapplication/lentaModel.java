package com.diploma.wineapplication;

import java.util.HashMap;
import java.util.Map;

public class LentaModel {

    String Color, Country, Description, Maker, Name, Region, Sort, Sweetness, Year, Original_name;

    public LentaModel(){  }

    public LentaModel( String Color, String Country, String Description, String Maker, String Name, String Region, String Sort, String Sweetness, String Year, String Original_name) {
        this.Color = Color;
        this.Country = Country;
        this.Description = Description;
        this.Maker = Maker;
        this.Name = Name;
        this.Region = Region;
        this.Sort = Sort;
        this.Sweetness = Sweetness;
        this.Year = Year;
        this.Original_name = Original_name;
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("Color", Color);
        result.put("Country", Country);
        result.put("Description", Description);
        result.put("Maker", Maker);
        result.put("Name", Name);
        result.put("Region", Region);
        result.put("Sort", Sort);
        result.put("Sweetness", Sweetness);
        result.put("Year", Year);
        result.put("Original_name", Original_name);
        return result;
    }
}
