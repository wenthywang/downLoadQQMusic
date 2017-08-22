package entity;

import java.io.Serializable;

/**
 * HTTP消息体
 * 
 * @author zcg 2012-12-11 下午11:03:46
 * 
 */
public class HttpBodyParam implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name = "";
    private String value = "";

    public HttpBodyParam() {

    }

    public HttpBodyParam(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
