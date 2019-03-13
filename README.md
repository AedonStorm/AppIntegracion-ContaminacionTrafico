<h1>Problema a Resolver</h1>
Se pretende integrar un servicio que recoja datos en tiempo real sobre la intensidad del tráfico por tramos, para analizar en distintos puntos de Valencia la influencia de los vehículos en circulación sobre la contaminación ambiental.

<h1>Objetivos</h1>
La solución de integración recogerá datos sobre el número de coches/hora mediante espiras electromagnéticas y distintos tipos de emisiones gracias a medidores ambientales. Los datos se cotejarán para ofrecer un análisis por zonas de la influencia de los vehículos en la contaminación de la ciudad.

Adicionalmente, se cotejará la climatología diaria para comprobar si ha influido en los datos.

<h1>Restricciones de Diseño y Riesgos</h1>
Para el desarrollo de este proyecto se necesitan los recursos de la REST API que ofrece  gobiernoabierto.valencia sobre sus dispositivos repartidos por la ciudad que ofrecen datos de intensidad de tráfico y contaminación ambiental.

Estos recursos se sirven en distintos formatos de datos: CSV, geoJSON, XML. Por lo que imponen unas restricciones tecnológicas para usar estás soluciones.

De forma complementaría mi solución debería integrarse con la REST API de AEMET para obtener los datos sobre la climatología diaria, lamentablemente no he conseguido integrarme del todo ya que su REST API es bastante compleja por motivos de seguridad.
 

<h1>Diagrama</h1>

![alt text](https://github.com/AedonStorm/AppIntegracion-ContaminacionTrafico/blob/master/GitImages/Diagrama%20sin%20t%C3%ADtulo.png)
 
<h1>Solución</h1>

<h2>Conector IntensidadTrafico</h2>

Se conecta a la REST API con la url: http://apigobiernoabiertortod.valencia.es/apirtod/rest/datasets/intensidad_tramos.xml?items=389
Obtiene el XML y lo manda al Exchange con el topic IntensidadTraficoXml.


<h2>Conector Contaminación</h2>

Se conecta a la REST API con la url: http://mapas.valencia.es/lanzadera/opendata/Estautomaticas/JSON
Obtiene el JSON y lo manda al Exchange con el topic contaminacionJson.

 

<h2>Transformadores</h2>

Ambos transformadores trabajan con parsers para acabar construyendo un JSON con un formato canónico.

<h2>Logger y analizador</h2>

El Logger crea un objeto de tipo Analizador, el cual tiene dos listas, una para objetos IntensidadTrafico y otra para Contaminacion.

Las clases IntensidadTrafico y Contaminación heredan de Canonico, ya que comparten la mayoría de variables.

Cuando llegan los mensajes canónicos, este construye los objetos según el tipo y al acabar intenta Analizar. Una vez las listas tengan contenido comenzará a analizar. Asignando según la distancia, cada tramo de tráfico a una de las zonas donde se mide la contaminación.

<h1>Resultado</h1>

![alt text](https://github.com/AedonStorm/AppIntegracion-ContaminacionTrafico/blob/master/GitImages/contaminacion.JPG)

