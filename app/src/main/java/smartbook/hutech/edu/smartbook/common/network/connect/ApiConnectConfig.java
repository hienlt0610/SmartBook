package smartbook.hutech.edu.smartbook.common.network.connect;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

import smartbook.hutech.edu.smartbook.common.Constant;

public class ApiConnectConfig {
    public static ApiConnectDetail createConnectionDetail() {
        ApiConnectDetail connection = new ApiConnectDetail();
        connection.setBaseURL(Constant.BASE_API_URL);
        return connection;
    }
}
