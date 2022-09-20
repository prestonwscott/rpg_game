public class CsvReadException extends Exception{
    private String data;

    public CsvReadException(String in){ data = in; }

    @Override

    public String toString(){
        return "CsvReadException: " + data;
    }
}
