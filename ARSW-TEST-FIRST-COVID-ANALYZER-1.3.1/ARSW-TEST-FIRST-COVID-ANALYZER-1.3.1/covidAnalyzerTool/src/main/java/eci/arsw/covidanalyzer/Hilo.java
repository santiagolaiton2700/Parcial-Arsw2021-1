package eci.arsw.covidanalyzer;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Hilo extends Thread {
    private TestReader testReader;
    public ResultAnalyzer restulAnalyzer;
    private List<File> resultFiles;
    private int min = 0;
    private int max = 0;
    private AtomicInteger amountOfFilesProcessed;
    private int amountOfFilesTotal;
    public ResultAnalyzer resultAnalyzer;
    private boolean pausado=true;


    public Hilo(List<File> resultFiles, ResultAnalyzer resultAnalyzer, TestReader testReader) {
        amountOfFilesProcessed = new AtomicInteger();
        this.resultFiles = resultFiles;
        this.restulAnalyzer = resultAnalyzer;
        this.testReader = testReader;

    }

    public void setMin(int min) {
        this.min = min - 1;
    }

    public void setMax(int max) {
        this.max = max - 1;
    }

    public void run() {
        amountOfFilesProcessed.set(0);
        amountOfFilesTotal = resultFiles.size();
        System.out.println(min + " " + max);
        while(pausado) {
            for (int i = this.min; i <= this.max; i++) {
                List<Result> results = testReader.readResultsFromFile(resultFiles.get(i));
                System.out.println(i);
                for (Result r : results) {
                    System.out.println(r);
                    restulAnalyzer.addResult(r);
                }
            }

            amountOfFilesProcessed.incrementAndGet();
        }
    }
    public synchronized void pause() throws InterruptedException {
        if(pausado){
            pausado=!pausado;
            this.wait();
        }else{
            pausado=!pausado;
            this.notifyAll();
        }

    }
}




