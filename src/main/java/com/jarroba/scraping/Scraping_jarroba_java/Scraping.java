package com.jarroba.scraping.Scraping_jarroba_java;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Scraping {
	
	//http://leer-comics.blogspot.com/
	
	
	public static final String url = "http://leer-comics.blogspot.com/2019/02/coleccion-ole-1-mortadelo-y-filemon.html";

//	public static final String url = "http://leer-comics.blogspot.com/2019/12/robin-hijo-de-batman-1-1-6-usa.html#more";

	public static void main(String args[]) {

		// Compruebo si me da un 200 al hacer la petición
		if (getStatusConnectionCode(url) == 200) {

			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);

			// Busco todas las historias de meneame que estan dentro de:
//			Elements entradas = document.select("div.col-md-4.col-xs-12").not("div.col-md-offset-2.col-md-4.col-xs-12");
//			System.out.println("Número de entradas en la página inicial de Jarroba: "+entradas.size()+"\n");
			Elements entradas = document.select("div.separator");
			Elements ahrefs = document.getElementsByTag("a");
//			documentoProcesado += "\n"+articulo.select("p.style-name span:nth-child(2)").text()
//				    +" -- "+articulo.getElementsByTag("a").attr("href");
			try {
//				FileOutputStream fos = new FileOutputStream("c:/prueba/foto.jpg");
				
				
				FileOutputStream fos = new FileOutputStream("c:/prueba/miPDF.pdf");
				com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
			    PdfWriter pdfW = PdfWriter.getInstance(documento, fos);
//			    pdfW.setInitialLeading(20);
			    documento.open();

				InputStream is = null;
				String documentoProcesado = "";
				

				// Paseo cada una de las entradas
				for (Element elem : ahrefs) {

//				String titulo = elem.getElementsByClass("tituloPost").text();
//				String autor = elem.getElementsByClass("autor").toString();
//				String fecha = elem.getElementsByClass("fecha").text();

					String href = elem.getElementsByTag("a").attr("href");

//				String href = elem.getElementsByTag("a").attr("href");
//				documentoProcesado += "\n" + href;


					// Con el método "text()" obtengo el contenido que hay dentro de las etiquetas
					// HTML
					// Con el método "toString()" obtengo todo el HTML con etiquetas incluidas

					// Url con la foto
					if (!"".equals(href) && href.contains(".jpg") && href.contains("bp.blogspot.com")) {
						
						System.out.println(href + "\n");
						
						
					
						
						URL miurl = new URL(href);
						
/* CREAR IMAGEN EN EL EQUIPO IMAGEN 	
						// establecemos conexion
						URLConnection urlCon = url.openConnection();

						// Sacamos por pantalla el tipo de fichero
						System.out.println(urlCon.getContentType());

						// Se obtiene el inputStream de la foto web y se abre el fichero
						// local.
						is = urlCon.getInputStream();

						// Lectura de la foto de la web y escritura en fichero local
						byte[] array = new byte[1000]; // buffer temporal de lectura.
						int leido = is.read(array);
						while (leido > 0) {
							fos.write(array, 0, leido);
							leido = is.read(array);
						}
						
						
*/						
						
						
//						String escritorio = System.getProperty("user.home") + "/Desktop/miPDF.pdf";
//						FileOutputStream fos;
						
						Image imagen;
						try {
//						    fos = new FileOutputStream(escritorio);


						    imagen = Image.getInstance(miurl);
						    System.out.println("heigth: " + imagen.getHeight() + " width: " + imagen.getWidth());
//						    imagen.scalePercent(50f);
						    imagen.scaleToFit(530, 800);
//						    imagen.scaleAbsolute(540f, 800f);
						    documento.add(imagen);

						} catch (Exception e) {
						    e.printStackTrace();
						}
						
						
						
						

					}
				}
				
				// cierre de conexion y fichero.
//				is.close();

			    documento.close();
				fos.close();
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
