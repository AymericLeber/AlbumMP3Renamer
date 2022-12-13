/**
 * Ce programme permet de renommer des fichiers mp3 automatiquement selon leur numero de track
 * @author Aymeric Leber
 * @version 0.1
 */
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class Renamer 
{
	private File path;
	private String[] tabFichier;

	/* 
	 * @param Le path du dossier contenant les mp3 a traite
	 */
	public Renamer(String path)
	{
		this.path = new File(path);
		this.tabFichier = this.path.list();		
	}

	/*
	 * Permet de visualiser dans le terminale tout les fichiers du repertoire courant
	 * @since 0.1
	 */
	public void ls()
	{
		this.updateListeFichier();

		for (String fichier : this.tabFichier)
		{
			System.out.println(fichier);
		}
	}

	private void updateListeFichier()
	{
		FilenameFilter filtre = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".mp3");
			}
		};

		this.tabFichier = this.path.list(filtre);
	}

	public boolean numerotation()
	{
        Scanner scan = new Scanner(System.in);
		String num;

		this.updateListeFichier();

		for (String nomMp3 : this.tabFichier)
		{
			try 
			{
				File mp3 = new File(this.path + "\\" + nomMp3);
				AudioFile f = AudioFileIO.read(mp3);
				Tag tag = f.getTag();
	
				System.out.println("Numéro de track du fichier : \n" + nomMp3);
				num = scan.nextLine();
				tag.setField(FieldKey.TRACK, num);

				mp3.renameTo(new File(this.path + "\\" + num + nomMp3));
				f.commit();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		scan.close();
		return true;
	}

	/*
	 * Test a supprimer, debug pour comprendre l'utilisation du mp3
	 * @deprecated
	 */
	public void test(String nomFichier)
	{
		AudioFile f;
		try 
		{
			f = AudioFileIO.read(new File(this.path + "\\" + nomFichier));
			Tag tag = f.getTag();
			System.out.println(tag.getFirst(FieldKey.TRACK));
			tag.setField(FieldKey.TRACK, "10");
			System.out.println(tag.getFirst(FieldKey.TRACK));
			f.commit();

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		Renamer folder = new Renamer("C:\\Users\\turquoise2805\\Music\\" + args[0]);
		folder.ls();
		folder.numerotation();
	}
}