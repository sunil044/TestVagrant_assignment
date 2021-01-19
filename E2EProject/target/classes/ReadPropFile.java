package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropFile {
	
	
	public static Properties readProp() throws IOException
	{
		String path =System.getProperty("user.dir")+"\\Prop-files\\data.properties";
		 
		Properties prop = new Properties();
		FileInputStream fis=new FileInputStream(path);
		prop.load(fis);
		return prop;

	}
	
}
