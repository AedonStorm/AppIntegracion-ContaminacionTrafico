
public class IntensidadTrafico extends Canonical<Integer> {
	
	Contaminacion zonaContaminacion;
	float currentDistance = Float.POSITIVE_INFINITY;
	
	public IntensidadTrafico(String tramo, float latitud, float longitud, int lectura) {
		super(tramo, latitud, longitud, lectura);
	}
	
	public void setZonaContaminacion(Contaminacion zona) {
		float zonaDistancia = distance(zona.getLatitud(), zona.getLongitud()); 
		if ( zonaDistancia < currentDistance )  {
			currentDistance = zonaDistancia;
			zonaContaminacion = zona;
		}
	}
}
