package com.mark.interview.payroll.data.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mark.interview.payroll.util.DateUtils;
import com.mark.interview.payroll.model.TaxBracket;
import com.mark.interview.payroll.model.TaxYearInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mark Cunningham on 9/24/2016.
 * <br>Extension class of the {@link JsonFileReader} to read the {@link TaxYearInformation} from the files in this application
 */
public class TaxInformationJsonFileReader extends JsonFileReader<TaxYearInformation, TaxInformationJsonFileReader.TaxYear> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxInformationJsonFileReader.class);
    private final static String TAX_RATE_FOLDER = "tax-years";
    private final static Set<String> TAX_RATE_JSON_FILES = new HashSet<>();
    private Set<TaxYearInformation> cacheOfAllTaxYearInformation;

    static {
        /**
         * The set of supported tax years - more years can be added by adding more json files to the resources folder
         * Note: this would ideally come from a DB or API call; but for this example, choose to have it packaged with the app
         */
        TAX_RATE_JSON_FILES.add(TAX_RATE_FOLDER+"/2013.json"); // 2013 tax rates
    }

    public TaxInformationJsonFileReader() {
        super(TaxYear.class);
    }

    /**
     * This method returns all the tax years that are supported/configured for this application (in json)
     * @return - the set of Tax years found in this application - Note, can be empty
     */
    public Set<TaxYearInformation> getAll() {
        if ( cacheOfAllTaxYearInformation != null ) {
            return cacheOfAllTaxYearInformation;
        }
        Set<TaxYearInformation> allTaxYears = new HashSet<>();
        for ( String taxFileToRead : TAX_RATE_JSON_FILES) {
            DataReaderResult<TaxYearInformation> taxRateReadResult = super.readResource(taxFileToRead);
            if ( taxRateReadResult != null && taxRateReadResult.hasData() && taxRateReadResult.getData().size() == 1) {
                allTaxYears.addAll(taxRateReadResult.getData());
            } else {
                LOGGER.error("No data was loaded for the tax file [{}]", taxFileToRead);
            }
        }
        this.cacheOfAllTaxYearInformation = Collections.unmodifiableSet(allTaxYears);
        return this.cacheOfAllTaxYearInformation;
    }

    @Override // We are given the Json object, so convert it to the POJO
    protected TaxYearInformation process(TaxYear parsedTaxYear) {
        if ( parsedTaxYear != null && parsedTaxYear.getTaxBrackets() != null) {
            LocalDate taxYearStart = DateUtils.tryParseDateFromString(parsedTaxYear.taxYearStart);
            LocalDate taxYearEnd = DateUtils.tryParseDateFromString(parsedTaxYear.taxYearEnd);
            Set<TaxBracket> taxRanges = parseInputTaxBrackets(parsedTaxYear.taxBrackets);
            return new TaxYearInformation(taxYearStart, taxYearEnd, taxRanges);
        }
        return null;
    }

    /**
     * Parses all the given JSON brackets into the POJO brackets
     * @param inputTaxBrackets - JSON brackets
     * @return - POJO brackets
     */
    private Set<TaxBracket> parseInputTaxBrackets(List<TaxYear.TaxBracket> inputTaxBrackets) {
        Set<TaxBracket> taxBrackets = new HashSet<>();
        inputTaxBrackets.forEach(inputTaxBracket -> {
            if (inputTaxBracket.getMinimumSalary() != null && inputTaxBracket.getSurplusTaxRatePerDollar() != null
                    && inputTaxBracket.getSurplusTaxMinimumSalary() != null && inputTaxBracket.getTaxBaseAmount() != null) {
                TaxBracket taxBracket = TaxBracket.builder()
                        .withMinimumSalary(inputTaxBracket.getMinimumSalary())
                        .withMaximumSalary(inputTaxBracket.getMaximumSalary())
                        .withTaxBaseAmount(inputTaxBracket.getTaxBaseAmount())
                        .withSurplusTaxMinimumSalary(inputTaxBracket.getSurplusTaxMinimumSalary())
                        .withSurplusTaxRatePerDollar(inputTaxBracket.getSurplusTaxRatePerDollar())
                        .build();
                taxBrackets.add(taxBracket);
            }
        });
        return taxBrackets;
    }

    /**
     * This Tax year will match the representation of the JSON files in this application.
     * Note, the POJO {@link TaxYearInformation} was not used here since I did not want to bleed JSON properties into the POJO
     * and possible have to make it mutable too - this separates the two concerns and later, if this information was
     * read from a database, then the POJO {@link TaxYearInformation} would not need to change
     */
    static class TaxYear {
        @JsonProperty("tax_year_start")
        private String taxYearStart;
        @JsonProperty("tax_year_end")
        private String taxYearEnd;
        @JsonProperty("tax_brackets")
        private List<TaxBracket> taxBrackets;

        public List<TaxBracket> getTaxBrackets() {
            return taxBrackets;
        }

        public void setTaxBrackets(List<TaxBracket> taxBrackets) {
            this.taxBrackets = taxBrackets;
        }

        public String getTaxYearStart() {
            return taxYearStart;
        }

        public void setTaxYearStart(String taxYearStart) {
            this.taxYearStart = taxYearStart;
        }

        public String getTaxYearEnd() {
            return taxYearEnd;
        }

        public void setTaxYearEnd(String taxYearEnd) {
            this.taxYearEnd = taxYearEnd;
        }

        static class TaxBracket {
            @JsonProperty("minimum_salary")
            private BigDecimal minimumSalary;
            @JsonProperty("maximum_salary")
            private BigDecimal maximumSalary;
            @JsonProperty("tax_base_amount")
            private BigDecimal taxBaseAmount;
            @JsonProperty("surplus_tax_minimum_salary")
            private BigDecimal surplusTaxMinimumSalary;
            @JsonProperty("surplus_tax_rate_per_dollar")
            private BigDecimal surplusTaxRatePerDollar;

            public BigDecimal getMinimumSalary() {
                return minimumSalary;
            }

            public void setMinimumSalary(BigDecimal minimumSalary) {
                this.minimumSalary = minimumSalary;
            }

            public BigDecimal getMaximumSalary() {
                return maximumSalary;
            }

            public void setMaximumSalary(BigDecimal maximumSalary) {
                this.maximumSalary = maximumSalary;
            }

            public BigDecimal getTaxBaseAmount() {
                return taxBaseAmount;
            }

            public void setTaxBaseAmount(BigDecimal taxBaseAmount) {
                this.taxBaseAmount = taxBaseAmount;
            }

            public BigDecimal getSurplusTaxMinimumSalary() {
                return surplusTaxMinimumSalary;
            }

            public void setSurplusTaxMinimumSalary(BigDecimal surplusTaxMinimumSalary) {
                this.surplusTaxMinimumSalary = surplusTaxMinimumSalary;
            }

            public BigDecimal getSurplusTaxRatePerDollar() {
                return surplusTaxRatePerDollar;
            }

            public void setSurplusTaxRatePerDollar(BigDecimal surplusTaxRatePerDollar) {
                this.surplusTaxRatePerDollar = surplusTaxRatePerDollar;
            }
        }

    }


}
