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
		
		String[] nombresLectura = "Fecha;PM2,5(µg/m³);SO2(µg/m³);CO(mg/m³);NO(µg/m³);NO2(µg/m³);PM10(µg/m³);NOx(µg/m³);Ozono(µg/m³);Veloc.(m/s)".split(";");
		
		
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
			
			System.out.println("Número de vehiculos que han pasado por esta zona: " + contaminacion.getTotalVehiculos());
			System.out.println("___________________________________________________________");
		});
	}
}
