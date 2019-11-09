package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GameFileDownloader2  {

	private String directory;
	private String mainURL = "http://itikawa.com/kifdb/herodb.cgi";
	
	public GameFileDownloader2(String directory) {
		this.directory = directory;
	}

	public void downloadPages(int start, int end) {
		
		IntConsumer pageDownloader = page -> 
			{
				try {
					downloadFilesInURL(getPageUrl(page));
				} catch (URISyntaxException | IOException e) {
					e.printStackTrace();
				}
			};
		IntStream.rangeClosed(start, end).parallel().forEach(pageDownloader);
	}
	
	private String getPageUrl(int page) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(mainURL);
		builder.addParameter("table", "bg");
		builder.addParameter("view", "M");
		builder.addParameter("recpoint", Integer.toString(page*10));
		builder.addParameter("sort", "1");
		builder.addParameter("order", "R");		
		return builder.toString();
	}
	
	private void downloadFilesInURL(String url) throws IOException {
		System.out.println("Downloading files from: "+ url);
		Elements game_links = retrieveGameLinks(url);
		game_links.stream().forEach(this::downloadFilesInLink);
//		for (Element link : game_links) {
//			downloadFilesInLink(link);
//		}
	}
	
	private void downloadFilesInLink(Element link) {
		try {
		String file = retrieveGameFileFromLink(link);
		Path path = Paths.get(directory, urlToFile(file));
		URL file_url = new URL(file);
		ReadableByteChannel rbc = Channels.newChannel(file_url.openStream());
		FileOutputStream fos = new FileOutputStream(path.toString());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private String urlToFile(String url) {
		return url.replaceAll("http://itikawa.com/kifdb/bg/bin/", "");
	}
	
	private String retrieveGameFileFromLink(Element link) throws IOException {
		Document doc = Jsoup.connect(link.absUrl("href").toString()).get();
		Elements links = doc.select("a[href]");
		return links.stream()	
			.map(l -> l.absUrl("href"))
			.filter(url -> url.startsWith("http://itikawa.com/kifdb/bg/bin/"))
			.findFirst()
			.get();
	}
	
	private Elements retrieveGameLinks(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
//		Elements game_links = new Elements();
		return links
			.stream()
			.filter(a -> a.absUrl("href").startsWith("http://itikawa.com/bgrPHP/"))
			.collect(Elements::new, Elements::add, Elements::addAll);
//		return game_links;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
}