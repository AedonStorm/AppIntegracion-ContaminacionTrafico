import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;
import com.opencsv.*;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

//Contaminacion
public class Transformador {
	private final static String NOMBRE_EXCHANGE = "exchange";
	private final static String DATA_TYPE = "contaminacionJson";
	private final static String NOMBRE_COLA_NOMINAL = "nominal";

	public static void main(String [ ] args) throws IOException, TimeoutException
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(NOMBRE_EXCHANGE, BuiltinExchangeType.TOPIC);
		String COLA_CONSUMER = channel.queueDeclare().getQueue();
		channel.queueBind(COLA_CONSUMER, NOMBRE_EXCHANGE, DATA_TYPE);
		
		channel.queueDeclare(NOMBRE_COLA_NOMINAL, false, false, false, null);
      
		Consumer consumer = new DefaultConsumer(channel) {
    	  @Override
    	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
    		 String message = new String(body, "UTF-8");
    		 //System.out.println("Recibido: '"+ message +"'");
    		 JSONArray json = dataTransform(message);
   	      	 channel.basicPublish("", NOMBRE_COLA_NOMINAL, null, json.toString().getBytes());
    	  }
		};
		channel.basicConsume(COLA_CONSUMER, true, consumer);
	}
	
	private static JSONArray dataTransform(String data) {
		JSONArray jsonList;
		try {
			jsonList = new JSONArray();
			JSONObject obj = new JSONObject(data);
			JSONArray jsonArrayFeatures = obj.getJSONArray("features");
			
			for (int i = 0; i < jsonArrayFeatures.length(); i++) {
				JSONObject json = new JSONObject();
				JSONObject properties = jsonArrayFeatures.getJSONObject(i).getJSONObject("properties");
				JSONObject geometry = jsonArrayFeatures.getJSONObject(i).getJSONObject("geometry");
				
				json.put("tramo", properties.get("nombre"));
				
				json.put("tipo", "Datos de contaminacion");
				JSONArray coordinates = geometry.getJSONArray("coordinates");
				json.put("latitud", coordinates.get(0).toString());
				json.put("longitud", coordinates.get(1).toString());

				String medicionesUrl = (String) properties.get("mediciones");
				//System.out.println(medicionesUrl);
				String lectura = getMediciones(medicionesUrl);
				System.out.println(lectura);
				json.put("lectura", lectura);
				
				jsonList.put(json);
			}
			return jsonList;
		} catch (Exception e ) {e.printStackTrace(); return null;}
	}
	
	private static String getMediciones(String url) throws IOException {
		 
		  String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
		  //String authorization = "";
		  
		  URL urlObject = new URL(url);
		  HttpURLConnection httpConnection = (HttpURLConnection) urlObject.openConnection();
		  httpConnection.setRequestMethod("GET");
		  httpConnection.setRequestProperty("user-agent", userAgent);
		  //connection.setRequestProperty("authorization", authorization);
		  
		  BufferedReader buffer = new BufferedReader( new InputStreamReader (httpConnection.getInputStream()));
		  String inputLine;
		  String lastLine = "";
		  
		  while ((inputLine = buffer.readLine()) != null) {
			  lastLine = inputLine;
		  }
		  
		  buffer.close();
		  
		  return lastLine;
	}
}