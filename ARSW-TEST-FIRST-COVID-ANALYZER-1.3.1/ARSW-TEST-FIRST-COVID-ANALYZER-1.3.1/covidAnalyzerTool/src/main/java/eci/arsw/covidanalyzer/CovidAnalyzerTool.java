package eci.arsw.covidanalyzer;

import com.sun.deploy.util.BlackList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Camel Application
 */
public class CovidAnalyzerTool {


    private static ResultAnalyzer resultAnalyzer;
    private static TestReader testReader;
    private static ArrayList<Hilo> listahilos;
    private int amountOfFilesTotal;
    private static AtomicInteger amountOfFilesProcessed;


    public CovidAnalyzerTool() {
        resultAnalyzer = new ResultAnalyzer();
        testReader = new TestReader();
        amountOfFilesProcessed = new AtomicInteger();
        listahilos=new ArrayList<Hilo>();
    }

    public void processResultData() {
        amountOfFilesProcessed.set(0);
        List<File> resultFiles = getResultFileList();
        amountOfFilesTotal = resultFiles.size();
        for (File resultFile : resultFiles) {
            List<Result> results = testReader.readResultsFromFile(resultFile);
            for (Result result : results) {
                resultAnalyzer.addResult(result);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    private static List<File> getResultFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }


    public Set<Result> getPositivePeople() {
        return resultAnalyzer.listOfPositivePeople();
    }


    public static void primerPunto(CovidAnalyzerTool covidAnalyzerTool){
        amountOfFilesProcessed.set(0);
        List<File> resultFiles = getResultFileList();
        int numHilos=5;
        int resi=resultFiles.size()/numHilos;
        int hilos=0;
        int min = 0,max=0;
        for(int i=1;i<resultFiles.size()+1;i++){
            if(min==0){
                min=i;
            }
            if (i%resi==0&&hilos<numHilos-1){
                System.out.println(min);
                hilos+=1;
                max=i;
                Hilo nuevo= new Hilo(resultFiles,resultAnalyzer,testReader);
                nuevo.setMin(min);
                nuevo.setMax(max);
                nuevo.start();
                listahilos.add(nuevo);
                //System.out.println("nuevo hilo numero: "+numHilos+" con min= "+min+" y max= "+max);
                min=0;

            }
            if(hilos==numHilos-1&&i==resultFiles.size()){
                max=i;
                Hilo nuevo= new Hilo(resultFiles,resultAnalyzer,testReader);
                nuevo.setMin(min);
                nuevo.setMax(max);
                nuevo.start();
                listahilos.add(nuevo);
                //System.out.println("nuevo hilo numero: "+numHilos+" con min= "+min+" y max= "+max);
                min=0;

            }
        }
        /**
        for(int i=0;i<listahilos.size();i++){
            listahilos.get(i).start();
        }
         **/


    }

    /**
     * A main() so we can easily run these routing rules in our IDE
     */

    public static void main(String... args) throws InterruptedException {
        CovidAnalyzerTool covidAnalyzerTool = new CovidAnalyzerTool();
        //Thread processingThread = new Thread(() -> covidAnalyzerTool.processResultData());
        //processingThread.start();
        primerPunto(covidAnalyzerTool);
        while (true) {

            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.contains(""))
                for(Hilo p: listahilos){
                        p.pause();
                }
            if (line.contains("exit"))
                break;
            String message = "Processed %d out of %d files.\nFound %d positive people:\n%s";
            Set<Result> positivePeople = covidAnalyzerTool.getPositivePeople();
            System.out.println(positivePeople.size());
            String affectedPeople = positivePeople.stream().map(Result::toString).reduce("", (s1, s2) -> s1 + "\n" + s2);
            message = String.format(message, covidAnalyzerTool.amountOfFilesProcessed.get(), covidAnalyzerTool.amountOfFilesTotal, positivePeople.size(), affectedPeople);
            System.out.println(message);
        }
    }

}

