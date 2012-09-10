package spread;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class GraphToImageExporter {
	
	private static List<String> allowedFormats = Arrays.asList("png", "jpg", "gif", "bmp");
	
	public static boolean export(File file, String format, myGraph graph)
			throws WrongFileFormatException, IOException{
		BufferedImage image = graph.getBufferedImage();
		
		
		if(!allowedFormats.contains(format))
			throw new WrongFileFormatException();
		
		ImageIO.write(image, format, file);
		return true;
	}

	public static Collection<String> getAllowedFormats(){
		return allowedFormats;
	}
}
