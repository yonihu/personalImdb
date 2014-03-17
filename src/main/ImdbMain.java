package main;
import htmlCreator.HtmlGenerator;
import imdb.ImdbConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.JAxbUtil;
import youtube.YouTubeManager;
import youtube.YouTubeVideo;
import entity.Movie;
import fileSystem.MovieDirectoryUtil;
import fileSystem.MovieFolder;


public class ImdbMain {
    static String clientID = "JavaCodeGeeks";
    static String textQuery = null;
    static int maxResults = 1;
    static boolean filter = true;
    static int timeout = 2000;

    YouTubeManager ym = new YouTubeManager(clientID);
    
	///public static final String DRIVE = "E:/";
	///public static final String MOVIES_DIRECTORY_FOLDER = "Yoni/Media/Movies/GOOD";
	//public static final String[] moviesDirectories = {DRIVE+MOVIES_DIRECTORY_FOLDER,"C:/Users/TikalKnowledge/Desktop/movies"};

	public static final String DRIVE = "c:/";
	public static final String MOVIES_DIRECTORY_FOLDER = "Users/TikalKnowledge/Desktop/movies/Seen";
	public static final String[] moviesDirectories = {DRIVE+MOVIES_DIRECTORY_FOLDER,"C:/Users/TikalKnowledge/Desktop/movies/notSeen"};
	public static final String cachDataFolder = DRIVE+"Users\\TikalKnowledge\\Desktop\\movies\\seen\\_data";
	public static final String movieCachFileSuffix = "__imdbData.xml";
	
	public static Map<String,Movie>  moviesData = new HashMap<>();

    
	public static void main(String[] args) {
		
		// get movies data from local cach directory
		Set<Movie>  moviesFromCachData = JAxbUtil.getMoviesFromCach();
		System.out.println("Found data about "+moviesFromCachData.size()+" movies");
		
		for (Iterator iterator = moviesFromCachData.iterator(); iterator.hasNext();) {
			Movie movie = (Movie) iterator.next();
			moviesData.put(movie.getFolderFullPath(), movie);		
		}
		
		
		try
		{
			YouTubeManager ym = new YouTubeManager(clientID);
			
			// invistigate the file system for movies folder
			Set<MovieFolder> movies = MovieDirectoryUtil.createMoviesInDirectoryList();
			System.out.println("Number of Movies Found :"+movies.size());
			
			for (Iterator iterator = movies.iterator(); iterator.hasNext();) {
				
				MovieFolder movieFolder = (MovieFolder) iterator.next();
				Movie movie = moviesData.get(movieFolder.getFolderPath());
				if(movie == null)
					System.out.println("found new directory : "+movieFolder.getFolderPath());
				else
				{
					continue;
				}

				if(movieFolder != null)
				{
					//go to imdb and bring the data
					String imdbJsonResponse = ImdbConnector.getMovieData(movieFolder);
					textQuery = movieFolder.getMovieName();
					
					//go to youtube and bring the trailer url
				    String trailerURL = null;
				    List<YouTubeVideo> videos = ym.retrieveVideos(textQuery+" trailer", maxResults, filter, timeout);
				    for (YouTubeVideo youtubeVideo : videos) {
			            trailerURL = youtubeVideo.getEmbeddedWebPlayerUrl();		            
			        }
				    
				    
				    try
				    {
				    	movie = new Movie(imdbJsonResponse, trailerURL,movieFolder.getFolderType(),movieFolder.getFolderPath(),movieFolder.getMovieName(),movieFolder.isSeenByMe());

				    	//create local cach file for the movie
				    	JAxbUtil.convertMovieToXml(movie);
				    	moviesData.put(movie.getFolderFullPath(),movie);
				    }
				    catch(Exception e)
				    {
				    	System.out.println("UNKnown ERROR : unable to parse "+movieFolder.getMovieName()+ " imdb data");
				    }
	
//				    if(moviesData.size() == 150)
//				    	break;
				}
			}
			
			//after finding all the new movies folder create a new file
			System.out.println("creating file with "+moviesData.size()+" movies");
			List<Movie> list = new ArrayList<Movie>(moviesData.values());
			HtmlGenerator.createMoviesFile(list);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
