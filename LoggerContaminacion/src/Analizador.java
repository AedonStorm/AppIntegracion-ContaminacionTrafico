import java.util.*;

public class Analizador {
	List<IntensidadTrafico> traficoList;
	List<Contaminacion> contaminacionList;
	
	public Analizador() {
		traficoList = new ArrayList<IntensidadTrafico>();
		contaminacionList = new ArrayList<Contaminacion>();
	}
	
	public void Analizar() {
		System.out.println("Analizar");
		if (traficoList.isEmpty() || contaminacionList.isEmpty()) return;
		System.out.println("Analizando...");
		
		for (int i = 0; i < traficoList.size(); i++) {}
		traficoList.forEach( zonaTrafico -> {
			System.out.println("zonaTrafico... " + zonaTrafico.getTramo());
			contaminacionList.forEach( zonaContaminacion -> {
				zonaTrafico.setZonaContaminacion(zonaContaminacion);
			});
			zonaTrafico.zonaContaminacion.tramosTraficoContaminante.add(zonaTrafico);
		});
		
		String[] nombresLectura = "Fecha;PM2,5(�g/m�);SO2(�g/m�);CO(mg/m�);NO(�g/m�);NO2(�g/m�);PM10(�g/m�);NOx(�g/m�);Ozono(�g/m�);Veloc.(m/s)".split(";");
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("___________________________________________________________");
		contaminacionList.forEach( contaminacion -> {
			String[] lectura = contaminacion.getLectura().split(";");
			System.out.println("Zona: " + contaminacion.getTramo());
			System.out.println("Lectura");
			for( int i = 0; i < nombresLectura.length - 2; i++) {
				System.out.println("	"+ nombresLectura[i] + ": " + lectura[i]);
			}
			
			System.out.println("N�mero de vehiculos que han pasado por esta zona: " + contaminacion.getTotalVehiculos());
			System.out.println("___________________________________________________________");
		});
	}
}
