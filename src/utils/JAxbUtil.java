package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.ImdbMain;
import entity.Movie;

public class JAxbUtil {

	public static void convertMovieToXml(Movie movie)
	{
		
		 try {
			 	System.out.println(ImdbMain.cachDataFolder + "\\"+movie.getName()+"("+movie.getYear()+")_"+ImdbMain.movieCachFileSuffix);
				File file = new File(ImdbMain.cachDataFolder + "\\"+movie.getName()+"("+movie.getYear()+")_"+ImdbMain.movieCachFileSuffix);
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		 
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 
				jaxbMarshaller.marshal(movie, file);
		 
		  } catch (JAXBException e) {
				e.printStackTrace();
		  }
	}
	
	public static Set<Movie> getMoviesFromCach()
	{
		Set<Movie> movies = new HashSet<>();
		
		File dataFolder = new File(ImdbMain.cachDataFolder);
		File[] dataFiles = dataFolder.listFiles();
		for (int i = 0; i < dataFiles.length; i++) {
			File movieDataFile = dataFiles[i];
			Movie movie = getMovieFromXml(movieDataFile);
			movies.add(movie);
		}
		return movies;
	}
	
	private  static Movie getMovieFromXml(File dataFile)
	{
		Movie movie =  null;
		try {
			 
			JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
	 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			movie = (Movie) jaxbUnmarshaller.unmarshal(dataFile);
			 
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		 return movie;
	}
	
}
