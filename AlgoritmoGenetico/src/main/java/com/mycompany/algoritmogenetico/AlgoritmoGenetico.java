/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.algoritmogenetico;

/**
 *
 * @author Rolando
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {

    public static List<String> generarPoblacion(int tamanoPoblacion, int longitudGen) {
        List<String> poblacion = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < tamanoPoblacion; i++) {
            StringBuilder individuo = new StringBuilder();
            for (int j = 0; j < longitudGen; j++) {
                individuo.append(random.nextInt(2));
            }
            poblacion.add(individuo.toString());
        }
        return poblacion;
    }

    public static int evaluarAptitud(String individuo) {
        
        int count = 0;
        for (char bit : individuo.toCharArray()) {
            if (bit == '1') {
                count++;
            }
        }
        return count;
    }

    public static List<String> seleccionarPadres(List<String> poblacion, int cantidadPadres) {
        List<String> padres = new ArrayList<>(poblacion);
        Collections.sort(padres, (individuo1, individuo2) ->
                Integer.compare(evaluarAptitud(individuo2), evaluarAptitud(individuo1)));
        return padres.subList(0, cantidadPadres);
    }

    public static List<String> cruzar(String padre1, String padre2) {
        Random random = new Random();
        int puntoCruza = random.nextInt(padre1.length() - 1) + 1;
        String hijo1 = padre1.substring(0, puntoCruza) + padre2.substring(puntoCruza);
        String hijo2 = padre2.substring(0, puntoCruza) + padre1.substring(puntoCruza);
        List<String> descendencia = new ArrayList<>();
        descendencia.add(hijo1);
        descendencia.add(hijo2);
        return descendencia;
    }

    public static String mutar(String individuo, double tasaMutacion) {
        Random random = new Random();
        StringBuilder mutatedIndividuo = new StringBuilder();
        for (char bit : individuo.toCharArray()) {
            if (random.nextDouble() > tasaMutacion) {
                mutatedIndividuo.append(bit);
            } else {
                mutatedIndividuo.append((bit == '0') ? '1' : '0');
            }
        }
        return mutatedIndividuo.toString();
    }

    public static List<String> evolucionar(List<String> poblacion, int cantidadGeneraciones, double tasaMutacion, int cantidadPadres) {
        for (int generacion = 0; generacion < cantidadGeneraciones; generacion++) {
            List<String> padres = seleccionarPadres(poblacion, cantidadPadres);
            List<String> descendencia = new ArrayList<>();
            while (descendencia.size() < poblacion.size()) {
                List<String> padresSeleccionados = seleccionarPadres(padres, 2);
                List<String> hijos = cruzar(padresSeleccionados.get(0), padresSeleccionados.get(1));
                descendencia.add(mutar(hijos.get(0), tasaMutacion));
                descendencia.add(mutar(hijos.get(1), tasaMutacion));
            }
            poblacion = new ArrayList<>(descendencia);
            String mejorGen = Collections.max(poblacion, (individuo1, individuo2) ->
                    Integer.compare(evaluarAptitud(individuo1), evaluarAptitud(individuo2)));
            System.out.println("Generación " + (generacion + 1) + " - Mejor Gen: " + mejorGen +
                    " - Aptitud: " + evaluarAptitud(mejorGen));
        }
        return poblacion;
    }

    public static void main(String[] args) {
        int tamanoPoblacion = 10;
        int longitudGen = 7;
        int cantidadGeneraciones = 7;
        double tasaMutacion = 0.1;
        int cantidadPadres = 10;

        List<String> poblacion = generarPoblacion(tamanoPoblacion, longitudGen);
        List<String> poblacionFinal = evolucionar(poblacion, cantidadGeneraciones, tasaMutacion, cantidadPadres);

        String mejorGen = Collections.max(poblacionFinal, (individuo1, individuo2) ->
                Integer.compare(evaluarAptitud(individuo1), evaluarAptitud(individuo2)));
        String peorGen = Collections.min(poblacionFinal, (individuo1, individuo2) ->
                Integer.compare(evaluarAptitud(individuo1), evaluarAptitud(individuo2)));

        System.out.println("\nMejor Gen: " + mejorGen + " - Aptitud: " + evaluarAptitud(mejorGen));
        System.out.println("Peor Gen: " + peorGen + " - Aptitud: " + evaluarAptitud(peorGen));
    }
}
