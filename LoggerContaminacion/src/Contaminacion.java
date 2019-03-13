import java.util.*;


public class Contaminacion extends Canonical<String> {

	public List<IntensidadTrafico> tramosTraficoContaminante;
	
	public Contaminacion(String tramo, float latitud, float longitud, String lectura) {
		super(tramo, latitud, longitud, lectura);
		tramosTraficoContaminante = new ArrayList<IntensidadTrafico>();
	}
	
	public int getTotalVehiculos() {
		int total = 0;
		for (int i = 0; i < tramosTraficoContaminante.size(); i++) {
			total += tramosTraficoContaminante.get(i).getLectura();
		}
		return total;
	}
}
