package com.jarroba.scraping.Scraping_jarroba_java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class ScrapingOLE {

	// http://leer-comics.blogspot.com/

	
	// magos del humor
	// http://leer-comics.blogspot.com/search/label/Magos%20del%20Humor
	
	
	// COLECCIÓN-OLÉ
	public static final String url = "http://leer-comics.blogspot.com/2019/02/coleccion-ole.html";

//	public static final String url = "http://leer-comics.blogspot.com/2019/12/robin-hijo-de-batman-1-1-6-usa.html#more";
	public static final String base= "https://leer-comics.blogspot.com/";
	public static final String dir_base= "c:/COLECCIÓN-OLÉ";
	public static final int INICIO = 318;
	public static final int FIN = 450;
	
	public static void main(String args[]) {

		boolean generarPDF = true;

		
		// Compruebo si me da un 200 al hacer la petición
		if (getStatusConnectionCode(url) == 200) {

			// Obtengo el HTML de la web en un objeto Document
			Document documentParent = getHtmlDocument(url);

			// Busco todas las historias de meneame que estan dentro de:
			Elements entradas = documentParent.select("div.separator");
			Elements ahrefs = documentParent.getElementsByTag("a");
			try {

				
				int contador=0;
				for (Element elem : ahrefs) {
					contador++;
					String href_html = elem.getElementsByTag("a").attr("href");
					
					String titulo = elem.text();
					if (contador>INICIO && contador<FIN && !"".equals(href_html) && href_html.contains(".html") && href_html.contains("leer-comics.blogspot.com") && href_html.contains("coleccion-ole")) {
						String directorios[]=href_html.substring(base.length()).replaceAll(".html", "").replaceAll("coleccion-ole-", "").split("/");
						System.out.println(titulo + " : " + href_html + "\n");
						
						String name_dir = dir_base;
						
						for (int k=0;k<directorios.length-1;k++) {
							
							name_dir = name_dir + "/" + directorios[k];
							File archivo = new File(name_dir);
							if (!archivo.exists()) {
							    archivo.mkdirs();
							}
						}
	
						
						if (generarPDF) {

							String name_file = name_dir + "/" + directorios[directorios.length-1];
							
							Document document = getHtmlDocument(href_html);
							Elements ahrefs_comic = document.getElementsByTag("a");
							
							FileOutputStream fos = new FileOutputStream(name_file + ".pdf");
							com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
							PdfWriter pdfW = PdfWriter.getInstance(documento, fos);
							documento.open();

							// Paseo cada una de las entradas
							for (Element elemento : ahrefs_comic) {

								String href = elemento.getElementsByTag("a").attr("href");

								// Url con la foto
								if (!"".equals(href) && (href.contains(".jpg") || href.contains(".JPG")) && href.contains("bp.blogspot.com")) {

									System.out.println(href + "\n");

									URL miurl = new URL(href);


									Image imagen;
									try {
										imagen = Image.getInstance(miurl);
										System.out.println("heigth: " + imagen.getHeight() + " width: " + imagen.getWidth());
//								    imagen.scalePercent(50f);
										imagen.scaleToFit(530, 800);
//								    imagen.scaleAbsolute(540f, 800f);
										documento.add(imagen);

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}

							// cierre de conexion y fichero.
							documento.close();
							fos.close();
						}						
					
					}
					
					
				}
				
				
				
				
				
				

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));

	}

	/**
	 * Con esta método compruebo el Status code de la respuesta que recibo al hacer
	 * la petición EJM: 200 OK 300 Multiple Choices 301 Moved Permanently 305 Use
	 * Proxy 400 Bad Request 403 Forbidden 404 Not Found 500 Internal Server Error
	 * 502 Bad Gateway 503 Service Unavailable
	 * 
	 * @param url
	 * @return Status Code
	 */
	public static int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	/**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia
	 * JSoup
	 * 
	 * @param url
	 * @return Documento con el HTML
	 */
	public static Document getHtmlDocument(String url) {

		Document doc = null;

		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}

		return doc;

	}

}
