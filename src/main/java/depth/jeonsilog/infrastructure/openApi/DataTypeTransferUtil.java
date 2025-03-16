package depth.jeonsilog.infrastructure.openApi;

import org.json.JSONObject;
import org.json.XML;

public class DataTypeTransferUtil {

    public static String xmlStrToJsonStr(String xml) {
        // XML To JSON To String
        JSONObject jsonObject = XML.toJSONObject(xml);
        return jsonObject.toString();
    }
}
