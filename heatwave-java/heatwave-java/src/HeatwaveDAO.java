import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the heatwave database.
 *
 * Every method here does one SQL operation. Other parts of the project
 * (mock data generation, advanced queries) should call these methods
 * instead of writing their own SQL.
 *
 * All statements use PreparedStatement with ? placeholders. This is both
 * safer (no SQL injection) and handles quoting and date formats for us.
 */
public class HeatwaveDAO {

    // =================================================================
    // CREATE - insert methods
    // Each returns the generated primary key of the new row.
    // =================================================================

    public int insertCountry(String name) throws SQLException {
        String sql = "INSERT INTO Country (Name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertCity(String name, String region, int countryId) throws SQLException {
        String sql = "INSERT INTO City (Name, Region, Country_Code) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, region);
            stmt.setInt(3, countryId);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertGeography(int cityId, double latitude, double longitude,
                               String weatherStation) throws SQLException {
        String sql = "INSERT INTO Geography "
                   + "(City_ID, Latitude, Longitude, Closest_Weather_Station) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cityId);
            stmt.setDouble(2, latitude);
            stmt.setDouble(3, longitude);
            stmt.setString(4, weatherStation);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertHospital(int cityId, String streetAddress,
                              String postalAddress, int capacity) throws SQLException {
        String sql = "INSERT INTO Hospital "
                   + "(City_ID, Street_Address, Postal_Address, Capacity) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cityId);
            stmt.setString(2, streetAddress);
            stmt.setString(3, postalAddress);
            stmt.setInt(4, capacity);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    /**
     * Inserts a heatwave. Prevention_ID is left NULL on purpose:
     * Heatwave and Prevention reference each other, so the heatwave has to
     * exist before a prevention row can point at it. Use
     * linkPreventionToHeatwave() afterwards to fill it in.
     */
    public int insertHeatwave(int locationId, LocalDate startDate, LocalDate endDate,
                              double minTemp, double maxTemp, double avgTemp,
                              int mortality, double heatIndex) throws SQLException {
        String sql = "INSERT INTO Heatwave "
                   + "(Location_ID, Start_Date, End_Date, Minimal_Temperature, "
                   + "Maximum_Temperature, Avg_Temperature, Mortality, Heat_Index) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, locationId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            stmt.setDouble(4, minTemp);
            stmt.setDouble(5, maxTemp);
            stmt.setDouble(6, avgTemp);
            stmt.setInt(7, mortality);
            stmt.setDouble(8, heatIndex);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertPrevention(int heatwaveId, int locationId,
                                String description, boolean deployed) throws SQLException {
        String sql = "INSERT INTO Prevention "
                   + "(Heatwave_ID, Location_ID, Description, Deployment) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, heatwaveId);
            stmt.setInt(2, locationId);
            stmt.setString(3, description);
            stmt.setBoolean(4, deployed);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertInfrastructureImpact(int heatwaveId, boolean powerOutage,
                                          boolean waterRestrictions,
                                          boolean transportIssues) throws SQLException {
        String sql = "INSERT INTO Infrastructure_Impact "
                   + "(Heatwave_ID, Power_Outage, Water_Restrictions, Transportation_Issues) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, heatwaveId);
            stmt.setBoolean(2, powerOutage);
            stmt.setBoolean(3, waterRestrictions);
            stmt.setBoolean(4, transportIssues);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertVictim(int heatwaveId, String firstName, String lastName,
                            int age, String sex, int cityId,
                            String streetAddress, String postalCode) throws SQLException {
        String sql = "INSERT INTO Victim "
                   + "(Heatwave_ID, First_Name, Last_Name, Age, Sex, "
                   + "City_ID, Street_Address, Postal_Code) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, heatwaveId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, age);
            stmt.setString(5, sex);
            stmt.setInt(6, cityId);
            stmt.setString(7, streetAddress);
            stmt.setString(8, postalCode);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertInjuryType(String typeName, String description) throws SQLException {
        String sql = "INSERT INTO Injury_Type (Type_Name, Description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, typeName);
            stmt.setString(2, description);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    /** Hospital is optional here, so pass null if the victim was not admitted. */
    public int insertVictimInjury(int victimId, int injuryTypeId,
                                  Integer hospitalId, boolean survivor) throws SQLException {
        String sql = "INSERT INTO Victim_Injury "
                   + "(Victim_ID, Injury_Type_ID, Hospital_ID, Survivor) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, victimId);
            stmt.setInt(2, injuryTypeId);
            if (hospitalId == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, hospitalId);
            }
            stmt.setBoolean(4, survivor);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    // =================================================================
    // UPDATE
    // =================================================================

    /** Fills in the Prevention_ID on a heatwave after the prevention exists. */
    public int linkPreventionToHeatwave(int heatwaveId, int preventionId) throws SQLException {
        String sql = "UPDATE Heatwave SET Prevention_ID = ? WHERE Heatwave_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preventionId);
            stmt.setInt(2, heatwaveId);
            return stmt.executeUpdate();
        }
    }

    public int updateHospitalCapacity(int hospitalId, int newCapacity) throws SQLException {
        String sql = "UPDATE Hospital SET Capacity = ? WHERE Hospital_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newCapacity);
            stmt.setInt(2, hospitalId);
            return stmt.executeUpdate();
        }
    }

    public int updateHeatwaveMortality(int heatwaveId, int newMortality) throws SQLException {
        String sql = "UPDATE Heatwave SET Mortality = ? WHERE Heatwave_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newMortality);
            stmt.setInt(2, heatwaveId);
            return stmt.executeUpdate();
        }
    }

    public int updateVictimAddress(int victimId, String streetAddress,
                                   String postalCode) throws SQLException {
        String sql = "UPDATE Victim SET Street_Address = ?, Postal_Code = ? "
                   + "WHERE Victim_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, streetAddress);
            stmt.setString(2, postalCode);
            stmt.setInt(3, victimId);
            return stmt.executeUpdate();
        }
    }

    public int markPreventionDeployed(int preventionId, boolean deployed) throws SQLException {
        String sql = "UPDATE Prevention SET Deployment = ? WHERE Prevention_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, deployed);
            stmt.setInt(2, preventionId);
            return stmt.executeUpdate();
        }
    }

    // =================================================================
    // DELETE
    // Child rows must be deleted before their parents, otherwise the
    // foreign keys block the delete.
    // =================================================================

    public int deleteVictimInjury(int victimInjuryId) throws SQLException {
        String sql = "DELETE FROM Victim_Injury WHERE Victim_Injury_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, victimInjuryId);
            return stmt.executeUpdate();
        }
    }

    /** Deletes a victim and any injury records attached to them. */
    public int deleteVictim(int victimId) throws SQLException {
        String deleteInjuries = "DELETE FROM Victim_Injury WHERE Victim_ID = ?";
        String deleteVictim = "DELETE FROM Victim WHERE Victim_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement s1 = conn.prepareStatement(deleteInjuries);
                 PreparedStatement s2 = conn.prepareStatement(deleteVictim)) {

                s1.setInt(1, victimId);
                s1.executeUpdate();

                s2.setInt(1, victimId);
                int rows = s2.executeUpdate();

                conn.commit();
                return rows;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public int deleteHospital(int hospitalId) throws SQLException {
        String sql = "DELETE FROM Hospital WHERE Hospital_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hospitalId);
            return stmt.executeUpdate();
        }
    }

    public int deleteInjuryType(int injuryTypeId) throws SQLException {
        String sql = "DELETE FROM Injury_Type WHERE Injury_Type_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, injuryTypeId);
            return stmt.executeUpdate();
        }
    }

    // =================================================================
    // READ - select methods
    // =================================================================

    /** All victims of one heatwave, with their city name. */
    public List<String> getVictimsByHeatwave(int heatwaveId) throws SQLException {
        String sql = "SELECT v.Victim_ID, v.First_Name, v.Last_Name, v.Age, v.Sex, c.Name "
                   + "FROM Victim v "
                   + "JOIN City c ON v.City_ID = c.City_ID "
                   + "WHERE v.Heatwave_ID = ? "
                   + "ORDER BY v.Age DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, heatwaveId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("#%d %s %s, age %d, %s, %s",
                        rs.getInt("Victim_ID"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getInt("Age"),
                        rs.getString("Sex"),
                        rs.getString("Name")));
                }
            }
        }
        return results;
    }

    // Advanced Query 1: Aggregation with GROUP BY
    public List<String> getHeatwaveStatsByCountry() throws SQLException {
        String sql = "SELECT co.Name AS Country, COUNT(h.Heatwave_ID) AS Total_Heatwaves, "
                   + "SUM(h.Mortality) AS Total_Mortality, AVG(h.Maximum_Temperature) AS Avg_Max_Temp "
                   + "FROM Heatwave h "
                   + "JOIN Geography g ON h.Location_ID = g.Location_ID "
                   + "JOIN City ci ON g.City_ID = ci.City_ID "
                   + "JOIN Country co ON ci.Country_Code = co.Country_ID "
                   + "GROUP BY co.Name "
                   + "ORDER BY Total_Mortality DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(String.format("Country: %s | Heatwaves: %d | Total Deaths: %d | Avg Max Temp: %.1f C",
                    rs.getString("Country"), rs.getInt("Total_Heatwaves"),
                    rs.getInt("Total_Mortality"), rs.getDouble("Avg_Max_Temp")));
            }
        }
        return results;
    }

    // Advanced Query 2: Subquery
    public List<String> getSevereHeatwavesAboveAverage() throws SQLException {
        String sql = "SELECT ci.Name AS City, h.Start_Date, h.Maximum_Temperature "
                   + "FROM Heatwave h "
                   + "JOIN Geography g ON h.Location_ID = g.Location_ID "
                   + "JOIN City ci ON g.City_ID = ci.City_ID "
                   + "WHERE h.Maximum_Temperature > (SELECT AVG(Maximum_Temperature) FROM Heatwave) "
                   + "ORDER BY h.Maximum_Temperature DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(String.format("City: %s | Start Date: %s | Max Temp: %.1f C (Above Average)",
                    rs.getString("City"), rs.getDate("Start_Date").toString(), rs.getDouble("Maximum_Temperature")));
            }
        }
        return results;
    }

    // Advanced Query 3: Conditional Aggregation and HAVING
    public List<String> getInjurySurvivalStats() throws SQLException {
        String sql = "SELECT it.Type_Name, COUNT(vi.Victim_Injury_ID) AS Total_Cases, "
                   + "SUM(CASE WHEN vi.Survivor = true THEN 1 ELSE 0 END) AS Survivors "
                   + "FROM Injury_Type it "
                   + "LEFT JOIN Victim_Injury vi ON it.Injury_Type_ID = vi.Injury_Type_ID "
                   + "GROUP BY it.Type_Name "
                   + "HAVING Total_Cases > 0 "
                   + "ORDER BY Total_Cases DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int total = rs.getInt("Total_Cases");
                int survivors = rs.getInt("Survivors");
                double survivalRate = (total == 0) ? 0 : ((double) survivors / total) * 100;
                results.add(String.format("Injury: %s | Cases: %d | Survivors: %d | Survival Rate: %.1f%%",
                    rs.getString("Type_Name"), total, survivors, survivalRate));
            }
        }
        return results;
    }

    /** All heatwaves in one country, newest first. */
    public List<String> getHeatwavesByCountry(String countryName) throws SQLException {
        String sql = "SELECT h.Heatwave_ID, h.Start_Date, h.End_Date, "
                   + "h.Maximum_Temperature, h.Mortality, ci.Name AS City_Name "
                   + "FROM Heatwave h "
                   + "JOIN Geography g  ON h.Location_ID = g.Location_ID "
                   + "JOIN City ci      ON g.City_ID = ci.City_ID "
                   + "JOIN Country co   ON ci.Country_Code = co.Country_ID "
                   + "WHERE co.Name = ? "
                   + "ORDER BY h.Start_Date DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, countryName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("Heatwave #%d in %s: %s to %s, max %.1f C, %d deaths",
                        rs.getInt("Heatwave_ID"),
                        rs.getString("City_Name"),
                        rs.getDate("Start_Date").toString(),
                        rs.getDate("End_Date").toString(),
                        rs.getDouble("Maximum_Temperature"),
                        rs.getInt("Mortality")));
                }
            }
        }
        return results;
    }

    /** Every hospital in a city. */
    public List<String> getHospitalsByCity(String cityName) throws SQLException {
        String sql = "SELECT h.Hospital_ID, h.Street_Address, h.Postal_Address, h.Capacity "
                   + "FROM Hospital h "
                   + "JOIN City c ON h.City_ID = c.City_ID "
                   + "WHERE c.Name = ? "
                   + "ORDER BY h.Capacity DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cityName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("Hospital #%d, %s %s, capacity %d",
                        rs.getInt("Hospital_ID"),
                        rs.getString("Street_Address"),
                        rs.getString("Postal_Address"),
                        rs.getInt("Capacity")));
                }
            }
        }
        return results;
    }

    /** Counts rows in any table. Handy for checking mock data loaded properly. */
    public int countRows(String tableName) throws SQLException {
        // Table names cannot be parameterised, so we check it against a
        // fixed list first instead of pasting user input into the SQL.
        List<String> allowed = List.of("Country", "City", "Geography", "Hospital",
            "Heatwave", "Prevention", "Infrastructure_Impact", "Victim",
            "Injury_Type", "Victim_Injury");

        if (!allowed.contains(tableName)) {
            throw new IllegalArgumentException("Unknown table: " + tableName);
        }

        String sql = "SELECT COUNT(*) AS total FROM " + tableName;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    // =================================================================
    // Helper
    // =================================================================

    private int getGeneratedKey(PreparedStatement stmt) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt(1);
            }
            throw new SQLException("Insert succeeded but no ID was returned.");
        }
    }
}
