package imdb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import fileSystem.MovieFolder;

public class ImdbConnector {

	private static final String USER_AGENT = "Mozilla/5.0";
	
	private static final String API_URL = "http://www.imdbapi.com/?i=";
	
	private static final String MOVIE_NAME_QUERY_PARAM = "&t";
	
	private static final String MOVIE_YEAR_QUERY_PARAM = "year";
	
	public static String  getMovieData(MovieFolder m)
	{
		String url ;
		String imdbId = m.getImdbID();
		if(imdbId != null)
		{
			url = API_URL+ imdbId;
		}
		else
		{
			String movieName = m.getMovieName();
			movieName = movieName.replace(" ", "%20").replace(":", "%3A");
			
			url = API_URL+ MOVIE_NAME_QUERY_PARAM+"="+movieName+"&"+MOVIE_YEAR_QUERY_PARAM+"="+m.getYear();
			
		}
		
		try
		{
			//url = targetURL.replace(":", "%3A");
			String jsonData =excutePost(url);
			//System.out.println(jsonData);
			return jsonData;
		}
		catch(Exception e)
		{
			System.out.println("ERORR:"+url);
			return null;
		}
	}
	
	private static String excutePost(String targetURL) throws Exception 
	  {
//		targetURL = targetURL.replace(":", "%3A");
		URL obj = new URL(targetURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		if(responseCode != 200)
		{
			System.out.println("\nSending 'GET' request to URL : " + targetURL);
			System.out.println("Response Code : " + responseCode);
		}
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		if(response.toString().indexOf("Movie not found") >-1)
			System.out.println("Movie not found :"+"  "+targetURL);
		return response.toString();
	  }
}
