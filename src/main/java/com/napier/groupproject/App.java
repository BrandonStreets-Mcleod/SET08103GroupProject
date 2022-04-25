package com.napier.groupproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.Math.round;

/**
 * definiton for main App class
 */
public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;
    /**
     * @param args - the argument that is passed to the main function
     */
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        //gets population of all countries
        ArrayList<Country> countries = a.populationOfCountries();
        printCountries(countries, "allCountries.md");
        ArrayList<Country> countriesInContinent = a.populationOfCountriesInContinent("Asia");
        printCountries(countriesInContinent, "allCountriesInContinent.md");
        ArrayList<Country> countriesInRegion = a.populationOfCountriesInRegion("North America");
        printCountries(countriesInRegion, "allCountriesInRegion.md");
        ArrayList<City> cities = a.allCities();
        printCities(cities, "allCities.md");
        ArrayList<City> citiesInContinent = a.allCitiesInAContinent("Asia");
        printCities(citiesInContinent, "allCitiesInContinent.md");
        ArrayList<City> citiesInRegion = a.allCitiesInARegion("North America");
        printCities(citiesInRegion, "allCitiesInRegion.md");
        ArrayList<City> citiesInCountry = a.allCitiesInACountry("United States");
        printCities(citiesInCountry, "allCitiesInCountry.md");
        ArrayList<City> citiesInDistrict = a.allCitiesInADistrict("California");
        printCities(citiesInDistrict, "allCitiesInDistrict.md");
        ArrayList<City> capitalCities = a.allCapitalCities();
        printCapitalCities(capitalCities, "allCapitalCities.md");
        ArrayList<City> capitalCitiesInContinent = a.allCapitalCitiesInContinent("Asia");
        printCapitalCities(capitalCitiesInContinent, "allCapitalCitiesInContinent.md");
        ArrayList<City> capitalCitiesInRegion = a.allCapitalCitiesInRegion("North America");
        printCapitalCities(capitalCitiesInRegion, "allCapitalCitiesInRegion.md");
        a.populationOfContinent("Asia");
        a.populationOfCountry("United States");
        a.populationOfRegion("North America");
        a.populationOfDistrict("California");
        ArrayList<City> city = a.populationOfCity("California");
        printCities(city, "populationOfCity.md");
        a.populationPeopleInContinents();
        a.populationPeopleInRegion();
        a.populationPeopleInCountry();
        a.populationPeopleInDistrict();
        a.populationPeopleInCity();
        a.populationPeopleInWorld();
        a.numPeopleWhoSpeak();
        ArrayList<Country> topNcountries = a.populationOfNCountries(5);
        printCountries(topNcountries, "topNCountries.md");
        ArrayList<Country> topNCountriesInContinent = a.populationOfNCountriesInContinent("Asia", 5);
        printCountries(topNCountriesInContinent, "topNCountiresInContinent.md");
        ArrayList<Country> topNCountriesInregion = a.populationOfNCountriesInRegion("North America", 5);
        printCountries(topNCountriesInregion, "topNCountriesInRegion.md");
        ArrayList<City> topNCities = a.topNCities(5);
        printCities(topNCities, "topNCities.md");
        ArrayList<City> topNCitiesInContinent = a.topNCitiesInContinent("Asia", 5);
        printCities(topNCitiesInContinent, "topNCitiesInContinent.md");
        ArrayList<City> topNCitiesInRegion = a.topNCitiesInRegion("North America", 5);
        printCities(topNCitiesInRegion, "topNCitiesInRegion.md");
        ArrayList<City> topNCitiesInCountry = a.topNCitiesInCountry("United States", 5);
        printCities(topNCitiesInCountry, "topNCitiesInCountry.md");
        ArrayList<City> topNCitiesInDistrict = a.topNCitiesInDistrict("California", 5);
        printCities(topNCitiesInDistrict, "topNCitiesInDistrict.md");
        ArrayList<City> topNcapitalCities = a.topNCapitalCities(5);
        printCapitalCities(topNcapitalCities, "topNCapitalCities.md");
        ArrayList<City> topNcapitalCitiesInContinent = a.topNCapitalCitiesInContinent("Asia", 5);
        printCapitalCities(topNcapitalCitiesInContinent, "topNCapitalCitiesInContinent.md");
        ArrayList<City> topNcapitalCitiesInRegion = a.topNCapitalCitiesInRegion("North America", 5);
        printCapitalCities(topNcapitalCitiesInRegion, "topNCapitalCitiesInRegion.md");
        // Disconnect from database
        a.disconnect();
    }
    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Population of each country
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountries()
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                    "FROM country ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * Finds population of countries within a continent
     * @param continentName - passes the string value of the continents name
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountriesInContinent(String continentName)
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                            "FROM country WHERE Continent = '" + continentName + "' ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * Finds population of countries within a region
     * @param regionName - the string value of the regions name
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountriesInRegion(String regionName)
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                            "FROM country WHERE Region = '" + regionName + "' ORDER BY Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * Prints all countries from the arraylist
     * @param countries - an arraylist of country objects that can be printed to create the report
     */
    public static void printCountries(ArrayList<Country> countries, String filename)
    {
        if (countries == null)
        {
            System.out.println("No countries");
            return;
        }
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Code | Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (Country country : countries)
        {
            if (country == null)
            {
                continue;
            }
            sb.append("| " + country.code + " | " + country.name + " | " + country.continent + " | " + country.region + " | " + country.population + " | " + country.capital + " | \r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new                                 File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return ArrayList<City>
     */
    public ArrayList<City> allCities()
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
            "FROM city JOIN country ON (country.code = city.CountryCode) " +
            "ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param continentName - name of continent to be used in query
     */
    public ArrayList<City> allCitiesInAContinent(String continentName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE Continent = '" + continentName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param regionName - name of region to be used in query
     */
    public ArrayList<City> allCitiesInARegion(String regionName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE Region = '" + regionName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param countryName - name of country used in query
     */
    public ArrayList<City> allCitiesInACountry(String countryName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE country.Name = '" + countryName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param districtName - name of district to be used in query
     */
    public ArrayList<City> allCitiesInADistrict(String districtName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE District = '" + districtName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param cities - arraylist of City objs
     * @param filename - filename of report for adding to repo
     */
    public static void printCities(ArrayList<City> cities, String filename)
    {
        if (cities == null)
        {
            System.out.println("No cities");
            return;
        }
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (City city : cities)
        {
            if (city == null)
            {
                continue;
            }
            sb.append("| " + city.Name + " | " + city.Country + " | " + city.District + " | " + city.Population + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new                                 File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return ArrayList<City>
     */
    public ArrayList<City> allCapitalCities()
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID)" +
                    " ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param continent - name of contient to be used in query
     * @return null
     */
    public ArrayList<City> allCapitalCitiesInContinent(String continent)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID) && continent = '" + continent +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param regionName - name of region to be used in query
     * @return null
     */
    public ArrayList<City> allCapitalCitiesInRegion(String regionName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID) && Region = '" + regionName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param cities - arraylist of City objs that are written to report
     * @param filename - name of file for report
     */
    public static void printCapitalCities(ArrayList<City> cities, String filename)
    {
        if (cities == null)
        {
            System.out.println("No capital countries");
            return;
        }
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Name | Country | Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (City city : cities)
        {
            if (city == null)
            {
                continue;
            }
            sb.append("| " + city.Name + " | " + city.Country + " | " + city.Population + " | \r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new                                 File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to get population of people on continents
     */
    public void populationPeopleInContinents()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Continent, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "GROUP BY country.Continent";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Continent Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Continent");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * function to get population of people in region
     */
    public void populationPeopleInRegion()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Region, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "GROUP BY country.Region";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Region Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Region");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * function to get population of people in country
     */
    public void populationPeopleInCountry()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Name, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "GROUP BY country.Name";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Country Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Name");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * function to get population of people in district
     */
    public void populationPeopleInDistrict()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Name, District, (country.Population), SUM(city.Population)" +
                    "FROM city JOIN country ON (country.code = city.CountryCode)" +
                    "GROUP BY District";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Country Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Name");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * function to get population of people in city
     */
    public void populationPeopleInCity()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT city.Name, (country.Population), SUM(city.Population)" +
                    "FROM city JOIN country ON (country.code = city.CountryCode)" +
                    "GROUP BY city.Name";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Country Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Name");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * function to get population of people in city
     */
    public void populationPeopleInWorld()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT SUM(Population) AS World Population" +
                    "FROM country" +
                    "GROUP BY World Population";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s", "Total Population"));
            while (rset.next())
            {
                Long totalPopulation = rset.getLong("SUM(Population)");
                System.out.println(String.format("%-28s", totalPopulation));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * gets the number of people who speak specific languages
     */
    public void numPeopleWhoSpeak()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT Language, SUM(country.Population), MIN(T1.Population) FROM countrylanguage JOIN country ON (country.code = countrylanguage.CountryCode) JOIN (SELECT SUM(Population) AS 'Population' FROM country) AS T1 " +
                    "WHERE Language = 'Chinese' OR Language = 'English' OR " +
                    "Language = 'Hindi' OR Language = 'Spanish' OR Language = 'Arabic' " +
                    "GROUP BY Language " +
                    "ORDER BY SUM(country.Population) DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s", "Language", "Total Population that speaks language", "Percentage of world population"));
            while (rset.next())
            {
                String name = rset.getString("Language");
                Long pop = rset.getLong("SUM(country.Population)");
                Long totalPopulation = rset.getLong("MIN(T1.Population)");
                Double percentage = Double.valueOf(round(pop * 100 / totalPopulation));
                System.out.println(String.format("%-28s %-28s %-28s", name, pop, percentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * @param contientName
     */
    public void populationOfContinent(String contientName)
    {
        try
        {
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Continent, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "WHERE country.Continent = '" + contientName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Continent Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Continent");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * @param countryName
     */
    public void populationOfCountry(String countryName)
    {
        try
        {
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Name, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "WHERE country.name = '" + countryName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Country Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Name");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * @param regionName
     */
    public void populationOfRegion(String regionName)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT country.Region, SUM(country.Population), SUM(city.Population) " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "WHERE country.Region = '" + regionName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "Region Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("country.Region");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * @param districtName
     */
    public void populationOfDistrict(String districtName)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT District, SUM(country.Population), SUM(city.Population)" +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE District = '" + districtName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", "District Name", "Total Population", "City Population", "City Population Percentage","Non-city Population", "Non-city Population Percentage"));
            while (rset.next())
            {
                String name = rset.getString("District");
                Long totalPopulation = rset.getLong("SUM(country.Population)");
                Long cityPopulation = rset.getLong("SUM(city.Population)");
                double cityPopPercentage = round(cityPopulation * 100 / totalPopulation);
                Long nonCityPopulation = totalPopulation-cityPopulation;
                double nonCityPopPercentage = 100-cityPopPercentage;
                System.out.println(String.format("%-28s %-28s %-28s %-28s %-28s %-28s", name, totalPopulation, cityPopulation, cityPopPercentage+"%", nonCityPopulation, nonCityPopPercentage+"%"));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
    }

    /**
     * @param cityName
     * @return ArrayList<City>
     */
    public ArrayList<City> populationOfCity(String cityName)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE District = '" + cityName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details");
        }
        return cities;
    }

    /**
     * @param N
     * @return ArrayList<Country>
     */
    public ArrayList<Country> populationOfNCountries(int N)
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                            "FROM country ORDER BY Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * @param continentName
     * @param N
     * @return ArrayList<Country>
     */
    public ArrayList<Country> populationOfNCountriesInContinent(String continentName, int N)
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                            "FROM country WHERE Continent = '" + continentName + "' ORDER BY Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * @param regionName
     * @param N
     * @return ArrayList<Country>
     */
    public ArrayList<Country> populationOfNCountriesInRegion(String regionName, int N)
    {
        ArrayList<Country> countries = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital " +
                            "FROM country WHERE Region = '" + regionName + "' ORDER BY Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                Country country = new Country();
                country.code = rset.getString("Code");
                country.name = rset.getString("Name");
                country.continent = rset.getString("Continent");
                country.region = rset.getString("Region");
                country.population = rset.getInt("Population");
                country.capital = rset.getInt("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
        return countries;
    }

    /**
     * @param N
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCities(int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) " +
                    "ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param continentName
     * @param N
     * @return arrayList<City>
     */
    public ArrayList<City> topNCitiesInContinent(String continentName, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE Continent = '" + continentName +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param regionName - name of region to be used in query
     * @param N - number of records to show
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCitiesInRegion(String regionName, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE Region = '" + regionName +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param countryName - name of country used in query
     * @param N - number of records to show
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCitiesInCountry(String countryName, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE country.Name = '" + countryName +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param districtName - name of district to be used in query
     * @param N - number of records to show
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCitiesInDistrict(String districtName, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE District = '" + districtName +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }


    /**
     * @param N
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCapitalCities(int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID)" +
                    " ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param continent - name of contient to be used in query
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCapitalCitiesInContinent(String continent, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID) && continent = '" + continent +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }

    /**
     * @param regionName - name of region to be used in query
     * @param N - number of records to show
     * @return ArrayList<City>
     */
    public ArrayList<City> topNCapitalCitiesInRegion(String regionName, int N)
    {
        ArrayList<City> cities = new ArrayList<>();
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE (Capital = ID) && Region = '" + regionName +
                    "' ORDER BY city.Population DESC LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            while (rset.next())
            {
                City city = new City();
                city.ID = rset.getInt("ID");
                city.Name = rset.getString("city.name");
                city.Country = rset.getString("country.name");
                city.District = rset.getString("District");
                city.Population = rset.getInt("city.Population");

                cities.add(city);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
        }
        return cities;
    }
}


