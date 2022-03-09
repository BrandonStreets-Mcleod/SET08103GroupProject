package com.napier.sem;

import com.napier.groupproject.App;
import com.napier.groupproject.City;
import com.napier.groupproject.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printCountiresTestNull()
    {
        app.printCountries(null);
    }

    @Test
    void printCountiresTestEmpty()
    {
        ArrayList<Country> countries = new ArrayList<>();
        app.printCountries(countries);
    }

    @Test
    void printCountiresTestContainsNull()
    {
        ArrayList<Country> countries = new ArrayList<>();
        countries.add(null);
        app.printCountries(countries);
    }

    @Test
    void printCountires()
    {
        ArrayList<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.code = "TST";
        country.name = "TEST";
        country.continent = "TEST";
        country.region = "TEST";
        country.area = 1;
        country.indepYear = 1;
        country.population = 1;
        country.lifeExpec = 1;
        country.GNP = 1;
        country.GNPOld = 1;
        country.localName = "TEST";
        country.governmentForm = "TEST";
        country.HeadOfState = "TEST";
        country.capital = 1;
        country.code2 = 1;
        countries.add(country);
        app.printCountries(countries);
    }

    @Test
    void printCityTestNull()
    {
        app.printCities(null);
    }

    @Test
    void printCityTestEmpty()
    {
        ArrayList<City> cities = new ArrayList<City>();
        app.printCities(cities);
    }

    @Test
    void printCityTestContainsNull()
    {
        ArrayList<City> cities = new ArrayList<City>();
        cities.add(null);
        app.printCities(cities);
    }

    @Test
    void printCities()
    {
        ArrayList<City> cities = new ArrayList<>();
        City city = new City();
        city.ID = 1;
        city.Name = "TEST";
        city.Country = "TEST";
        city.District = "TEST";
        city.Population = 1;
        cities.add(city);
        app.printCities(cities);
    }

    //TEST
    @Test
    void printCapitalCityTestNull()
    {
        app.printCapitalCities(null);
    }

    @Test
    void printCapitalCityTestEmpty()
    {
        ArrayList<City> cities = new ArrayList<City>();
        app.printCities(cities);
    }

    @Test
    void printCapitalCityTestContainsNull()
    {
        ArrayList<City> cities = new ArrayList<City>();
        cities.add(null);
        app.printCities(cities);
    }

    @Test
    void printCapitalCities()
    {
        ArrayList<City> cities = new ArrayList<>();
        City city = new City();
        city.ID = 1;
        city.Name = "TEST";
        city.Country = "TEST";
        city.District = "TEST";
        city.Population = 1;
        cities.add(city);
        app.printCities(cities);
    }
}