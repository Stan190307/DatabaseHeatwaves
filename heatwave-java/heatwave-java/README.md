# DatabaseHeatwaves

## Setup
1. In MySQL Workbench, execute `schema.sql` to create the `heatwave_db` database.
2. In `DatabaseConnection.java`, replace `REPLACE_WITH_YOUR_OWN_PASSWORD` with your own local MySQL password. Don't commit your real password.
3. Run `Main.java`, it tests the connection and runs CRUD operations (insert, read, update, delete).

## Modifying queries
Edit `HeatwaveDAO.java` to add or change SQL queries.

## Real-world data

The `real life data` folder contains real-world data integrated into the database for Week 4:

- `real_world_data.sql` — 471 `INSERT INTO Heatwave` statements combining real weekly mortality and real temperature data for Paris and Madrid, 2022–2026.
- `eurostat_mortality_clean.csv` — cleaned weekly death counts (intermediate file, kept for documentation).
- `heatwave_joined.csv` — the final joined mortality + temperature dataset, one row per (city, week), before SQL generation.
- `raw/` — original downloaded files (Eurostat TSV, ECA&D station files) kept for source documentation.

**Sources:**
- **Mortality:** Eurostat, dataset `demo_r_mwk3_t` (weekly deaths by NUTS region), regions FR10 (Île-de-France) and ES30 (Comunidad de Madrid). License: CC BY 4.0. Downloaded via the [Eurostat bulk download facility](https://ec.europa.eu/eurostat/databrowser/bulk), 2026-10-02.
- **Temperature:** [ECA&D](https://www.ecad.eu) blended daily data, elements TX (max), TN (min), TG (mean), stations Orly (STAID 11249, Paris) and Madrid Retiro (STAID 230, Madrid). License: free for non-commercial research and education (Klein Tank et al., 2002). Downloaded 2026-10-02.


**Mapping to the schema:** each (city, week) is one row in `Heatwave` — no individual victims are fabricated.
- Eurostat weekly death count → `Mortality`
- ECA&D TN/TX/TG aggregated per week → `Minimal_Temperature` / `Maximum_Temperature` / `Avg_Temperature`
- `Heat_Index` has no real-data source in this dataset — set to 0 for all real-world rows (documented limitation).


**To load it:**
1. Run `schema.sql` first (creates all tables).
2. Make sure Paris and Madrid exist in `Country`/`City`/`Geography`. If they don't yet, run this first:
   ```sql
   INSERT INTO Country (Name) VALUES ('France'), ('Spain');
   INSERT INTO City (Name, Region, Country_Code) VALUES
     ('Paris',  'Île-de-France',       (SELECT Country_ID FROM Country WHERE Name='France')),
     ('Madrid', 'Comunidad de Madrid', (SELECT Country_ID FROM Country WHERE Name='Spain'));
   INSERT INTO Geography (City_ID, Latitude, Longitude, Closest_Weather_Station) VALUES
     ((SELECT City_ID FROM City WHERE Name='Paris'),  48.8566, 2.3522,  'Paris Orly'),
     ((SELECT City_ID FROM City WHERE Name='Madrid'), 40.4168, -3.7038, 'Madrid Barajas');
   ```
3. Run `real life data/real_world_data.sql`. It looks up each row's `Location_ID` via a subquery on `City.Name`, so it doesn't need hardcoded IDs — it just needs Paris/Madrid to already exist (step 2).
4. Verify with:
   ```sql
   SELECT COUNT(*) FROM Heatwave;
   ```