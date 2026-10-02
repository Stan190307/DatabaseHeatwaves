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
 * Updated for the 3NF schema structure.
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

    public void insertPostalArea(String postalCode, int cityId) throws SQLException {
        String sql = "INSERT IGNORE INTO Postal_Area (Postal_Code, City_ID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, postalCode);
            stmt.setInt(2, cityId);
            stmt.executeUpdate();
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

    public int insertHospital(String postalCode, String streetAddress,
                              int capacity) throws SQLException {
        String sql = "INSERT INTO Hospital "
                   + "(Postal_Code, Street_Address, Capacity) "
                   + "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, postalCode);
            stmt.setString(2, streetAddress);
            stmt.setInt(3, capacity);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertHeatwave(int locationId, LocalDate startDate, LocalDate endDate,
                              double minTemp, double maxTemp, double heatIndex) throws SQLException {
        String sql = "INSERT INTO Heatwave "
                   + "(Location_ID, Start_Date, End_Date, Minimal_Temperature, "
                   + "Maximum_Temperature, Heat_Index) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, locationId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            stmt.setDouble(4, minTemp);
            stmt.setDouble(5, maxTemp);
            stmt.setDouble(6, heatIndex);
            stmt.executeUpdate();
            return getGeneratedKey(stmt);
        }
    }

    public int insertPrevention(int heatwaveId, String description, boolean deployed) throws SQLException {
        String sql = "INSERT INTO Prevention "
                   + "(Heatwave_ID, Description, Deployment) "
                   + "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, heatwaveId);
            stmt.setString(2, description);
            stmt.setBoolean(3, deployed);
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
                            int age, String sex, String postalCode,
                            String streetAddress) throws SQLException {
        String sql = "INSERT INTO Victim "
                   + "(Heatwave_ID, First_Name, Last_Name, Age, Sex, "
                   + "Postal_Code, Street_Address) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, heatwaveId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setInt(4, age);
            stmt.setString(5, sex);
            stmt.setString(6, postalCode);
            stmt.setString(7, streetAddress);
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

    public int updateHospitalCapacity(int hospitalId, int newCapacity) throws SQLException {
        String sql = "UPDATE Hospital SET Capacity = ? WHERE Hospital_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newCapacity);
            stmt.setInt(2, hospitalId);
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
    // =================================================================

    public int deleteVictimInjury(int victimInjuryId) throws SQLException {
        String sql = "DELETE FROM Victim_Injury WHERE Victim_Injury_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, victimInjuryId);
            return stmt.executeUpdate();
        }
    }

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

    public List<String> getVictimsByHeatwave(int heatwaveId) throws SQLException {
        String sql = "SELECT v.Victim_ID, v.First_Name, v.Last_Name, v.Age, v.Sex, c.Name "
                   + "FROM Victim v "
                   + "JOIN Postal_Area pa ON v.Postal_Code = pa.Postal_Code "
                   + "JOIN City c ON pa.City_ID = c.City_ID "
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

    public List<String> getHeatwaveStatsByCountry() throws SQLException {
        String sql = "SELECT co.Name AS Country, COUNT(DISTINCT h.Heatwave_ID) AS Total_Heatwaves, "
                   + "COALESCE(SUM(CASE WHEN vi.Survivor = FALSE THEN 1 ELSE 0 END), 0) AS Total_Mortality, "
                   + "AVG(h.Maximum_Temperature) AS Avg_Max_Temp "
                   + "FROM Country co "
                   + "JOIN City ci ON co.Country_ID = ci.Country_Code "
                   + "JOIN Geography g ON ci.City_ID = g.City_ID "
                   + "JOIN Heatwave h ON g.Location_ID = h.Location_ID "
                   + "LEFT JOIN Victim v ON h.Heatwave_ID = v.Heatwave_ID "
                   + "LEFT JOIN Victim_Injury vi ON v.Victim_ID = vi.Victim_ID "
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

    public List<String> getSevereHeatwavesAboveAverage() throws SQLException {
        String sql = "SELECT ci.Name AS City, co.Name AS Country, h.Start_Date, h.Maximum_Temperature "
                   + "FROM Heatwave h "
                   + "JOIN Geography g ON h.Location_ID = g.Location_ID "
                   + "JOIN City ci ON g.City_ID = ci.City_ID "
                   + "JOIN Country co ON ci.Country_Code = co.Country_ID "
                   + "WHERE h.Maximum_Temperature >= ( "
                   + "    SELECT AVG(h2.Maximum_Temperature) "
                   + "    FROM Heatwave h2 "
                   + "    JOIN Geography g2 ON h2.Location_ID = g2.Location_ID "
                   + "    JOIN City ci2 ON g2.City_ID = ci2.City_ID "
                   + "    WHERE ci2.Country_Code = co.Country_ID "
                   + ") "
                   + "ORDER BY h.Maximum_Temperature DESC";

        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(String.format("City: %s (%s) | Start Date: %s | Max Temp: %.1f C",
                    rs.getString("City"),
                    rs.getString("Country"),
                    rs.getDate("Start_Date").toString(),
                    rs.getDouble("Maximum_Temperature")));
            }
        }
        return results;
    }

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

    public List<String> getHeatwavesByCountry(String countryName) throws SQLException {
        String sql = "SELECT h.Heatwave_ID, h.Start_Date, h.End_Date, "
                   + "h.Maximum_Temperature, ci.Name AS City_Name, "
                   + "COALESCE(SUM(CASE WHEN vi.Survivor = FALSE THEN 1 ELSE 0 END), 0) AS Calculated_Mortality "
                   + "FROM Heatwave h "
                   + "JOIN Geography g  ON h.Location_ID = g.Location_ID "
                   + "JOIN City ci      ON g.City_ID = ci.City_ID "
                   + "JOIN Country co   ON ci.Country_Code = co.Country_ID "
                   + "LEFT JOIN Victim v ON h.Heatwave_ID = v.Heatwave_ID "
                   + "LEFT JOIN Victim_Injury vi ON v.Victim_ID = vi.Victim_ID "
                   + "WHERE co.Name = ? "
                   + "GROUP BY h.Heatwave_ID, h.Start_Date, h.End_Date, h.Maximum_Temperature, ci.Name "
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
                        rs.getInt("Calculated_Mortality")));
                }
            }
        }
        return results;
    }

    public List<String> getHospitalsByCity(String cityName) throws SQLException {
        String sql = "SELECT h.Hospital_ID, h.Street_Address, h.Postal_Code, h.Capacity "
                   + "FROM Hospital h "
                   + "JOIN Postal_Area pa ON h.Postal_Code = pa.Postal_Code "
                   + "JOIN City c ON pa.City_ID = c.City_ID "
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
                        rs.getString("Postal_Code"),
                        rs.getInt("Capacity")));
                }
            }
        }
        return results;
    }

    public int countRows(String tableName) throws SQLException {
        List<String> allowed = List.of("Country", "City", "Postal_Area", "Geography", 
            "Hospital", "Heatwave", "Prevention", "Infrastructure_Impact", "Victim",
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

    private int getGeneratedKey(PreparedStatement stmt) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt(1);
            }
            throw new SQLException("Insert succeeded but no ID was returned.");
        }
    }
}