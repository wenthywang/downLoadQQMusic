package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP消息
 * 
 * @author zcg<br/>
 * @version 1.0<br/>
 * @email: zcg@suntektech.com<br/>
 * @datetime: 2013-8-29 <br/>
 */
public class HttpMessage {


    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_USER_AGENT = "User-Agent";

    public static final String AGENT_NAME = "Http-Client-Tool";
    public static final String CONTENT_TYPE_FORM = "form";

    private String requestUrl = "";
    private String method = "";
    private String body = "";
    private String contentType = "";
    private boolean isFrom = true;

    private String attachName = "";
    private String attachPath = "";

    private String protocol = "";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    /**
     * 消息头集合
     */
    List<HttpHeader> headers = null;

    /**
     * 消息体集合
     */
    List<HttpBodyParam> params = null;

    public HttpMessage() {
        headers = new ArrayList<HttpHeader>();
        params = new ArrayList<HttpBodyParam>();
    }

    public void addHeader(String key, String value) {
        HttpHeader header = new HttpHeader(key, value);
        headers.add(header);
    }

    public void addBodyParam(String name, String value) {
        HttpBodyParam bodyParam = new HttpBodyParam(name, value);
        params.add(bodyParam);
    }

    /**
     * 获取URL地址
     * 
     * @return
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getProtocolStr() {
        String headS = "://";
        this.protocol = requestUrl.substring(0, requestUrl.indexOf(headS));
        return protocol;
    }

    /**
     * 获取请求相对路径
     * 
     * @return
     */
    public String getRequestPath() {
        // http://localhost:8090/ap/service/index.jsp
        String headS = "://";
        String lastPart = requestUrl.substring(requestUrl.indexOf(headS) + headS.length());
        String path = lastPart.substring(lastPart.indexOf("/"));
        return path;
    }

    /**
     * 获取主机地址
     * 
     * @return
     */
    public String getHost() {
        String headS = "://";
        String lastPart = requestUrl.substring(requestUrl.indexOf(headS) + headS.length());
        String host = lastPart.substring(0, lastPart.indexOf("/"));
        if (host.indexOf(":") > 0) {
            return host.split(":")[0];
        }
        return host;
    }

    /**
     * 获取主机端口
     * 
     * @return
     */
    public int getPort() {
        String headS = "://";
        String lastPart = requestUrl.substring(requestUrl.indexOf(headS) + headS.length());
        String host = lastPart.substring(0, lastPart.indexOf("/"));
        if (host.indexOf(":") > 0) {
            return Integer.parseInt(host.split(":")[1]);
        }
        return 80;
    }

    public int getBodyLength() {
        return body.length();
    }

    public boolean isGzip() {
        for (HttpHeader header : headers) {
            if (header.getKey().equalsIgnoreCase("Content-Encoding")
                    && header.getValue().equalsIgnoreCase("gzip")) {
                return true;
            }
        }
        return false;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public List<HttpBodyParam> getParams() {
        return params;
    }

    public void setParams(List<HttpBodyParam> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method.toUpperCase();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public boolean isFrom() {
        return isFrom;
    }

    public void setFrom(boolean isFrom) {
        this.isFrom = isFrom;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeReal() {
        String[] arr = contentType.split(";");
        return arr[0].trim();
    }

    public String getCharset() {
        String[] arr = contentType.split(";");
        return arr[1].trim().split("=")[1].trim();
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public static void main(String[] args) {
        String test = "https://localhost:8090/ap/service/index.jsp";
        String headS = "://";
        System.out.println(test.substring(0, test.indexOf(headS)));
    }
}
