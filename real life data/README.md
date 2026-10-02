# Real-world data integration

This folder contains the real-world data we used to test our database.

## 2. Integration, cleaning and constraint checks

For the real-world data we used two sources: Eurostat mortality data and ECA&D temperature data. The data was cleaned and combined before inserting it into the database.

The final dataset contains 471 weekly records for Paris and Madrid.

### Schema and constraints

Before inserting the data, we checked if the records matched the constraints in the `Heatwave` table.

We checked:
- `End_Date >= Start_Date`
- `Mortality >= 0`

All 471 records passed these checks, so no changes to the schema were needed.

### Missing data

The two datasets used different values for missing data.

Eurostat uses `:` for missing values. It can also use a `p` flag for provisional values. These were handled during the cleaning process.

ECA&D uses `-9999` for missing temperature values. Rows with missing temperature data or missing quality information were removed.

### Dates

Eurostat gives mortality data per ISO week, while ECA&D gives temperature data per day.

The ISO weeks were converted to actual dates. The daily temperature data was then grouped into weeks so both datasets could be combined.

### Duplicates

The datasets were joined using city and ISO week.

The final combined dataset contains 471 unique city-week records.

### Naming

City and region names were kept consistent between the two datasets so they could be joined correctly.

Paris was linked to Île-de-France and Madrid to Comunidad de Madrid.

### Transformations

The ECA&D temperature data was daily, so it had to be converted to weekly values.

TX was used for the weekly maximum temperature, TN for the weekly minimum temperature and TG for the weekly average temperature.

Only weeks with at least five valid days of temperature data were kept.

After that, the temperature data was joined with the mortality data using city and ISO week.

This resulted in:
- 236 records for Paris
- 235 records for Madrid

### Other decisions

The years 2020 and 2021 were excluded because mortality in those years was strongly affected by COVID-19.

There was no real-world source for `Heat_Index` in the selected datasets, so this value was set to `0`.

## 3. Re-running the Week 3 queries

The example queries from week 3 were run again after adding the real-world data.

### Query 1 - Heatwave statistics by country

The query still worked after adding the real-world data.

France returned 238 records and Spain 237 records. Italy and the Netherlands still only contained the existing mock data.

We noticed that the column name `Total_Heatwaves` was a bit misleading, because the real-world data contains one record per city and week, not one record per separate heatwave event.

Because of this, we changed the name to `Total_Weekly_Records`.

The rest of the query did not need to be changed.

### Query 2 - Temperatures above the country average

The query also worked after adding the real-world data.

It returned records where the maximum temperature was above the average maximum temperature for that country.

The original query also returned mock-data records from cities such as Rome, Lyon and Valencia.

To better test the real-world data, the query was adapted to only include Paris and Madrid.

The adapted query returned the expected results, so the joins and temperature data are working correctly.

### Query 3 - Injury survival statistics

This query still worked after adding the real-world data.

It returned the existing injury and survival statistics from the database.

The real-world datasets used in this assignment do not contain individual victim or injury data. Because of this, the new real-world data did not change the results of this query.

The query itself did not need to be changed.

### Conclusion

The Week 3 queries still work with the real-world data.

Some small changes were needed to make the results easier to interpret. In particular, the first query was renamed to better describe the weekly records, and the second query was filtered to Paris and Madrid when testing the real-world data.

The third query still works, but is only based on the existing mock victim and injury data.