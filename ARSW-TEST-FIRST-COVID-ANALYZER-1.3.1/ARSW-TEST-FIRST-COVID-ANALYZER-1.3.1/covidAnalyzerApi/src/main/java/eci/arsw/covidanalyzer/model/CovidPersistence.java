package eci.arsw.covidanalyzer.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("covidPersistence")

public class CovidPersistence {
    private HashMap<ResultType,Result>resultados=new HashMap<ResultType,Result>();

    public Boolean aggregateResult(Result result, ResultType type) {
        try{
            resultados.put(type,result);
            return true;
        }catch (Exception e){
            return false;
        }


    }
    public Result getResult(ResultType type){
        return resultados.get(type);
    }

}
