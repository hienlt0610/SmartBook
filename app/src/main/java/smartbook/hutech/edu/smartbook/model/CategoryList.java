package smartbook.hutech.edu.smartbook.model;
/*
 * Created by Nhat Hoang on 01/07/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartbook.hutech.edu.smartbook.common.BaseModel;

public class CategoryList extends BaseModel {
    @SerializedName("categories")
    List<Category> list = new ArrayList<>();

    public List<Category> getList() {
        return list;
    }
}
