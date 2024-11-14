package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.cglib.core.Local;

import javax.sound.midi.Sequence;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado=new Scanner(System.in);
    private ConsumoAPI consumoAPI =new ConsumoAPI();
    private final String URL_BASE="http://www.omdbapi.com/?t=";
    private final String API_KEY="&apikey=1a58976d";
    private ConvierteDatos conversor=new ConvierteDatos();
    private List<DatosSerie> datosSeries=new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio=repository;
    }

//
//    public void muestraElMenu(){
//        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
//        //Busca los datos generales de las series
//        var nombreSerie=teclado.nextLine();
//        var json=consumoAPI.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+API_KEY);
//        var datos=conversor.obtenerDatos(json, DatosSerie.class);
//      //  System.out.println(json);
//
//
//        //Busca los datos de todas las temporadas
//        List<DatosTemporadas> datosTemporadasList=new ArrayList<>();
//        for (int i = 1; i < datos.totalDeTemporadas(); i++) {
//            json=consumoAPI.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+"&Season="+i+API_KEY);
//            var datosTemporadas=conversor.obtenerDatos(json,DatosTemporadas.class);
//            datosTemporadasList.add(datosTemporadas);
//        }
//       // datosTemporadasList.forEach(System.out::println);
//
//        //Mostrar solo el titulo de los episodios para las temporadas
//
////        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
////
////            List<DatosEpisodio> episodiosTemporada=datosTemporadasList.get(i).episodios();
////            for (int j = 0; j < episodiosTemporada.size(); j++) {
////                System.out.println(episodiosTemporada.get(j).titulo());
////            }
////        }
//        //datosTemporadasList.forEach(t->t.episodios().forEach(e-> System.out.println(e.titulo())));
//
//        //Convertir todas las informaciones a  una lista del tipo DatosEpisodios
//
//        List<DatosEpisodio> datosEpisodios =datosTemporadasList.stream()
//                .flatMap(temporada->temporada.episodios().stream())
//                .collect(Collectors.toList());
//
//
//        //Top 5 episodios
//        System.out.println("Top 5 episodios");
//        datosEpisodios.stream()
//                .filter(e->!e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primer filtro (N/A) "+e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e-> System.out.println("Segundo filtro (De mayor a menor) "+e))
//                .map(e->e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Tercer filtro mayusculas"+e))
//                .limit(5)
//                .forEach(System.out::println);
//
//        //Convirtiendo los datos a una lista del tipo episodio
//
//        List<Episodio> episodios=datosTemporadasList.stream()
//                .flatMap(t->t.episodios().stream()
//                        .map(d->new Episodio(t.numero(), d)))
//                .collect(Collectors.toList());
//
//        episodios.forEach(episodio -> System.out.println(episodio));
//
//        //Busqyeda de episodios a partir de x año
//        System.out.println("Indica el año a partir del cual deseas ver los episodios");
//        var fecha =teclado.nextInt();
//        teclado.nextLine();
//
//        LocalDate fechaBusqueda= LocalDate.of(fecha,1,1);
//
//        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(episodio -> episodio.getFechaDeLanzamiento() !=null && episodio.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(episodio -> {
//                    System.out.println("Temporada "+episodio.getTemporada()+
//                            " Episodio "+episodio.getTitulo()+
//                            " Fecha de lanzamiento "+episodio.getFechaDeLanzamiento().format(dtf));
//                });
//
//        //Busca episodio por pedazo de titulo
//        System.out.println("Escriba el pedazo de titulo que desa ver");
//        var pedazoTitulo=teclado.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(titulo -> titulo.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son: "+episodioBuscado.get());
//        }else {
//            System.out.println("Episodio no encontrado");
//        }
//
//        Map<Integer,Double> evaluacionesPorTemporada=episodios.stream()
//                .filter(episodio -> episodio.getEvaluacion()>0.0)
//                .collect(Collectors.groupingBy(episodio -> episodio.getTemporada(),
//                        Collectors.averagingDouble(episodio->episodio.getEvaluacion())));
//        System.out.println(evaluacionesPorTemporada);
//        DoubleSummaryStatistics est=episodios.stream()
//                .filter(episodio -> episodio.getEvaluacion()>0.0)
//                .collect(Collectors.summarizingDouble(episodio->episodio.getEvaluacion()));
//        System.out.println("Media de las evaluaciones: "+est.getAverage());
//        System.out.println("Episodio Mejor evaluado: "+est.getMax());
//        System.out.println("Episodio Peor evaluado: "+est.getMin());
//
//
//    }

    //NUEVA VERSION
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por nombre
                    5 - Top 5 mejores series
                    6 - Buscar series por categoria
                    7 - Filtrar series por temporadas y evaluación
                    8 - Buscar episodios por titulo
                    9 - Buscar top 5 episodios
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarseriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriePorTemporadaYEvaluaion();
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        //DatosSerie datosSerie = getDatosSerie();
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie= series.stream()
                .filter(serie1 -> serie1.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada=serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios=temporadas.stream()
                    .flatMap(datoEpis->datoEpis.episodios().stream()
                            .map(episode->new Episodio(datoEpis.numero(),episode)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //datosSeries.add(datos);
        Serie serie=new Serie(datos);
        repositorio.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
       // List<Serie> series= repositorio.findAll();
         series= repositorio.findAll();
//        List<Serie> series= new ArrayList<>();
//        series =datosSeries.stream()
//                .map(d->new Serie(d))
//                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(serie -> serie.getGenero()))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas busar");
        var nombreSerie = teclado.nextLine();
        serieBuscada=repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: "+serieBuscada.get());
        }else{
            System.out.println("Serie no encontrada ");
        }
    }

    private void buscarTop5Series(){
        List<Serie> top5Series=repositorio.findTop5ByOrderByEvaluacionDesc();
        top5Series.forEach(s-> System.out.println("Titulo: "+s.getTitulo() + "Evaluación: "+s.getEvaluacion()));
    }

    private void buscarseriesPorCategoria(){
        System.out.println("Escriba el genero/categoria de la serie que desea buscar");
        var genero=teclado.nextLine();
        var categoria=Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria =repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria: "+genero+" son: ");
        seriePorCategoria.forEach(System.out::println);
    }

    public void filtrarSeriePorTemporadaYEvaluaion(){
        System.out.println("¿Filtrar series con cuantas temporadas? ");
        var totalTemporadas=teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con evaluacion a partir de cúal valor? ");
        var evaluacion =teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries= repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(serie-> System.out.println(serie.getTitulo()+" - evaluación: "+ serie.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas busccar");
        var nombreEpisodio=teclado.nextLine();
        List<Episodio> episodiosEncontrados =repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s \n",
                e.getSerie().getTitulo(),e.getTemporada(),e.getNumeroEpisodio(),e.getEvaluacion()));

      //  episodiosEncontrados.forEach(episode-> System.out.println(episode.getTitulo()+" "+episode.getEvaluacion()));
    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie=serieBuscada.get();
            List<Episodio> topEpisodios=repositorio.top5Episodios(serie);
            topEpisodios.forEach(e->
                    System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s \n",
                    e.getSerie().getTitulo(),e.getTemporada(),e.getTitulo(),e.getEvaluacion()));;
        }
    }

}
