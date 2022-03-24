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
        assertEquals(country.population, 1013662000);
    }

    @Test
    void testGetCity()
    {
        City city = app.allCities().get(1);
        assertEquals(city.Name, "Mumbai (Bombay)");
    }

    @Test
    void testGetCapitalCity()
    {
        City city = app.allCapitalCities().get(1);
        assertEquals(city.Name, "Seoul");
    }
}