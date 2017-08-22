package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import entity.HttpMessage;

/**
 * 
 * Description：Http访问工具类
 * 
 * @author chenmaode<br/>
 * @version 1.0<br/>
 * @date 2015-12-26
 *
 */
public class HttpUtil {

    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    
    public static int ERROR_SONG_COUNT=0;

    public static String get(String url, String param, HttpContext context) throws Exception {
        String content = null;
        HttpClienProxy httpClienProxy = new HttpClienProxy();
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.setRequestUrl(generateGetUrl(url, param));
        httpMessage.setMethod(HttpMessage.METHOD_GET);
        CloseableHttpClient httpclient = httpClienProxy.initHttpClient();
        try {
            HttpResponse httpResponse =
                    httpClienProxy.sendRequest(httpclient, httpMessage, context,false);
            if (httpResponse != null
                    && HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8") ;
            } else if (httpResponse != null) {
                logger.warn("request url: {} response status: {}", url,
                        httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpclient != null) {
            	httpclient.close();
            }
        }
        return content;
    }
    
    public static void getMedia(String url, String songName, String singer,HttpContext context,String album_name,String searchContent) throws Exception {
    	HttpEntity content = null;
    	HttpClienProxy httpClienProxy = new HttpClienProxy();
    	HttpMessage httpMessage = new HttpMessage();
    	httpMessage.setRequestUrl(generateGetUrl(url, null));
    	httpMessage.setMethod(HttpMessage.METHOD_GET);
    	CloseableHttpClient httpclient = httpClienProxy.initHttpClient();
    	try {
    		HttpResponse httpResponse =
    				httpClienProxy.sendRequest(httpclient, httpMessage, context,false);
    		if (httpResponse != null
    				&& HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
    			content = httpResponse.getEntity();
    			
    				songName=songName.replace("?", "").replace("\\", "");
    		
    	
    				album_name=album_name.replace("?", "");
    		
    			File s=new File("song");
    			if(!s.exists()){
    				s.mkdir();
    			}
    			File t=new File("song/"+searchContent);
    			if(!t.exists()){
    				t.mkdir();
    			}
    			String fileName="song/"+searchContent+"/"+songName+"-"+singer+"-"+album_name+".mp3";
    		    if(album_name==null){
    			  fileName="song/"+searchContent+"/"+songName+"-"+singer+".mp3";
    		  }
    			File songFile=new File(fileName);
    			FileOutputStream out=new FileOutputStream(songFile);
    			FileCopyUtils.copy(content.getContent(), out);
    			//文件大小
    			String size=getFormatSize(Double.valueOf(String.valueOf(content.getContentLength())));
    			String consoleShow=songName+"-"+singer+",   "+size;
                 System.out.println(consoleShow);
    			
    		} else if (httpResponse != null) {
    			logger.warn("request url: {} response status: {}，song：{}", url,
    					httpResponse.getStatusLine().getStatusCode(),songName);
    			ERROR_SONG_COUNT++;
    		}
    	} catch (Exception e) {
//    		throw e;
    		e.printStackTrace();
    		ERROR_SONG_COUNT++;
    	} finally {
    		if (httpclient != null) {
//    			httpclient.close();
    		}
    	}
    	
    }

    private static String generateGetUrl(String url, String param) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        try {
            JSONObject paramJson = JSON.parseObject(param);
            builder.append("?");
            for (String key : paramJson.keySet()) {
                String value = URLEncoder.encode(paramJson.getString(key),"UTF-8");
                builder.append(key).append("=").append(value).append("&");
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
        } catch (Exception e) {
        }
        return builder.toString();
    }

    private static String generatePostBody(String param) throws Exception {
        StringBuilder builder = new StringBuilder();
        JSONObject paramJson = JSON.parseObject(param);
        //处理多图文参数相同问题
        if(paramJson.containsKey("isManyRichText")){
            String paramString=paramJson.getString("paramStr");
            builder.append(paramString);
        }else{
            for (String key : paramJson.keySet()) {
                String value = URLEncoder.encode(paramJson.getString(key),"UTF-8");
                builder.append(key).append("=").append(value).append("&");
            }
        }
     
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String post(String url,String type, String param, HttpContext context) throws Exception {
        String content = null;
        HttpClienProxy httpClienProxy = new HttpClienProxy();
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.setRequestUrl(url);
        httpMessage.setMethod(HttpMessage.METHOD_POST);
        httpMessage.addHeader("Content-Type", "application/"+type+";charset=utf-8");
        httpMessage.setBody(generatePostBody(param));
        CloseableHttpClient httpclient = httpClienProxy.initHttpClient();
        try {
            HttpResponse httpResponse =
                    httpClienProxy.sendRequest(httpclient, httpMessage, context,false);
            if (httpResponse != null
                    && HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                content = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } else if (httpResponse != null) {
            	int code=httpResponse.getStatusLine().getStatusCode();
            	System.out.println( "http-status-code->"+code);
            	if(code==302){
            		content="success";
            	}
        } 
            }catch (Exception e) {
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return content;
    }
    

    public static String getFormatSize(double size) {  
        double kiloByte = size/1024;  
        if(kiloByte < 1) {  
            return size + "Byte(s)";  
        }  
          
        double megaByte = kiloByte/1024;  
        if(megaByte < 1) {  
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));  
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";  
        }  
          
        double gigaByte = megaByte/1024;  
        if(gigaByte < 1) {  
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));  
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";  
        }  
          
        double teraBytes = gigaByte/1024;  
        if(teraBytes < 1) {  
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));  
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";  
        }  
        BigDecimal result4 = new BigDecimal(teraBytes);  
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";  
    }  

}
