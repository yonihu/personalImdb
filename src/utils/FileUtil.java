package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtil {

	public static void writeTofileFile (File f,String html)
	{
		BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f));
            writer.write(html);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	public static File createFile(String folderPath)
	{
		File f = new File(folderPath+"/movies.html");
		try {
		if (!f.exists()) {
			f.createNewFile();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
	}
	
	public static void createMoviesFile(String folderPath,String html)
	{
		File f =createFile(folderPath);
		writeTofileFile(f,html);
		System.out.println("Finished !!!!");
	}
	
	public static String readFile(String filePath)
	{
		BufferedReader br = null;
		StringBuilder template = new StringBuilder();
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(filePath));
 
			while ((sCurrentLine = br.readLine()) != null) {
				template.append(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return template.toString();
	}
}
