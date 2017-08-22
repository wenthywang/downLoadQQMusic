package download;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import utils.HttpUtil;

/**
 * 
 * <pre>
 *下载QQ音乐 。
 * </pre>
 * 
 * @author wangwenhui 946374340@qq.com
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public class DownLoadMusicMain {

	private static HttpContext context = new BasicHttpContext();

	/**
	 * 主函数
	 * 
	 * @throws Exception
	 */
	public static void main(String arg[]) throws Exception {
      List<String>searchList=new ArrayList<String>();
      searchList.add("五月天");
      searchList.add("陈奕迅");
      searchList.add("周杰伦");
      for (String name : searchList) {
    	  int total=getQQMusic(name,1);
  		System.out.println("total->"+total);
  		int page=total/20;
  		if(total%20>0){
  			page++;
  		}
  		for(int i=2;i<=page;i++){
  			System.out.println("current page->"+i);
  			getQQMusic(name,i);
  	
  		}
  		System.out.println("download error count->"+HttpUtil.ERROR_SONG_COUNT);
  		HttpUtil.ERROR_SONG_COUNT=0;
	}
		
	}

	/**
	 * 根据搜索内容获取音乐列表
	 * @param searchContent
	 * @throws Exception
	 */
	public static int getQQMusic(String searchContent,int pageNo) throws Exception {
		String result = null;
		
		String search_url = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?" + "ct=24" + "&qqmusic_ver=1298"
				+ "&new_json=1" + "&remoteplace=txt.yqq.song" + "&searchid=70231880807041663" + "&t=0" + "&aggr=1"
				+ "&cr=1" + "&catZhida=1" + "&lossless=0" + "&flag_qc=0" + "&p="+pageNo + "&n=20" + "&w="
				+ URLEncoder.encode(searchContent, "UTF-8") + "&g_tk=5381"
				+ "&loginUin=0" + "&hostUin=0" + "&format=jsonp" + "&inCharset=utf8" + "&outCharset=utf-8" + "&notice=0"
				+ "&platform=yqq" + "&needNewCode=0";
		result = HttpUtil.get(search_url, null, context);
		String newResult = result.replace("callback(", "");
		int index =newResult.lastIndexOf(")");
		newResult=	newResult.substring(0, index);
		
		JSONObject obj = JSONObject.parseObject(newResult);
		JSONArray list = obj.getJSONObject("data").getJSONObject("song").getJSONArray("list");
		//音乐列表总数
		int toltalSong=obj.getJSONObject("data").getJSONObject("song").getInteger("totalnum");
		for (Object object : list) {
			JSONObject song = (JSONObject) object;
			String songName = song.getString("title");
			String singer = song.getJSONArray("singer").getJSONObject(0).getString("name");
			String media_mid = song.getJSONObject("file").getString("media_mid");
			String album_name = song.getJSONObject("album").getString("name");
			String mid = song.getString("mid");
			downLoadMp3(songName, singer, mid,media_mid,album_name,searchContent);
		}
		return toltalSong;
	}

	/**
	 * 根据音乐信息 下载音乐
	 * @param songName
	 * @param singer
	 * @param mid
	 * @throws Exception
	 */
	public static void downLoadMp3(String songName, String singer, String mid,String media_id,String album_name,String searchContent) throws Exception {
		String result = null;
		String filename = "C400" + media_id + ".m4a";
//		String prev_config="https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?"
//				+ "g_tk=5381"
//				+ "&jsonpCallback=MusicJsonCallback31108080259641"
//				+ "&loginUin=0"
//				+ "&hostUin=0"
//				+ "&format=json"
//				+ "&inCharset=utf8"
//				+ "&outCharset=utf-8"
//				+ "&notice=0&platform=yqq"
//				+ "&needNewCode=0"
//				+ "&cid=205361747"
//				+ "&callback=MusicJsonCallback31108080259641"
//				+ "&uin=0"
//				+ "&songmid="+mid
//				+ "&filename="+filename
//				+ "&guid=1250403680";
//
//		result = HttpUtil.get(prev_config, null, null);
//		String newResult = result.replace("MusicJsonCallback31108080259641(", "");
//		int index =newResult.lastIndexOf(")");
//		newResult=	newResult.substring(0, index);
//		System.out.println(newResult);
//		JSONObject obj = JSONObject.parseObject(newResult);
//		String vkey = obj.getJSONObject("data").getJSONArray("items").getJSONObject(0).getString("vkey");
		
		
		
		
		
		
		
		
		String get_config = "https://c.y.qq.com/base/fcgi-bin/fcg_musicexpress.fcg?json=3" + "&guid=1250403680"
				+ "&g_tk=439947196" + "&jsonpCallback=jsonCallback" + "&loginUin=946374340" + "&hostUin=0"
				+ "&format=jsonp" + "&inCharset=utf8" + "&outCharset=GB2312" + "&notice=0" + "&platform=yqq"
				+ "&needNewCode=0";

		result = HttpUtil.get(get_config, null, null);
		result = result.replace("jsonCallback(", "").replace(")", "").replace(";", "");
		// System.out.println(result);
		JSONObject obj2 = JSONObject.parseObject(result);
		 String vkey = obj2.getString("key");
		String url = "http://dl.stream.qqmusic.qq.com/" + filename + "?" + "vkey=" + vkey
				+ "&guid=1250403680&uin=0&fromtag=66";

		HttpUtil.getMedia(url, songName, singer, null,album_name,searchContent);

	}

}
