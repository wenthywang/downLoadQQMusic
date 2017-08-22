package utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLException;

import org.apache.commons.lang.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import entity.HttpHeader;
import entity.HttpMessage;

/**
 * Comment
 * 
 * @author zcg<br/>
 * @version 1.0<br/>
 * @email: zcg@pci-suntektech.com<br/>
 * @datetime: 2013-7-25 <br/>
 */
public class HttpClienProxy {

    static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";

    /**
     * 连接超时
     */
    private static final int HTTP_CONNECTION_TIMEOUT = 100000;
    /**
     * 请求超时
     */
    private static final int HTTP_REQUEST_TIMEOUT = 200000;


    public HttpResponse sendRequest(CloseableHttpClient httpclient, HttpMessage message) throws IOException {
        return this.sendRequest(httpclient, message, true);
    }

    public HttpResponse sendRequest(CloseableHttpClient httpclient, HttpMessage message, HttpContext context)
            throws IOException {
        return this.sendRequest(httpclient, message, context, true);
    }

    public HttpResponse sendRequest(CloseableHttpClient httpclient, HttpMessage message, boolean closeHttp)
            throws IOException {
        if (HttpMessage.METHOD_GET.equalsIgnoreCase(message.getMethod())) {
            return httpGet(httpclient, message, closeHttp);
        } else if (HttpMessage.METHOD_POST.equalsIgnoreCase(message.getMethod())) {
            return httpPost(httpclient, message, closeHttp);
        } else if (HttpMessage.METHOD_PUT.equalsIgnoreCase(message.getMethod())) {
            return httpPut(httpclient, message, closeHttp);
        } else if (HttpMessage.METHOD_DELETE.equalsIgnoreCase(message.getMethod())) {
            return httpDelete(httpclient, message, closeHttp);
        }
        return null;
    }

    public HttpResponse sendRequest(CloseableHttpClient httpclient, HttpMessage message,
            HttpContext context, boolean closeHttp) throws IOException {
        if (HttpMessage.METHOD_GET.equalsIgnoreCase(message.getMethod())) {
            return httpGet(httpclient, message, context, closeHttp);
        } else if (HttpMessage.METHOD_POST.equalsIgnoreCase(message.getMethod())) {
            return httpPost(httpclient, message, context, closeHttp);
        } else if (HttpMessage.METHOD_PUT.equalsIgnoreCase(message.getMethod())) {
            return httpPut(httpclient, message, context, closeHttp);
        } else if (HttpMessage.METHOD_DELETE.equalsIgnoreCase(message.getMethod())) {
            return httpDelete(httpclient, message, closeHttp);
        }
        return null;
    }

    private HttpResponse httpGet(CloseableHttpClient httpclient, HttpMessage message, boolean closeHttp) {
        HttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(message.getRequestUrl());
            // 初始HTTP请求头
            handleRequestHeader(httpget, message);
            response = httpclient.execute(httpget);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return response;
    }

    private HttpResponse httpPost(CloseableHttpClient httpclient, HttpMessage message, boolean closeHttp)
            throws IOException {

        HttpResponse response = null;

        try {
            HttpPost httppost = new HttpPost(message.getRequestUrl());

            // 初始HTTP请求头
            handleRequestHeader(httppost, message);

            HttpEntity entity = new StringEntity(message.getBody(), CharEncoding.UTF_8);
            httppost.setEntity(entity);

            response = httpclient.execute(httppost);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                httpclient.close();
            }
        }
        return response;
    }

    private HttpResponse httpGet(CloseableHttpClient httpclient, HttpMessage message, HttpContext context,
            boolean closeHttp) {
        HttpResponse response = null;
        try {

            HttpGet httpget = new HttpGet(message.getRequestUrl());
            // 初始HTTP请求头
            handleRequestHeader(httpget, message);
            response = httpclient.execute(httpget, context);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return response;
    }

    private HttpResponse httpPost(CloseableHttpClient httpclient, HttpMessage message, HttpContext context,
            boolean closeHttp) throws IOException {

        HttpResponse response = null;

        try {
            HttpPost httppost = new HttpPost(message.getRequestUrl());

            // 初始HTTP请求头
            handleRequestHeader(httppost, message);

            HttpEntity entity = new StringEntity(message.getBody(), CharEncoding.UTF_8);
            httppost.setEntity(entity);

            response = httpclient.execute(httppost, context);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
//                httpclient.close();
            }
        }
        return response;
    }

    /**
     * 初始化Header
     * 
     * @param request
     * @param message
     */
    private void handleRequestHeader(HttpRequestBase request, HttpMessage message) {
        List<HttpHeader> list = message.getHeaders();
        for (HttpHeader header : list) {
            if (!request.containsHeader(header.getKey()))
                request.addHeader(header.getKey(), header.getValue());
        }

//        String contentType = message.getContentType();
//        String attachPath = message.getAttachPath();
//        if (message.isFrom() && attachPath != null && !"".equals(attachPath)) {
//            contentType += "; boundary=" + BOUNDARY;
//        }

//        // 设置ContentType
//        if ((request instanceof HttpPut) || (request instanceof HttpPost)) {
//            request.addHeader("Content-Type", contentType);
//        }
    }

    private HttpResponse httpPut(CloseableHttpClient httpclient, HttpMessage message, boolean closeHttp) {

        HttpResponse response = null;

        try {

            HttpPut httpput = new HttpPut(message.getRequestUrl());
            // 初始HTTP请求头
            handleRequestHeader(httpput, message);
            HttpEntity entity = new StringEntity(message.getBody(), CharEncoding.UTF_8);
            httpput.setEntity(entity);

            response = httpclient.execute(httpput);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

        return response;
    }

    private HttpResponse httpPut(CloseableHttpClient httpclient, HttpMessage message, HttpContext context, boolean closeHttp) {

        HttpResponse response = null;

        try {

            HttpPut httpput = new HttpPut(message.getRequestUrl());
            // 初始HTTP请求头
            handleRequestHeader(httpput, message);
            HttpEntity entity = new StringEntity(message.getBody(), CharEncoding.UTF_8);
            httpput.setEntity(entity);

            response = httpclient.execute(httpput, context);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

        return response;
    }

    private HttpResponse httpDelete(CloseableHttpClient httpclient, HttpMessage message, boolean closeHttp) {

        HttpResponse response = null;
        try {

            HttpDelete httpdelete = new HttpDelete(message.getRequestUrl());
            // 初始HTTP请求头
            handleRequestHeader(httpdelete, message);
            response = httpclient.execute(httpdelete);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null && closeHttp) {
                try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return response;
    }

//    public HttpResponse uploadFile(CloseableHttpClient httpclient, HttpMessage message, String filename,
//            InputStream stream, HttpContext context, boolean closeHttp) throws IOException {
//        HttpResponse response = null;
//        try {
//            HttpPost httppost = new HttpPost(message.getRequestUrl());
//
//            // 初始HTTP请求头
//            handleRequestHeader(httppost, message);
//
//            InputStreamBody body = new InputStreamBody(stream, filename);
//            MultipartEntity entity = new MultipartEntity();
//            entity.addPart("uploadFile", body);
//
//            httppost.setEntity(entity);
//
//            response = httpclient.execute(httppost, context);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (httpclient != null && closeHttp) {
//                httpclient.close();
//            }
//        }
//        return response;
//    }
    
   /**
   * 初始化client
   * 不添加请求超时参数
   * 添加请求超时参数导致httpclient读取返回数据异常
   * @return
   */
	public CloseableHttpClient initHttpClient() {
		RequestConfig customizedRequestConfig = RequestConfig.custom().
		setCookieSpec(CookieSpecs.STANDARD)
		.setConnectTimeout(HTTP_CONNECTION_TIMEOUT)
		.setConnectionRequestTimeout(HTTP_REQUEST_TIMEOUT)
		.setSocketTimeout(HTTP_REQUEST_TIMEOUT)
		.setExpectContinueEnabled(true)
		.build();

        HttpClientBuilder customizedClientBuilder = HttpClients.custom().setDefaultRequestConfig(customizedRequestConfig)
        		.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36")
        		.setRetryHandler(getRetryHandler());
        CloseableHttpClient client = customizedClientBuilder.build(); // customized c
        
        return client;
    }
	
	/**
	 * 重连handler
	 * @return
	 */
	private HttpRequestRetryHandler getRetryHandler(){
		return new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception,int executionCount,HttpContext context) {
			if (executionCount >= 10) {
			// Do not retry if over max retry count
			return false;
			}
			if (exception instanceof InterruptedIOException) {
			// Timeout
			return false;
			}
			if (exception instanceof UnknownHostException) {
			// Unknown host
			return false;
			}
		  //connection time out 后重连 默认是false
			if (exception instanceof ConnectTimeoutException) {
			// Connection refused
			return true;
			}
			if (exception instanceof SSLException) {
			// SSL handshake exception
			return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
			// Retry if the request is considered idempotent
			return true;
			}
			return false;
			}
			};
	}
}
