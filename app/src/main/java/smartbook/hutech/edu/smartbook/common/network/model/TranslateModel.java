package smartbook.hutech.edu.smartbook.common.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hienl on 7/8/2017.
 */

public class TranslateModel {
    @SerializedName("code")
    private int code;
    @SerializedName("lang")
    private String lang;
    @SerializedName("text")
    private List<String> text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
