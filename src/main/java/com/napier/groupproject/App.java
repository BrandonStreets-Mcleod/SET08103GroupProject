package com.napier.groupproject;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    /**
     * @param args - the argument that is passed to the main function
     */
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();
        //gets population of all countries
        ArrayList<Country> countries = a.populationOfCountries();
        printCountries(countries);
        ArrayList<Country> countriesInContinent = a.populationOfCountriesInContinent("Asia");
        printCountries(countriesInContinent);
        ArrayList<Country> countriesInRegion = a.populationOfCountriesInRegion("North America");
        printCountries(countriesInRegion);
        ArrayList<City> cities = a.allCities();
        printCities(cities);
        ArrayList<City> citiesInContinent = a.allCitiesInAContinent("Asia");
        printCities(citiesInContinent);
        ArrayList<City> citiesInRegion = a.allCitiesInARegion("North America");
        printCities(citiesInRegion);
        ArrayList<City> citiesInCountry = a.allCitiesInACountry("United States");
        printCities(citiesInCountry);
        ArrayList<City> citiesInDistrict = a.allCitiesInADistrict("California");
        printCities(citiesInDistrict);
        ArrayList<City> capitalCities = a.allCapitalCities();
        printCapitalCities(capitalCities);
        ArrayList<City> capitalCitiesInContinent = a.allCapitalCitiesInContinent("Asia");
        printCapitalCities(capitalCitiesInContinent);
        ArrayList<City> capitalCitiesInRegion = a.allCapitalCitiesInRegion("North America");
        printCapitalCities(capitalCitiesInRegion);
        // Disconnect from database
        a.disconnect();
    }
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
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
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountries()
    {
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
            ArrayList<Country> countries = new ArrayList<>();
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
        return null;
    }

    /**
     * @param continentName - passes the string value of the continents name
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountriesInContinent(String continentName)
    {
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
            ArrayList<Country> countries = new ArrayList<>();
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
        return null;
    }

    /**
     * @param regionName - the string value of the regions name
     * @return list of countries
     */
    public ArrayList<Country> populationOfCountriesInRegion(String regionName)
    {
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
            ArrayList<Country> countries = new ArrayList<>();
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
        return null;
    }

    /**
     * @param countries - an arraylist of country objects that can be printed to create the report
     */
    public static void printCountries(ArrayList<Country> countries)
    {
        // Print header
        System.out.println(String.format("%-5s %-50s %-20s %-35s %-20s %-20s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Loop over all countries in the list
        for (Country country : countries)
        {
            System.out.println(String.format("%-5s %-50s %-20s %-35s %-20s %-20s", country.code, country.name, country.continent, country.region, country.population, country.capital));
        }
    }

    public ArrayList<City> allCities()
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCitiesInAContinent(String continentName)
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCitiesInARegion(String regionName)
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCitiesInACountry(String countryName)
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCitiesInADistrict(String districtName)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT ID, city.Name, country.name, District, city.Population " +
                    "FROM city JOIN country ON (country.code = city.CountryCode) WHERE Continent = '" + districtName +
                    "' ORDER BY city.Population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public static void printCities(ArrayList<City> cities)
    {
        // Print header
        System.out.println(String.format("%-30s %-30s %-35s %-20s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cities)
        {
            System.out.println(String.format("%-30s %-30s %-35s %-20s", city.Name, city.Country, city.District, city.Population));
        }
    }

    public ArrayList<City> allCapitalCities()
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCapitalCitiesInContinent(String continent)
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }

    public ArrayList<City> allCapitalCitiesInRegion(String regionName)
    {
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
            ArrayList<City> cities = new ArrayList<>();
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
        return null;
    }
    public static void printCapitalCities(ArrayList<City> cities)
    {
        // Print header
        System.out.println(String.format("%-30s %-30s %-20s", "Name", "Country", "Population"));
        // Loop over all countries in the list
        for (City city : cities)
        {
            System.out.println(String.format("%-30s %-30s %-20s", city.Name, city.Country, city.Population));
        }
    }
}