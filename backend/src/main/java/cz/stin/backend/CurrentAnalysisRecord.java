package cz.stin.backend;

import java.util.List;
import java.util.Map;

public class CurrentAnalysisRecord {

    public String source;
    public String base;

    public List<String> selectedCurrencies;

    public Map<String, Map<String, String>> calculations;

    public String strongestCurrency;
    public Double strongestValue;

    public String weakestCurrency;
    public Double weakestValue;
}