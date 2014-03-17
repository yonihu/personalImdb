package main;
import htmlCreator.HtmlGenerator;
import imdb.ImdbConnector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;

import youtube.YouTubeManager;
import youtube.YouTubeVideo;
import entity.Movie;
import fileSystem.MovieDirectoryUtil;
import fileSystem.MovieFolder;

//created by yoni
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
	public static final String[] moviesDirectories = {DRIVE+MOVIES_DIRECTORY_FOLDER,"C:/Users/TikalKnowledge/Desktop/movies/notSeen" +
			""};
    static Set<Movie> moviesData = new HashSet<>();

    
	public static void main(String[] args) {
		try
		{
			YouTubeManager ym = new YouTubeManager(clientID);
			
			Set<MovieFolder> movies = MovieDirectoryUtil.createMoviesInDirectoryList();
			System.out.println("Number of Movies Found :"+movies.size());
			
			for (Iterator iterator = movies.iterator(); iterator.hasNext();) {
				MovieFolder movieFolder = (MovieFolder) iterator.next();
				if(movieFolder != null)
				{
					String json = ImdbConnector.getMovieData(movieFolder);
					textQuery = movieFolder.getMovieName();
				    List<YouTubeVideo> videos = ym.retrieveVideos(textQuery+" trailer", maxResults, filter, timeout);
				    
				    String trailerURL = null;
				    for (YouTubeVideo youtubeVideo : videos) {
			            trailerURL = youtubeVideo.getEmbeddedWebPlayerUrl();		            
			        }
				    
				    try
				    {
				    	Movie movie = new Movie(json, trailerURL,movieFolder.getFolderType(),movieFolder.getFolderPath(),movieFolder.getMovieName(),movieFolder.isSeenByMe());
				    	moviesData.add(movie);
				    }
				    catch(Exception e)
				    {
				    	System.out.println("UNKnown ERROR : unable to parse "+movieFolder.getMovieName()+ " imdb data");
				    }
	
//				    if(moviesData.size() == 150)
//				    	break;
				}
			}
			
			System.out.println("creating file with "+moviesData.size()+" movies");
			HtmlGenerator.createMoviesFile(moviesData);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
