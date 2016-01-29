package se.lanteam.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileManager {

	private static final String sourceFolder = "C:/Projekt/lanteam/filedirs/input";
	private static final String destinationFolder = "C:/Projekt/lanteam/filedirs/output";
	
	public void moveFiles() {
		final File inputFolder = new File(sourceFolder);
		File[] filesInFolder = inputFolder.listFiles();
		if (filesInFolder != null) {
			for (final File fileEntry : filesInFolder) {
				System.out.println("FILE : " + fileEntry.getName());
				Path source = Paths.get(sourceFolder + "/" + fileEntry.getName());
				Path target = Paths.get(destinationFolder + "/" + fileEntry.getName());
				try {
					List<String> rows = Files.readAllLines(source);
					for (String row : rows)
						System.out.println("row: " + row);
					System.out.println("File has been read");
					Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("File moved");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
