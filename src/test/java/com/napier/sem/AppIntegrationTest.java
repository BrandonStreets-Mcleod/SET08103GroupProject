package com.napier.sem;

import com.napier.groupproject.App;
import com.napier.groupproject.City;
import com.napier.groupproject.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);

    }

    @Test
    void testGetCountry()
    {
        Country country = app.populationOfCountries().get(1);
        assertEquals(country.population, 1013662000, "Population does not equal 1013662000");
    }

    @Test
    void testGetCountryInContinent()
    {
        Country country = app.populationOfCountriesInContinent("Asia").get(0);
        assertEquals(country.name, "China", "Country name does not equal China");
    }

    @Test
    void testGetCountryInRegion()
    {
        Country country = app.populationOfCountriesInRegion("North America").get(0);
        assertEquals(country.name, "United States", "Country name does not equal United States");
    }

    @Test
    void testGetCity()
    {
        City city = app.allCities().get(1);
        assertEquals(city.Name, "Seoul", "City does not equal Seoul");
    }

    @Test
    void testGetCityInContinent()
    {
        City city = app.allCitiesInAContinent("Asia").get(0);
        assertEquals(city.Name, "Mumbai (Bombay)","City does not equal Mumbai (Bombay)");
    }

    @Test
    void testGetCityInRegion()
    {
        City city = app.allCitiesInARegion("North America").get(0);
        assertEquals(city.Name, "New York","City does not equal New York");
    }

    @Test
    void testGetCityInCountry()
    {
        City city = app.allCitiesInACountry("United States").get(0);
        assertEquals(city.Name, "New York","City does not equal New York");
    }

    @Test
    void testGetCityInDistrict()
    {
        City city = app.allCitiesInADistrict("California").get(0);
        assertEquals(city.Name, "Los Angeles", "City does not equal Los Angeles");
    }

    @Test
    void testGetCapitalCity()
    {
        City city = app.allCapitalCities().get(1);
        assertEquals(city.Name, "Jakarta", "City does not equal Jakarta");
    }

    @Test
    void testGetCapitalCityInContinent()
    {
        City city = app.allCapitalCitiesInContinent("Asia").get(1);
        assertEquals(city.Name, "Jakarta", "City does not equal Jakarta");
    }

    @Test
    void testGetCapitalCityInRegion()
    {
        City city = app.allCapitalCitiesInRegion("North America").get(0);
        assertEquals(city.Name, "Washington", "City does not equal Washington");
    }
}