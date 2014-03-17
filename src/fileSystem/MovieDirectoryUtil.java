package fileSystem;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import main.ImdbMain;

public class MovieDirectoryUtil 
{
	



	private static  Set<MovieFolder> movies = new HashSet<>();
	
	
	
	public static Set<MovieFolder> createMoviesInDirectoryList()
	{
		for (int i = 0; i < ImdbMain.moviesDirectories.length; i++) {
			
			File folder = new File(ImdbMain.moviesDirectories[i]);
			
			for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	
		        	MovieFolder movie = MovieFolder.createMovieFolder(fileEntry);
		        	if(movie != null)
		        		movies.add(movie);
		        } else {
		            System.out.println("	ERROR :  unkown file "+fileEntry.getName());
		        }
		    }
		}

		
		return movies;
	}
	
	private  static MovieFolder.FolderType folderType(File folder)
	{
		for (final File fileEntry : folder.listFiles()) {
			if(fileEntry.isDirectory() && !"Subs".equals(fileEntry.getName()))
				return MovieFolder.FolderType.MOVIES_SERIES;
		}
		
		return MovieFolder.FolderType.MOVIE;
	}
}
