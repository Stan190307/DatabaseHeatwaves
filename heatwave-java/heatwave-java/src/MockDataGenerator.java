import java.sql.SQLException;
import java.time.LocalDate;

public class MockDataGenerator {

    public static void main(String[] args) {

        HeatwaveDAO dao = new HeatwaveDAO();

        try {
            System.out.println("=== Generating mock data ===");

            // ============================================================
            // 1. COUNTRIES
            // ============================================================

            int netherlands = dao.insertCountry("Netherlands");
            int france = dao.insertCountry("France");
            int spain = dao.insertCountry("Spain");
            int italy = dao.insertCountry("Italy");


            // ============================================================
            // 2. CITIES
            // ============================================================

            int maastricht = dao.insertCity(
                "Maastricht", "Limburg", netherlands
            );

            int amsterdam = dao.insertCity(
                "Amsterdam", "North Holland", netherlands
            );

            int paris = dao.insertCity(
                "Paris", "Île-de-France", france
            );

            int lyon = dao.insertCity(
                "Lyon", "Auvergne-Rhône-Alpes", france
            );

            int madrid = dao.insertCity(
                "Madrid", "Community of Madrid", spain
            );

            int valencia = dao.insertCity(
                "Valencia", "Valencian Community", spain
            );

            int rome = dao.insertCity(
                "Rome", "Lazio", italy
            );

            int milan = dao.insertCity(
                "Milan", "Lombardy", italy
            );


            // ============================================================
            // 3. GEOGRAPHY
            // ============================================================

            int maastrichtLocation = dao.insertGeography(
                maastricht,
                50.8514,
                5.6910,
                "Maastricht Aachen Airport"
            );

            int amsterdamLocation = dao.insertGeography(
                amsterdam,
                52.3676,
                4.9041,
                "Schiphol Weather Station"
            );

            int parisLocation = dao.insertGeography(
                paris,
                48.8566,
                2.3522,
                "Paris-Montsouris"
            );

            int lyonLocation = dao.insertGeography(
                lyon,
                45.7640,
                4.8357,
                "Lyon-Bron Weather Station"
            );

            int madridLocation = dao.insertGeography(
                madrid,
                40.4168,
                -3.7038,
                "Madrid Retiro"
            );

            int valenciaLocation = dao.insertGeography(
                valencia,
                39.4699,
                -0.3763,
                "Valencia Airport Weather Station"
            );

            int romeLocation = dao.insertGeography(
                rome,
                41.9028,
                12.4964,
                "Rome Ciampino Weather Station"
            );

            int milanLocation = dao.insertGeography(
                milan,
                45.4642,
                9.1900,
                "Milan Linate Weather Station"
            );


            // ============================================================
            // 4. HOSPITALS
            // ============================================================

            int maastrichtHospital = dao.insertHospital(
                maastricht,
                "Medical Campus 1",
                "6229 AA",
                700
            );

            int amsterdamHospital = dao.insertHospital(
                amsterdam,
                "Health Avenue 15",
                "1105 AZ",
                900
            );

            int parisHospital = dao.insertHospital(
                paris,
                "Rue de Santé 10",
                "75013",
                1200
            );

            int lyonHospital = dao.insertHospital(
                lyon,
                "Avenue Médicale 6",
                "69003",
                750
            );

            int madridHospital = dao.insertHospital(
                madrid,
                "Calle Salud 21",
                "28040",
                1000
            );

            int valenciaHospital = dao.insertHospital(
                valencia,
                "Avenida Hospital 8",
                "46010",
                650
            );

            int romeHospital = dao.insertHospital(
                rome,
                "Via Medica 30",
                "00161",
                950
            );

            int milanHospital = dao.insertHospital(
                milan,
                "Via Salute 12",
                "20133",
                850
            );


            // ============================================================
            // 5. HEATWAVES
            // ============================================================

            int heatwaveMaastricht = dao.insertHeatwave(
                maastrichtLocation,
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 15),
                21.4,
                38.2,
                29.8,
                12,
                41.5
            );

            int heatwaveAmsterdam = dao.insertHeatwave(
                amsterdamLocation,
                LocalDate.of(2025, 6, 28),
                LocalDate.of(2025, 7, 3),
                20.1,
                35.7,
                27.3,
                8,
                38.0
            );

            int heatwaveParis = dao.insertHeatwave(
                parisLocation,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 8),
                24.0,
                41.0,
                32.1,
                48,
                45.0
            );

            int heatwaveLyon = dao.insertHeatwave(
                lyonLocation,
                LocalDate.of(2025, 7, 18),
                LocalDate.of(2025, 7, 25),
                23.0,
                40.2,
                31.3,
                31,
                43.7
            );

            int heatwaveMadrid = dao.insertHeatwave(
                madridLocation,
                LocalDate.of(2025, 7, 5),
                LocalDate.of(2025, 7, 14),
                26.0,
                43.5,
                34.5,
                63,
                47.2
            );

            int heatwaveValencia = dao.insertHeatwave(
                valenciaLocation,
                LocalDate.of(2025, 8, 2),
                LocalDate.of(2025, 8, 9),
                25.1,
                39.8,
                32.0,
                21,
                44.0
            );

            int heatwaveRome = dao.insertHeatwave(
                romeLocation,
                LocalDate.of(2025, 7, 12),
                LocalDate.of(2025, 7, 20),
                25.5,
                42.1,
                33.4,
                37,
                46.1
            );

            int heatwaveMilan = dao.insertHeatwave(
                milanLocation,
                LocalDate.of(2025, 7, 20),
                LocalDate.of(2025, 7, 27),
                24.3,
                39.6,
                31.0,
                25,
                43.2
            );


            // ============================================================
            // 6. PREVENTION
            // ============================================================

            int preventionMaastricht = dao.insertPrevention(
                heatwaveMaastricht,
                maastrichtLocation,
                "Cooling centres opened in public buildings",
                true
            );

            int preventionAmsterdam = dao.insertPrevention(
                heatwaveAmsterdam,
                amsterdamLocation,
                "Public heat warnings and free water points",
                true
            );

            int preventionParis = dao.insertPrevention(
                heatwaveParis,
                parisLocation,
                "Emergency cooling centres and outreach to elderly residents",
                true
            );

            int preventionLyon = dao.insertPrevention(
                heatwaveLyon,
                lyonLocation,
                "Public information campaign",
                false
            );

            int preventionMadrid = dao.insertPrevention(
                heatwaveMadrid,
                madridLocation,
                "Cooling shelters and adjusted working hours",
                true
            );

            int preventionValencia = dao.insertPrevention(
                heatwaveValencia,
                valenciaLocation,
                "Public hydration stations",
                true
            );

            int preventionRome = dao.insertPrevention(
                heatwaveRome,
                romeLocation,
                "Heat alerts issued to residents",
                false
            );

            int preventionMilan = dao.insertPrevention(
                heatwaveMilan,
                milanLocation,
                "Cooling centres and hospital preparedness measures",
                true
            );


            // Link Prevention back to Heatwave
            dao.linkPreventionToHeatwave(
                heatwaveMaastricht, preventionMaastricht
            );

            dao.linkPreventionToHeatwave(
                heatwaveAmsterdam, preventionAmsterdam
            );

            dao.linkPreventionToHeatwave(
                heatwaveParis, preventionParis
            );

            dao.linkPreventionToHeatwave(
                heatwaveLyon, preventionLyon
            );

            dao.linkPreventionToHeatwave(
                heatwaveMadrid, preventionMadrid
            );

            dao.linkPreventionToHeatwave(
                heatwaveValencia, preventionValencia
            );

            dao.linkPreventionToHeatwave(
                heatwaveRome, preventionRome
            );

            dao.linkPreventionToHeatwave(
                heatwaveMilan, preventionMilan
            );


            // ============================================================
            // 7. INFRASTRUCTURE IMPACT
            // ============================================================

            dao.insertInfrastructureImpact(
                heatwaveMaastricht,
                false,
                true,
                false
            );

            dao.insertInfrastructureImpact(
                heatwaveAmsterdam,
                false,
                false,
                true
            );

            dao.insertInfrastructureImpact(
                heatwaveParis,
                true,
                true,
                true
            );

            dao.insertInfrastructureImpact(
                heatwaveLyon,
                false,
                true,
                true
            );

            dao.insertInfrastructureImpact(
                heatwaveMadrid,
                true,
                true,
                false
            );

            dao.insertInfrastructureImpact(
                heatwaveValencia,
                false,
                true,
                false
            );

            dao.insertInfrastructureImpact(
                heatwaveRome,
                true,
                false,
                true
            );

            dao.insertInfrastructureImpact(
                heatwaveMilan,
                false,
                false,
                true
            );


            // ============================================================
            // 8. INJURY TYPES
            // ============================================================

            int heatstroke = dao.insertInjuryType(
                "Heatstroke",
                "Dangerously elevated body temperature caused by extreme heat"
            );

            int dehydration = dao.insertInjuryType(
                "Severe dehydration",
                "Significant fluid loss caused by prolonged heat exposure"
            );

            int heatExhaustion = dao.insertInjuryType(
                "Heat exhaustion",
                "Weakness, dizziness and nausea caused by high temperatures"
            );

            int fainting = dao.insertInjuryType(
                "Heat syncope",
                "Temporary fainting caused by heat exposure"
            );

            int cardiovascular = dao.insertInjuryType(
                "Cardiovascular complications",
                "Heart or circulatory complications worsened by extreme heat"
            );


            // ============================================================
            // 9. VICTIMS
            // ============================================================

            int victim1 = dao.insertVictim(
                heatwaveMaastricht,
                "Jan",
                "de Vries",
                78,
                "Male",
                maastricht,
                "Example Street 12",
                "6211 AA"
            );

            int victim2 = dao.insertVictim(
                heatwaveMaastricht,
                "Anna",
                "Bakker",
                83,
                "Female",
                maastricht,
                "Market Street 8",
                "6211 BB"
            );

            int victim3 = dao.insertVictim(
                heatwaveAmsterdam,
                "Mohamed",
                "El Amrani",
                55,
                "Male",
                amsterdam,
                "Canal Street 31",
                "1012 AB"
            );

            int victim4 = dao.insertVictim(
                heatwaveAmsterdam,
                "Sophie",
                "Jansen",
                34,
                "Female",
                amsterdam,
                "Park Road 22",
                "1054 AC"
            );

            int victim5 = dao.insertVictim(
                heatwaveParis,
                "Pierre",
                "Martin",
                81,
                "Male",
                paris,
                "Rue Exemple 14",
                "75005"
            );

            int victim6 = dao.insertVictim(
                heatwaveParis,
                "Claire",
                "Bernard",
                72,
                "Female",
                paris,
                "Avenue Exemple 9",
                "75011"
            );

            int victim7 = dao.insertVictim(
                heatwaveLyon,
                "Lucas",
                "Robert",
                41,
                "Male",
                lyon,
                "Rue Centrale 17",
                "69002"
            );

            int victim8 = dao.insertVictim(
                heatwaveLyon,
                "Emma",
                "Petit",
                67,
                "Female",
                lyon,
                "Rue du Parc 6",
                "69006"
            );

            int victim9 = dao.insertVictim(
                heatwaveMadrid,
                "Carlos",
                "Garcia",
                76,
                "Male",
                madrid,
                "Calle Central 28",
                "28013"
            );

            int victim10 = dao.insertVictim(
                heatwaveMadrid,
                "Lucia",
                "Martinez",
                69,
                "Female",
                madrid,
                "Calle Norte 15",
                "28020"
            );

            int victim11 = dao.insertVictim(
                heatwaveValencia,
                "Javier",
                "Lopez",
                28,
                "Male",
                valencia,
                "Calle Mar 5",
                "46002"
            );

            int victim12 = dao.insertVictim(
                heatwaveValencia,
                "Elena",
                "Sanchez",
                74,
                "Female",
                valencia,
                "Avenida Sol 19",
                "46005"
            );

            int victim13 = dao.insertVictim(
                heatwaveRome,
                "Marco",
                "Rossi",
                82,
                "Male",
                rome,
                "Via Centrale 7",
                "00184"
            );

            int victim14 = dao.insertVictim(
                heatwaveRome,
                "Giulia",
                "Romano",
                63,
                "Female",
                rome,
                "Via Verde 23",
                "00198"
            );

            int victim15 = dao.insertVictim(
                heatwaveMilan,
                "Luca",
                "Bianchi",
                45,
                "Male",
                milan,
                "Via Milano 18",
                "20121"
            );

            int victim16 = dao.insertVictim(
                heatwaveMilan,
                "Sara",
                "Conti",
                79,
                "Female",
                milan,
                "Via Nord 11",
                "20124"
            );


            // ============================================================
            // 10. VICTIM INJURIES
            // ============================================================

            dao.insertVictimInjury(
                victim1,
                heatstroke,
                maastrichtHospital,
                false
            );

            dao.insertVictimInjury(
                victim2,
                dehydration,
                maastrichtHospital,
                true
            );

            dao.insertVictimInjury(
                victim3,
                heatExhaustion,
                amsterdamHospital,
                true
            );

            dao.insertVictimInjury(
                victim4,
                fainting,
                amsterdamHospital,
                true
            );

            dao.insertVictimInjury(
                victim5,
                cardiovascular,
                parisHospital,
                false
            );

            dao.insertVictimInjury(
                victim6,
                heatstroke,
                parisHospital,
                true
            );

            dao.insertVictimInjury(
                victim7,
                dehydration,
                lyonHospital,
                true
            );

            dao.insertVictimInjury(
                victim8,
                heatExhaustion,
                lyonHospital,
                true
            );

            dao.insertVictimInjury(
                victim9,
                heatstroke,
                madridHospital,
                false
            );

            dao.insertVictimInjury(
                victim10,
                cardiovascular,
                madridHospital,
                true
            );

            dao.insertVictimInjury(
                victim11,
                fainting,
                valenciaHospital,
                true
            );

            dao.insertVictimInjury(
                victim12,
                dehydration,
                valenciaHospital,
                true
            );

            dao.insertVictimInjury(
                victim13,
                cardiovascular,
                romeHospital,
                false
            );

            dao.insertVictimInjury(
                victim14,
                heatExhaustion,
                romeHospital,
                true
            );

            dao.insertVictimInjury(
                victim15,
                dehydration,
                milanHospital,
                true
            );

            dao.insertVictimInjury(
                victim16,
                heatstroke,
                milanHospital,
                true
            );


            System.out.println();
            System.out.println("Mock data inserted successfully!");

        } catch (SQLException e) {

            System.out.println(
                "Error inserting mock data: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}