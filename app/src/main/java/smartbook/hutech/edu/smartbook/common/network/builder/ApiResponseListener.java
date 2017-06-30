package smartbook.hutech.edu.smartbook.common.network.builder;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import smartbook.hutech.edu.smartbook.common.BaseModel;

public interface ApiResponseListener {
    void onDataResponse(int nCode, BaseModel nData);
}
