package entity;

/**
 * HTTP消息头
 * 
 * @author zcg<br/>
 * @version 1.0<br/>
 * @email: zcg@suntektech.com<br/>
 * @datetime: 2013-8-29 <br/>
 */
public class HttpHeader {

    private String key = "";
    private String value = "";

    public HttpHeader() {

    }

    public HttpHeader(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
