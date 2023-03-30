package com.quizcreator.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.quizcreator.data.Program;

public class ZipUtilities {
/**
 * 	/**
	 * Creates a ZIP file with the content of the work folder defined in utlis.FolderFinder including sub folders.
	 * @param folderLocation Location of the folder that shall be packed.
	 * @param stringToConcatFromLocation Standardmaessig ist die Ordnertiefe im ZIP-File unbeschnitten. Mit diesem String kann die Ordnertiefe beschnitten werden.
	 * @param zipLocation Location of the ZIP / QGM File
	 * @param overwrite TRUE if an existing ZIP file on the HDD should be overwritten. FALSE if not.
	 * 
	 */
	public static void folderToZIP(String folderLocation, String zipLocation, String stringToConcatFromLocation, Boolean overwrite) {
		
		// GGf existierende qgm Datei loeschen
		if(overwrite) {
			File oldFile = new File(zipLocation);
			if(oldFile.exists()) {
				oldFile.delete();
			}
		}
		
		// Dateiliste des Ordnerinhaltes
		File zipFile = new File(zipLocation);
		File[] srcFiles = new File(folderLocation).listFiles();

		// Fuer alle Dateien innerhalb des Ordners..
		for(int i = 0; i < srcFiles.length; i++) {
			// Pfad innerhalb der QGM Datei berechnen
			String locationInZipfile = srcFiles[i].getPath().replace(stringToConcatFromLocation, "");
			if(Program.DEBUG) System.out.println("navigating to location in QGM file: " + locationInZipfile);
			
			// Falls Zipdatei nicht bereits existiert
			if(!zipFile.exists()) {
				// Falls aktueller Dateizeiger eine Datei ist...
				File f = new File(srcFiles[i].getPath());
				if(f.isFile()) {
					// ZIP Datei anlegen und Datei reinspeichern
					writeFileIntoZIP(zipLocation,srcFiles[i].getPath(),locationInZipfile,true);
				}
				// Falls aktueller Dateizeiger ein Verzeichnis ist...
				if(f.isDirectory()) {
					// Rekursiv die folderToZIP Methode für diesen Ordner wiederholen (damit Ordnerinhalt abgespeichert wird)
					folderToZIP(f.getPath(), zipLocation, stringToConcatFromLocation, false);
				}
			}
			// Falls Zip Datei bereits existiert
			else {
				File f = new File(srcFiles[i].getPath());
				if(f.isFile()) {
					// In vorhandene ZIP Datei neue Datei reinspeichern
					writeFileIntoZIP(zipLocation,srcFiles[i].getPath(),locationInZipfile,false);
				}
				if(f.isDirectory()) {
					// Rekursiv Ordnerinhalte erfassen und reinspeichern
					folderToZIP(f.getPath(), zipLocation, stringToConcatFromLocation, false);
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////
	// ZIP to folder
	
	 private static final int  BUFFER_SIZE = 4096;

	  private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException
	  {
		outdir.mkdirs();
		if(Program.DEBUG) System.out.print("Extracting from QGM file into: " + outdir + "/" + name + " ... ");
	    byte[] buffer = new byte[BUFFER_SIZE];
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir,name)));
	    int count = -1;
	    while ((count = in.read(buffer)) != -1)
	      out.write(buffer, 0, count);
	    out.close();
	    if(Program.DEBUG) System.out.print("Done\n");
	  }

	  private static void mkdirs(File outdir,String path)
	  {
	    File d = new File(outdir, path);
	    if( !d.exists() )
	      d.mkdirs();
	  }

	  private static String dirpart(String name)
	  {
	    int s = name.lastIndexOf( File.separatorChar );
	    return s == -1 ? null : name.substring( 0, s );
	  }

	  /***
	   * Extract zipfile to outdir with complete directory structure
	   * @param zipfile Input .zip file
	   * @param outdir Output directory
	   */
	  public static void zipToFolder(File zipfile, File outdir)
	  {
	    try
	    {
	      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
	      ZipEntry entry;
	      String name, dir;
	      while ((entry = zin.getNextEntry()) != null)
	      {
	        name = entry.getName();
	        if( entry.isDirectory() )
	        {
	          mkdirs(outdir,name);
	          continue;
	        }
	        /* this part is necessary because file entry can come before
	         * directory entry where is file located
	         * i.e.:
	         *   /foo/foo.txt
	         *   /foo/
	         */
	        dir = dirpart(name);
	        if(dir != null )
	          mkdirs(outdir,dir);

	        extractFile(zin, outdir, name);
	      }
	      zin.close();
	      if(Program.DEBUG) System.out.println("QGM file successfully unpacked into: " + outdir);
	    } 
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	  }
	
	/**
	 * Writes files into a new or existing ZIP file
	 * @param ZIPLocation Location of the existing / new to create ZIP file
	 * @param sourceLocation Location of the file that shall be written into the ZIP file
	 * @param targetPathInZIPFile Target path in the ZIP file
	 * @param overwriteZIPFile true if file should be overwritten / newly created or false if files should be added
	 */
	public static void writeFileIntoZIP(String ZIPLocation, String sourceLocation, String targetPathInZIPFile, Boolean overwriteZIPFile) {
		/* Define ZIP File System Properties in HashMap */    
        Map<String, String> zip_properties = new HashMap<>(); 
        /* set create to true if you want to create a new ZIP file */
        if(overwriteZIPFile == true) {
        	// Delete eventually existing file
        	File f = new File(ZIPLocation);
        	if(f.exists()) {
        		f.delete();
        	}
        	zip_properties.put("create", "true");
        }
        else {
        	zip_properties.put("create", "false");
        }
        /* specify encoding to UTF-8 */
        zip_properties.put("encoding", "UTF-8");
        /* Locate File on disk for creation */
        Path p = Paths.get(ZIPLocation);
        URI zip_disk = URI.create("jar:" + p.toUri());
 //       System.out.println("uri: " + zip_disk.toString());
        try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zip_properties)) {
        	 /* Path to source file */   
        	p = Paths.get(sourceLocation);
            Path file_to_zip = Paths.get(p.toUri());
            if(Program.DEBUG) System.out.print("Copying into .QGM File: " + file_to_zip.toString() + " ... ");
  //          System.out.println("file_to_zip: " + file_to_zip);
            /* Path inside ZIP File. Create it if it doesn't exist */
            Path pathInZipfile = zipfs.getPath(targetPathInZIPFile);
            if(!Files.exists(pathInZipfile)) {
            	Files.createDirectories(pathInZipfile);
            }
  //          System.out.println("zip file path: " + pathInZipfile.toString());
            /* Add file to archive, replace existing files */
            Files.copy(file_to_zip,pathInZipfile,StandardCopyOption.REPLACE_EXISTING); 
            if(Program.DEBUG) System.out.print("done\n");
        } catch (IOException e) {
			// TODO Auto-generated catch block
        	System.out.println("ERROR in writeFileIntoZIP!");
			e.printStackTrace();
		}	
	}
	
	/**
	 * Deletes a specific file in a ZIP file
	 * @param ZIPLocation Location of the zip file
	 * @param locationInZipFile location of the file to be erased within the ZIP file
	 */
	public static void deleteFileFromZIP(String ZIPLocation, String locationInZipFile) {
		 /* Define ZIP File System Properies in HashMap */    
        Map<String, String> zip_properties = new HashMap<>(); 
        /* We want to read an existing ZIP File, so we set this to False */
        zip_properties.put("create", "false"); 
        
        /* Specify the path to the ZIP File that you want to read as a File System */
        URI zip_disk = URI.create("jar:" + new File(ZIPLocation).toURI().toString());
        
        /* Create ZIP file System */
        try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zip_properties)) {
            /* Get the Path inside ZIP File to delete the ZIP Entry */
            Path pathInZipfile = zipfs.getPath(locationInZipFile);
            if(Program.DEBUG) System.out.println("About to delete an entry from ZIP File " + pathInZipfile.toUri() ); 
            /* Execute Delete */
            Files.delete(pathInZipfile);
            if(Program.DEBUG) System.out.println("File successfully deleted");   
        } 
        catch(Exception e) {
        	System.out.println("ERROR in deleteFileFromZIP!");
        	e.printStackTrace();
        }
	}
}
