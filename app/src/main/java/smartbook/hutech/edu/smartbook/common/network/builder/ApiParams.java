package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import java.util.HashMap;
import java.util.Map;

public class ApiParams {
    private Map<String, Object> mParams = new HashMap<>();

    public void add(String key, Object value) {
        mParams.put(key, value);
    }

    public Map<String, Object> getParams() {
        return mParams;
    }
}
