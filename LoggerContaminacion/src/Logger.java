import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Logger {
	private final static String NOMBRE_COLA_NOMINAL = "nominal";
	private static Analizador analizador;
	public static void main(String [ ] args) throws IOException, TimeoutException
	{
		analizador = new Analizador();
		
		ConnectionFactory factory = new ConnectionFactory();
	      factory.setHost("localhost");
	      Connection connection = factory.newConnection();
	      
	      Channel channel = connection.createChannel();
	      
	      channel.queueDeclare(NOMBRE_COLA_NOMINAL, false, false, false, null);
	      
	      Consumer consumer = new DefaultConsumer(channel) {
	    	
	    	  @Override
	    	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    		 String message = new String(body, "UTF-8");
	    		 JSONArray obj;
				try {
					obj = new JSONArray(message);
					LogReader(obj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
	      };
	      channel.basicConsume(NOMBRE_COLA_NOMINAL, true, consumer);
	}
	
	private static void LogReader(JSONArray jsonList) {
		//System.out.println(jsonList.toString());
		System.out.println("LogReader");
		for (int i = 0; i < jsonList.length(); i++) {
			try {
				
				String type = jsonList.getJSONObject(i).get("tipo").toString();
				String tramo = jsonList.getJSONObject(i).get("tramo").toString();
				float latitud = Float.parseFloat(jsonList.getJSONObject(i).get("latitud").toString());
				float longitud = Float.parseFloat(jsonList.getJSONObject(i).get("longitud").toString());
				
				switch (type) {
					case "Datos de contaminacion" :
						System.out.println(type);
						String lecturaContaminacion = jsonList.getJSONObject(i).get("lectura").toString();
						Contaminacion contaminacion =  new Contaminacion(tramo, latitud, longitud, lecturaContaminacion);
						analizador.contaminacionList.add(contaminacion);
						break;
					case "Intensidad Trafico Valencia":
						System.out.println(type);
						int lecturaTrafico = Integer.parseInt(jsonList.getJSONObject(i).get("lectura").toString());
						IntensidadTrafico intensidadTrafico =  new IntensidadTrafico(tramo, latitud, longitud, lecturaTrafico);
						analizador.traficoList.add(intensidadTrafico);
						break;
					default: 
						System.out.println("LogReader Switch Error");
						break;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		analizador.Analizar();
	}
}
