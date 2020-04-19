package com.diploma.wineapplication;

import java.util.HashMap;
import java.util.Map;

public class RatingModel implements Comparable<RatingModel> {
    String Color, Country, Description, Maker, Name, Region, Sort, Sweetness, Year, user;
    float mark;

    public RatingModel(){  }

    public int compareTo(RatingModel other)
    {
        return Float.compare(mark, other.mark);
    }

    public RatingModel( String Color, String Country, String Description, String Maker, String Name, String Region, String Sort, String Sweetness, String Year, String user, float mark) {
        this.Color = Color;
        this.Country = Country;
        this.Description = Description;
        this.Maker = Maker;
        this.Name = Name;
        this.Region = Region;
        this.Sort = Sort;
        this.Sweetness = Sweetness;
        this.Year = Year;
        this.user = user;
        this.mark = mark;
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
        result.put("user", user);
        result.put("mark", mark);
        return result;
    }

}
