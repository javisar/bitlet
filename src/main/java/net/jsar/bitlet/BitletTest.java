package net.jsar.bitlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bitlet.wetorrent.Metafile;
import org.bitlet.wetorrent.Torrent;
import org.bitlet.wetorrent.disk.PlainFileSystemTorrentDisk;
import org.bitlet.wetorrent.disk.TorrentDisk;
import org.bitlet.wetorrent.peer.IncomingPeerListener;

public class BitletTest {
	// https://code.google.com/p/bitlet/wiki/AnnotatedExample
	
	public static void main(String[] args) {
		try {
			test1(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test1(String[] args) throws Exception {
		String filename = args[0];
		int port = Integer.parseInt(args[1]);
		
		// Parse the metafile
		Metafile metafile = new Metafile(new BufferedInputStream(new FileInputStream(filename)));

		// Create the torrent disk, this is the destination where the torrent file/s will be saved
		TorrentDisk tdisk = new PlainFileSystemTorrentDisk(metafile, new File("."));
		tdisk.init();
		
		IncomingPeerListener peerListener = new IncomingPeerListener(port);
		peerListener.start();
		
		Torrent torrent = new Torrent(metafile, tdisk, peerListener);
		torrent.startDownload();
		
		while (!torrent.isCompleted()) {
		    try {
		        Thread.sleep(1000);
		    } catch(InterruptedException ie) {
		        break;
		    }
		    torrent.tick();
		}
		
		// Finlalization
		//torrent.interrupt();
		//IncomingPeerListener.interrupt
		
		
	}
}
