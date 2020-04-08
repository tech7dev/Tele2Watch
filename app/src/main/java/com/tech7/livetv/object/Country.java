package com.tech7.livetv.object;

public class Country {
    private String CountryCode;
    private String Country;

    public Country(){

    }

    public Country(String countryCode, String country) {
        CountryCode = countryCode;
        Country = country;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
