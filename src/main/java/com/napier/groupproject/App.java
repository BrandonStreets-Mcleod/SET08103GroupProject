package com.napier.groupproject;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Math.round;

public class App
{
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
        a.populationPeopleInContinents();
        a.populationPeopleInRegion();
        a.populationPeopleInCountry();
        a.numPeopleWhoSpeak();
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
     * Finds population of countries within a continent
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
     * Finds population of countries within a region
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
     * Prints all countries from the arraylist
     * @param countries - an arraylist of country objects that can be printed to create the report
     */
    public static void printCountries(ArrayList<Country> countries)
    {
        if (countries == null)
        {
            System.out.println("No countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-5s %-50s %-20s %-35s %-20s %-20s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Loop over all countries in the list
        for (Country country : countries)
        {
            if (country == null)
            {
                continue;
            }
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
        if (cities == null)
        {
            System.out.println("No cities");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-30s %-35s %-20s", "Name", "Country", "District", "Population"));
        // Loop over all countries in the list
        for (City city : cities)
        {
            if (city == null)
            {
                continue;
            }
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
        if (cities == null)
        {
            System.out.println("No capital countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-30s %-20s", "Name", "Country", "Population"));
        // Loop over all countries in the list
        for (City city : cities)
        {
            if (city == null)
            {
                continue;
            }
            System.out.println(String.format("%-30s %-30s %-20s", city.Name, city.Country, city.Population));
        }
    }

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
}


