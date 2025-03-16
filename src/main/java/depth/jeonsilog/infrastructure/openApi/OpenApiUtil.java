package depth.jeonsilog.infrastructure.openApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenApiUtil {

    public static HttpURLConnection createHttpURLConnectionGet(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "application/json");
        return connection;
    }

    public static StringBuilder writeResponse(HttpURLConnection connection) throws IOException{
        BufferedReader br;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300)
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        return sb;
    }
}
