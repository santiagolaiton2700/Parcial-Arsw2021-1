package eci.arsw.covidanalyzer.model;

public class Result {
    private  int id;
    private ResultType tipoResultado;
    private Result result;

    public Result(int id,ResultType tipoResultado,Result result){
        this.id=id;
        this.tipoResultado=tipoResultado;
        this.result=result;
    }


    public ResultType getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(ResultType tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
