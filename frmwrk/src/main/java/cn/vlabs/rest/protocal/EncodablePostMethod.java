package cn.vlabs.rest.protocal;

import org.apache.commons.httpclient.methods.PostMethod;

public class EncodablePostMethod extends PostMethod{
    public EncodablePostMethod(String url, String encode){
        super(url);
        this.encode=encode;
    }
    @Override
    public String getRequestCharSet() {
        return encode;
    }
    private String encode;
}