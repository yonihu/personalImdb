package fileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MovieFolder {

	public enum FolderType {MOVIE,MOVIES_SERIES}
	public static String folderImdbFile = "imdbId.txt";
	
	String movieName;
	int year;
	private FolderType folderType ;
	private String folderPath;
	boolean isSeenByMe = false;
	private String imdbID ;
	
	
	public boolean isSeenByMe() {
		return isSeenByMe;
	}

	public void setSeenByMe(boolean isSeenByMe) {
		this.isSeenByMe = isSeenByMe;
	}


	
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public  static MovieFolder createMovieFolder(File folder)
	{
		MovieFolder movieFolder = null;
		try
		{
			FolderType folderType = folderType(folder);
			if(FolderType.MOVIE == folderType)
	    	{
				movieFolder = MovieFolder.createMovie(folder);
	    	}
			else
	    	{
	    		System.out.println("	the folder :"+folder.getName() +" has subfolder in it other than 'Subs', so it conidered as a movie series folder");
	    		movieFolder = MovieFolder.createMovieSeries(folder);
	    	}
			
			return movieFolder;
		}
		catch(Exception e)
		{
			System.out.println("ERROR READING FOLDER:"+ folder.getName()+ " check if the folder name is in the followed format : <ENGLISH NAME>.<YERA>.<HEBREW NAME> \n(common problem is more or less then 2 dots in the folder name\n");
//
			return null;
		}
	}
	
	private static MovieFolder createMovie(File folder) throws Exception
	{
		boolean isSeenByMeTemp = true;
		if(folder.getName().indexOf('_') == 0)
			isSeenByMeTemp = false;
		
		String imdbID = getId(folder);
		String movieEnglishName = getName(folder);
		int movieYear = getYear(folder.getName());
		return new MovieFolder(movieEnglishName,movieYear,FolderType.MOVIE,folder.getAbsolutePath(),isSeenByMeTemp,imdbID);

	}
	
	private static MovieFolder createMovieSeries(File folder) throws Exception
	{
		String movieEnglishName = getName(folder);
		return new MovieFolder(movieEnglishName,FolderType.MOVIES_SERIES,folder.getAbsolutePath(),true,null);
	}
	
	private  MovieFolder(String movieName,int year,FolderType folderType,String fullPath,boolean isSeenByMe,String imdbID) {
		this(movieName,folderType,fullPath,isSeenByMe,imdbID);
		this.year = year;
	}
	
	private  MovieFolder(String movieName,FolderType folderType,String fullPath,boolean isSeenByMe,String imdbID) {
		super();
		this.movieName = movieName;
		this.folderType = folderType;
		this.folderPath = fullPath;
		this.isSeenByMe = isSeenByMe;
		this.imdbID = imdbID;
	}
	
	public FolderType getFolderType() {
		return folderType;
	}

	public void setFolderType(FolderType folderType) {
		this.folderType = folderType;
	}

	public String getMovieName() {
		return movieName;
	}


	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}

		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((movieName == null) ? 0 : movieName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieFolder other = (MovieFolder) obj;
		if (movieName == null) {
			if (other.movieName != null)
				return false;
		} else if (!movieName.equals(other.movieName))
			return false;
		return true;
	}

//	private int setMovieYear()
//	{
//		return year;
//	}
//	
//	private static String[] validateFolderName(String fullName)
//	{
//		String[] parts = null;
//		try
//		{
//			if(fullName == null)
//				throw new RuntimeException();
//			
//			parts = new String[2];
//			parts[0] = fullName.substring(0,fullName.indexOf('.'));
//			
//			String temp = fullName.substring(fullName.indexOf('.')+1);
//			parts[1] = temp.substring(0,temp.indexOf('.'));
//			
//		    try { 
//		        Integer.parseInt(parts[1]); 
//		    } catch(NumberFormatException e) { 
//		    	throw new RuntimeException();
//		    }
//		}
//		catch(Exception e)
//		{
//			System.out.println("ERROR READING FOLDER:"+ fullName);
//			return null;
//		}
//	    return parts;
//	}
	
	private static String getId(File folder) throws Exception
	{
		File[] filesInFolder = folder.listFiles();
		for (int i = 0; i < filesInFolder.length; i++) {
			if("imdbId.txt".equals(filesInFolder[i].getName()))
			{
				//read name of the movie from a file in the directory - not from the folder nmae
				String everything;
				BufferedReader br = new BufferedReader(new FileReader(filesInFolder[i]));
			    try {
			        StringBuilder sb = new StringBuilder();
			        String line = br.readLine();

			        while (line != null) {
			            sb.append(line);
			            sb.append(System.lineSeparator());
			            line = br.readLine();
			        }
			        everything = sb.toString();
			    } finally {
			        br.close();
			    }
			    return everything;
			}
		}
		return null;
	}
	
	private static String getName(File folder) throws Exception
	{
//		String imdbId = getId(folder);
//		if(imdbId != null)
//			return imdbId;
		
		String fullName = folder.getName();
		String folderEnglishName = null;
		if(fullName == null)
			throw new RuntimeException();
		
		folderEnglishName = fullName.substring(0,fullName.indexOf('.'));
		folderEnglishName = folderEnglishName.replace("_", "");
		return folderEnglishName;
	}
	
	private static int getYear(String fullName) throws Exception
	{
		//String[] parts = null;
		String yearStr = null;
		int year;

		if(fullName == null)
			throw new RuntimeException();
		
		String temp = fullName.substring(fullName.indexOf('.')+1);
		yearStr = temp.substring(0,temp.indexOf('.'));
		
	    try { 
	    	year = Integer.parseInt(yearStr); 
	    	return year;
	    } catch(NumberFormatException e) { 
	    	throw new RuntimeException();
	    }
	
	}
	
	private  static MovieFolder.FolderType folderType(File folder)
	{
		for (final File fileEntry : folder.listFiles()) {
			if(fileEntry.isDirectory() && !"Subs".equals(fileEntry.getName()))
				return MovieFolder.FolderType.MOVIES_SERIES;
		}
		
		return MovieFolder.FolderType.MOVIE;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
}
