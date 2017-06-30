package smartbook.hutech.edu.smartbook.common.network.connect;
/*
 * Created by Nhat Hoang on 30/06/2017.
 */

public class ApiConnectConfig {
    public static ApiConnectDetail createConnectionDetail() {
        ApiConnectDetail connection = new ApiConnectDetail();
        connection.setBaseURL("http://hienlt0610.ddns.net:2212/");
        return connection;
    }
}
