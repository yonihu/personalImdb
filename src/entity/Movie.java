package entity;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fileSystem.MovieFolder;

@XmlRootElement
public class Movie {

	
	private MovieFolder.FolderType type;
	
	private String name;
	
	private String year;
	
	private String imdbID;
	
	private String imdbRate;
	
	private String movieTrailer;
	
	private String plot;
	
	private String runTime;
	
	private String[] genre;
	
	private String[] actors;
	
	private String language;
	
	private String director;
	
	private String moviePoster;
	
	private String folderFullPath;
		
	boolean isSeenByMe = true;
	
	public static Set<String> genres = new HashSet<>();
	
	public Movie()
	{
		
	}

	public Movie(String json,String movieUrl,MovieFolder.FolderType type,String folderFullPath,String movieNameByFolder,boolean isSeenByMe) throws Exception
	{
		try {
			setFieldsbyJson(json);
			if(this.getName() == null)
				throw new Exception();
		} catch (ParseException e) {
			setFieldsAsNA(movieNameByFolder);
			System.out.println("ubable to pares movie data for :"+ movieNameByFolder);
		}
		String youtubeId = movieUrl.replace("http://www.youtube.com/v/","");
		youtubeId= youtubeId.substring(0,youtubeId.indexOf("&"));
		this.movieTrailer = "https://youtube.googleapis.com/v/"+ youtubeId+"%26feature=youtube_gdata_player";
		
		this.type = type;
		this.folderFullPath = folderFullPath;
		this.isSeenByMe = isSeenByMe;
	}
	
	public String getMoviePoster() {
		return moviePoster;
	}
	
	@XmlElement
	public void setMoviePoster(String moviePoster) {
		this.moviePoster = moviePoster;
	}

	public String getDirector() {
		return director;
	}

	@XmlElement
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getName() {
		if(type == MovieFolder.FolderType.MOVIES_SERIES)
			return name+ " Movie Series";
		else
			return name;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	
	@XmlElement
	public void setYear(String year) {
		this.year = year;
	}
	public String getImdbID() {
		return imdbID;
	}
	
	@XmlElement
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	public String getImdbRate() {
		return imdbRate;
	}
	
	@XmlElement
	public void setImdbRate(String imdbRate) {
		this.imdbRate = imdbRate;
	}
	public String getMovieTrailer() {
		return movieTrailer;
	}
	
	@XmlElement
	public void setMovieTrailer(String movieTrailer) {
		this.movieTrailer = movieTrailer;
	}
	public String getPlot() {
		return plot;
	}
	
	@XmlElement
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public String getRunTime() {
		return runTime;
	}
	
	@XmlElement
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String[] getGenre() {
		return genre;
	}
	
	@XmlElement
	public void setGenre(String[] genre) {
		this.genre = genre;
	}
	public String[] getActors() {
		return actors;
	}
	
	@XmlElement
	public void setActors(String[] actors) {
		this.actors = actors;
	}
	public String getLanguage() {
		return language;
	}
	
	@XmlElement
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public int getRateAsInt()
	{
		if("N/A".equals(this.imdbRate))
		{
			return 0 ;
		}
		else
		{
			float f = Float.parseFloat(this.imdbRate);
			return (int)Math.abs(f);
		}
	}
	
	public String generateClass()
	{
		StringBuilder builder = new StringBuilder();
		int rate = getRateAsInt();
		for (int i = rate; i <10; i++) {
			builder.append("rate-"+i);
			builder.append(" ");
		}
		
		for (int i = 0; i < genre.length; i++) {
			builder.append(genre[i].trim());
			builder.append(" ");
		}
		if(!this.isSeenByMe)
			builder.append("notSeen");
		else
			builder.append("seen");
		
		return builder.toString();
	}
	
	private void setFieldsbyJson(String json) throws ParseException
	{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		 
		JSONObject jsonObject = (JSONObject) obj;
		
		String Title = (String) jsonObject.get("Title");
		this.setName(Title);
		
		String Year = (String) jsonObject.get("Year");
		this.setYear(Year);
		
		String Runtime = (String) jsonObject.get("Runtime");
		this.setRunTime(Runtime);
		
		String Director = (String) jsonObject.get("Director");
		this.setDirector(Director);
		
		String Plot = (String) jsonObject.get("Plot");
		this.setPlot(Plot);
		
		String Language = (String) jsonObject.get("Language");
		this.setLanguage(Language);
		
		String Poster = (String) jsonObject.get("Poster");
		this.setMoviePoster(Poster);
		
		String imdbRating = (String) jsonObject.get("imdbRating");
		this.setImdbRate(imdbRating);
		
		String imdbID = (String) jsonObject.get("imdbID");
		this.setImdbID(imdbID);
		
		String[] parts = null ;
		String Actors = (String) jsonObject.get("Actors");
		if(Actors != null)
		{
			parts = Actors.split(",");
			setActors(parts);
		}
		
		String Genre = (String) jsonObject.get("Genre");
		if(Genre != null)
		{
			parts = Genre.split(",");
			setGenre(parts);
		}
		if(parts != null)
		{
			for (int i = 0; i < parts.length; i++) {
				genres.add(parts[i]);
			}
		}
	}
	
	private void setFieldsAsNA(String movieNameByFolderName)
	{
	
		this.setName("");
		this.setYear("NA");
		this.setRunTime("NA");
		this.setDirector("NA");
		this.setPlot("NA");
		this.setLanguage("NA");
		this.setMoviePoster("NA");
		this.setImdbRate("NA");
		this.setImdbID("NA");
		String[] parts1 = {"NA"};
		this.setActors(parts1);
		setGenre(parts1);
	}
	
	public String getFolderFullPath() {
		return folderFullPath;
	}

	@XmlElement
	public void setFolderFullPath(String folderFullPath) {
		this.folderFullPath = folderFullPath;
	}
	
	public MovieFolder.FolderType getType() {
		return type;
	}

	@XmlElement
	public void setType(MovieFolder.FolderType type) {
		this.type = type;
	}

	public boolean isSeenByMe() {
		return isSeenByMe;
	}

	@XmlElement
	public void setSeenByMe(boolean isSeenByMe) {
		this.isSeenByMe = isSeenByMe;
	}

	
	public static Set<String> getGenres() {
		return genres;
	}

	//@XmlElement
	public static void setGenres(Set<String> genres) {
		Movie.genres = genres;
	}
}
