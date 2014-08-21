package com.elance.utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class HtmlSaver {

	private static final String SOURCES_DIR_NAME = "/sources"; 
	
	private String url;
	private String domainName;
	private String rootUrl;
	private String sourcesFolder;
	private String htmlSourceFilePath;
	private String outputFolder;
	
	public HtmlSaver(String outputFileName, String url, String outputFolder){
		this.url = url;
		domainName = getDomainName();
		rootUrl = "http://" + domainName;
		this.outputFolder = String.format("%s/%s", outputFolder,outputFileName);
		createOutputFolder(this.outputFolder);
		sourcesFolder = this.outputFolder+"/"+SOURCES_DIR_NAME;
		createOutputFolder(sourcesFolder);
		htmlSourceFilePath = String.format("%s/%s.%s", this.outputFolder, domainName, "html");
	}
	
	public  String getDomainName(){
		try {
			return new URI(url).getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void saveHTML()
			throws IOException, URISyntaxException {

		Document document = Jsoup.connect(url).get();
		saveSourceImages(document);
		saveSourceCSSFiles(document);
		saveDOM(document, htmlSourceFilePath);
	}
	
	private void saveSourceCSSFiles(Document document){
		Elements elements = document.select("link");
		for (Element element : elements) {
			String rel = element.attr("rel");
			if(rel.equalsIgnoreCase("stylesheet")){
				String savedCssFilePath = saveSourceCSSFile(element);
				element.attr("href", savedCssFilePath);
			}
		}
	}
	
	private String saveSourceCSSFile(Element element){
		try {
			String urlPath = url+"/"+element.attr("href").toString();
			URL cssURL = new URL(urlPath);
			try {
				
				int index1 = cssURL.toString().lastIndexOf('?');
				int index2 = urlPath.length();
				int cssUrlValueLastIndex = (index1 < 0) ? index2 : index1;
				String cssUrlValue = urlPath.substring(urlPath.lastIndexOf('/') + 1,
						cssUrlValueLastIndex);

				FileUtils.copyURLToFile(cssURL, new File(generateSourseLocalFilePath(cssUrlValue)));
				return generateSrcForHtml(cssUrlValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private String generateSourseLocalFilePath(String fileName){
		return sourcesFolder+"/"+fileName;
	}
	
	private String generateSrcForHtml(String fileName){
		return "./"+SOURCES_DIR_NAME+"/"+fileName;
	}
	
	private void saveSourceImages(Document document){
		Elements elements = document.select("img");
		for (Element element : elements) {
			saveSourceImage(element);
		}
	}
	
	private  void saveSourceImage(Element element){
		String src = element.attr("src");
		String imgUrl = src;
		if (!imgUrl.startsWith("http:")) {
			imgUrl = rootUrl + "/" + imgUrl;
		}
		String savedImageName = ImageSaver.save(imgUrl, sourcesFolder);
		element.attr("src", generateSrcForHtml(savedImageName));
	}
	
	private  void saveDOM(Document document, String filePath) throws IOException{
		BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
		htmlWriter.write(document.toString());
		htmlWriter.close();
	}

	private  void createOutputFolder(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
			file.getPath();
		}
	}

	private static class ImageSaver {

		public static  String save(String url, String outputDir) {
			int index1 = url.lastIndexOf('?');
			int index2 = url.length();
			int imgNameLastIndex = (index1 < 0) ? index2 : index1;
			String imgName = url.substring(url.lastIndexOf('/') + 1,
					imgNameLastIndex);

			String savedImgName = imgName;
			try {
				saveImage(url, outputDir + "/" + savedImgName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return savedImgName;
		}

		private static  void saveImage(String imageUrl, String destinationFile)
				throws IOException {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);
			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		}

	}
	
//	public static void main(String[] args) throws Exception {
//		HtmlSaver saver = new HtmlSaver("http://google.com",".");
//		saver.saveHTML();
//	}
}
