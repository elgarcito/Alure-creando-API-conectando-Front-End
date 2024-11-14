package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemplosStreams {
public void muestraEjemplo(){
    List<String> nombres= Arrays.asList("Brenda","Luis","Carlitos");
    nombres.stream()
            .sorted()
            .limit(2)
            .filter(n->n.startsWith("C"))
            .map(m->m.toUpperCase())
            .forEach(System.out::println);
}
}
