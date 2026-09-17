import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Demonstrates basic SQL operations
 * inserting, reading, updating and deleting data.
 * 
 * Run this once the schema has been created in MySQL.
 */

public class Main {

    public static void main(String[] args) {

        // ---- Step 0: check the connection works at all ----------------
        System.out.println("=== Testing connection ===");
        DatabaseConnection.testConnection();
        System.out.println();

        HeatwaveDAO dao = new HeatwaveDAO();

        try {
            // ---- Step 1: INSERT ---------------------------------------
            // Parents first, then children. The foreign keys force this order.
            System.out.println("=== Inserting data ===");

            int netherlands = dao.insertCountry("Netherlands");
            System.out.println("Country inserted, ID = " + netherlands);

            int maastricht = dao.insertCity("Maastricht", "Limburg", netherlands);
            System.out.println("City inserted, ID = " + maastricht);

            int location = dao.insertGeography(maastricht, 50.8514, 5.6910, "Maastricht Aachen Airport");
            System.out.println("Geography inserted, ID = " + location);

            int hospital = dao.insertHospital(maastricht, "P. Debyelaan 25", "6229 HX", 715);
            System.out.println("Hospital inserted, ID = " + hospital);

            // Heatwave goes in without a prevention link (see note below).
            int heatwave = dao.insertHeatwave(
                location,
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 15),
                21.4, 38.2, 29.8,
                12, 41.5);
            System.out.println("Heatwave inserted, ID = " + heatwave);

            int prevention = dao.insertPrevention(
                heatwave, location,
                "Cooling centres opened in public libraries", true);
            System.out.println("Prevention inserted, ID = " + prevention);

            // Now close the circle: point the heatwave back at its prevention.
            dao.linkPreventionToHeatwave(heatwave, prevention);
            System.out.println("Heatwave linked to prevention plan");

            dao.insertInfrastructureImpact(heatwave, true, true, false);
            System.out.println("Infrastructure impact inserted");

            int victim1 = dao.insertVictim(heatwave, "Jan", "de Vries", 78, "Male",
                maastricht, "Brusselsestraat 12", "6211 PB");
            int victim2 = dao.insertVictim(heatwave, "Anna", "Bakker", 83, "Female",
                maastricht, "Grote Gracht 44", "6211 SZ");
            System.out.println("Victims inserted, IDs = " + victim1 + ", " + victim2);

            int heatstroke = dao.insertInjuryType("Heatstroke",
                "Core body temperature above 40 C with central nervous system effects");
            int dehydration = dao.insertInjuryType("Severe dehydration",
                "Fluid loss causing circulatory problems");
            System.out.println("Injury types inserted, IDs = " + heatstroke + ", " + dehydration);

            int injury1 = dao.insertVictimInjury(victim1, heatstroke, hospital, false);
            int injury2 = dao.insertVictimInjury(victim2, dehydration, hospital, true);
            System.out.println("Victim injuries inserted, IDs = " + injury1 + ", " + injury2);
            System.out.println();

            // ---- Step 2: SELECT ---------------------------------------
            System.out.println("=== Reading data ===");

            System.out.println("Victims of heatwave #" + heatwave + ":");
            for (String line : dao.getVictimsByHeatwave(heatwave)) {
                System.out.println("   " + line);
            }

            System.out.println("Heatwaves in the Netherlands:");
            for (String line : dao.getHeatwavesByCountry("Netherlands")) {
                System.out.println("   " + line);
            }

            System.out.println("Hospitals in Maastricht:");
            for (String line : dao.getHospitalsByCity("Maastricht")) {
                System.out.println("   " + line);
            }
            System.out.println();

            // ---- Step 3: UPDATE ---------------------------------------
            System.out.println("=== Updating data ===");

            int rows = dao.updateHospitalCapacity(hospital, 800);
            System.out.println("Hospital capacity updated, rows changed = " + rows);

            rows = dao.updateHeatwaveMortality(heatwave, 14);
            System.out.println("Heatwave mortality corrected, rows changed = " + rows);

            rows = dao.updateVictimAddress(victim1, "Brusselsestraat 14", "6211 PB");
            System.out.println("Victim address updated, rows changed = " + rows);

            System.out.println("Hospitals in Maastricht after the update:");
            for (String line : dao.getHospitalsByCity("Maastricht")) {
                System.out.println("   " + line);
            }
            System.out.println();

            // ---- Step 4: DELETE ---------------------------------------
            System.out.println("=== Deleting data ===");

            rows = dao.deleteVictim(victim2);
            System.out.println("Victim deleted (with their injuries), rows changed = " + rows);

            System.out.println("Victims of heatwave #" + heatwave + " after the delete:");
            for (String line : dao.getVictimsByHeatwave(heatwave)) {
                System.out.println("   " + line);
            }
            System.out.println();

            // ---- Step 5: row counts -----------------------------------
            System.out.println("=== Row counts ===");
            String[] tables = {"Country", "City", "Geography", "Hospital", "Heatwave",
                               "Prevention", "Infrastructure_Impact", "Victim",
                               "Injury_Type", "Victim_Injury"};
            for (String table : tables) {
                System.out.printf("   %-22s %d%n", table, dao.countRows(table));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
