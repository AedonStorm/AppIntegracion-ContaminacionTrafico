import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//Intensidad del Tr�fico - Tipo: XML
public class Conector {
	private final static String NOMBRE_EXCHANGE = "exchange";
	private final static String DATA_TYPE = "IntensidadTraficoXml";

	public static void main(String [ ] args) throws IOException, TimeoutException
	{
		  String url = "http://apigobiernoabiertortod.valencia.es/apirtod/rest/datasets/intensidad_tramos.xml?items=389";
		  String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
		  //String authorization = "";
		  
		  URL urlObject = new URL(url);
		  HttpURLConnection httpConnection = (HttpURLConnection) urlObject.openConnection();
		  httpConnection.setRequestMethod("GET");
		  httpConnection.setRequestProperty("user-agent", userAgent);
		  //connection.setRequestProperty("authorization", authorization);
		  
		  BufferedReader buffer = new BufferedReader( new InputStreamReader (httpConnection.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  
		  while ((inputLine = buffer.readLine()) != null) {
			  response.append(inputLine);
		  }
		  
		  buffer.close();
		  System.out.println(response.toString());
		  
		  
		  //EXCHANGE
	      ConnectionFactory factory = new ConnectionFactory();
	      factory.setHost("localhost");
	      Connection connection = factory.newConnection();
	      Channel channel = connection.createChannel();
	      
	      channel.exchangeDeclare(NOMBRE_EXCHANGE, BuiltinExchangeType.TOPIC);
	     
	      String message = response.toString();
	      channel.basicPublish(NOMBRE_EXCHANGE, DATA_TYPE, null, message.getBytes());
	      
	      channel.close();
	      connection.close();
	}
}
