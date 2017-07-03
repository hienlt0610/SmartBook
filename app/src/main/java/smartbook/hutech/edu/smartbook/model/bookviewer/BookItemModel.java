package smartbook.hutech.edu.smartbook.model.bookviewer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hienl on 7/2/2017.
 */

public class BookItemModel {
    @SerializedName("itemType")
    private String itemType;
    @SerializedName("resource")
    private String resource;
    @SerializedName("data")
    private List<Object> data = null;
    @SerializedName("correct")
    private List<String> correct = null;
    @SerializedName("coordinate")
    private CoordinateModel coordinate;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<Object> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public List<String> getCorrect() {
        return correct;
    }

    public void setCorrect(List<String> correct) {
        this.correct = correct;
    }

    public CoordinateModel getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateModel coordinate) {
        this.coordinate = coordinate;
    }
}
