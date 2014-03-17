package htmlCreator;

import java.util.Iterator;
import java.util.List;

import main.ImdbMain;
import utils.FileUtil;
import entity.Movie;

public class HtmlGenerator {

	private final static String MOVIE_HTML_FILE_PATH = "src/html/mainFile.html";//"C:/Users/TikalKnowledge/workspace/Imdb/src/html/mainFile.html";
	private final static String MOVIE_DATA_HTML_FILE_PATH = "src/html/movieTemplate.html";//"C:/Users/TikalKnowledge/workspace/Imdb/src/html/movieTemplate.html";


	public static void createMoviesFile(List<Movie> moviesData)
	{
		String mainFile = FileUtil.readFile(MOVIE_HTML_FILE_PATH);
		String singleMovieHtml = FileUtil.readFile(MOVIE_DATA_HTML_FILE_PATH);
		
		StringBuffer moviesDataHtml = new StringBuffer();
		boolean doItAgin = true;
		while(doItAgin)
		{
			try
			{
				int i = 0;
				for (Iterator iterator = moviesData.iterator(); iterator.hasNext();) {
					Movie movie = (Movie) iterator.next();
					moviesDataHtml.append(createMovieData(singleMovieHtml,movie));
					System.out.println(i+" "+movie.getName());
					i++;
				}
				doItAgin = false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("SOME CRITICAL EROOR !!!");
			}
		}
	
		mainFile = mainFile.replace("<MOVIES_DATA>", moviesDataHtml);
		
		StringBuilder genreCheckBoxes = new StringBuilder();
		int i = 0;
		boolean lastHasClosedDiv = false;
		for (Iterator iterator = Movie.genres.iterator(); iterator.hasNext();) {
			lastHasClosedDiv = false;
			if(i % 5 == 0)
				genreCheckBoxes.append("<div style='float:left'>");
			String genre = (String) iterator.next();
			genreCheckBoxes.append("<span style='margin-right:20px;'><input type='checkbox' name='genre' value='"+genre+"'>"+genre+" </span><br>");
			if(i % 5 == 4)
			{
				genreCheckBoxes.append("</div>");
				lastHasClosedDiv = true;
			}
			i++;
		}
		if(!lastHasClosedDiv)
			genreCheckBoxes.append("</div>");
		mainFile = mainFile.replace("<MOVIES_GENRE_LIST>", genreCheckBoxes.toString());
		
		System.out.println("creating file in "+ImdbMain.DRIVE+ImdbMain.MOVIES_DIRECTORY_FOLDER);
		FileUtil.createMoviesFile(ImdbMain.DRIVE+ImdbMain.MOVIES_DIRECTORY_FOLDER,mainFile);
	}
	
	
	
	private static  String createMovieData(String htmlTemplate,Movie movie)
	{
		
		String temp = new String(htmlTemplate);
		temp = temp.replaceAll("<MOVIE_CLASS>", movie.generateClass());
		
		temp = temp.replace("<MOVIE_POSTER>", movie.getMoviePoster());
		temp = temp.replace("<MOVIE_NAME>", movie.getName());
		temp = temp.replace("<MOVIE_YEAR>", movie.getYear());
		temp = temp.replace("<MOVIE_RATE>", movie.getImdbRate());
		temp = temp.replace("<MOVIE_PLOT>", movie.getPlot());
		temp = temp.replace("<MOVIE_RUNTIME>", movie.getRunTime());
		temp = temp.replace("<MOVIE_LANG>", movie.getLanguage());
		
		StringBuilder genrebuilder = new StringBuilder();
		for(String s : movie.getGenre()) {
			genrebuilder.append(s);
		}
	
		temp = temp.replaceAll("<MOVIE_GENRE>", genrebuilder.toString());
		
		StringBuilder actorsbuilder = new StringBuilder();
		for(String s : movie.getActors()) {
			actorsbuilder.append(s);
		}
		
		temp = temp.replaceAll("<MOVIE_ACTORS>", actorsbuilder.toString());
		temp = temp.replaceAll("<MOVIE_IMDBID>", movie.getImdbID());
		temp = temp.replaceAll("<MOVIE_TRAILER>", movie.getMovieTrailer());
		
		return temp;
	}
	
	
}
