
public class Canonical<T> {
	
	private String tramo;
	private float latitud;
	private float longitud;
	private T lectura;
	
	public Canonical(String tramo, float latitud, float longitud, T lectura) {
		this.tramo = tramo;
		this.latitud = latitud;
		this.longitud = longitud;
		this.lectura = lectura;
	}
	
	public String getTramo() { return tramo; }
	public float getLatitud() { return latitud; }
	public float getLongitud() { return longitud; }
	public T getLectura() { return lectura; }
	
	public float distance(float latitud, float logitud) {
		float a = this.latitud - latitud;
		float b = this. longitud - longitud;
		
		return Math.abs(a+b);
	}
}
