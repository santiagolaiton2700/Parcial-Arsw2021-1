package eci.arsw.covidanalyzer.service;

import eci.arsw.covidanalyzer.model.CovidPersistence;
import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CovidService {
    @Autowired
    @Qualifier(value = "covidPersistence")
    CovidPersistence covidPersistence;

    /**
     * Add a new result into the specified result type storage.
     *
     * @param result
     * @param type
     * @return
     */
    public boolean aggregateResult(Result result, ResultType type){
        return covidPersistence.aggregateResult(result,type);
    }

    /**
     * Get all the results for the specified result type.
     *
     * @param type
     * @return
     */
    Result getResult(ResultType type) {
        return covidPersistence.getResult(type);
    }

    /**
     * @param id
     * @param type
     */
    void upsertPersonWithMultipleTests(UUID id, ResultType type) {

    }


}
