
-- Query 1
-- Author: Finn Osterop
--
-- Question:
-- Which cities have the highest average maximum temperature
-- and what is their average mortality?
--
-- Relevance:
-- This helps identify cities that experience the most extreme
-- heat and shows the mortality observed during those periods.


SELECT
    c.Name AS City,
    co.Name AS Country,
    ROUND(AVG(h.Maximum_Temperature), 2) AS Avg_Max_Temperature,
    ROUND(AVG(h.Mortality), 2) AS Avg_Mortality,
    COUNT(h.Heatwave_ID) AS Number_Of_Records
FROM Heatwave h
JOIN Geography g ON h.Location_ID = g.Location_ID
JOIN City c ON g.City_ID = c.City_ID
JOIN Country co ON c.Country_Code = co.Country_ID
GROUP BY c.Name, co.Name
ORDER BY Avg_Max_Temperature DESC;


-- Query 2
-- Author: Finnished
--
-- Question:
-- Is average mortality higher during weeks where the maximum
-- temperature reached at least 35 degrees Celsius?
--
-- Relevance:
-- This helps explore whether periods of extreme heat are
-- associated with higher mortality, which is one of the main
-- societal effects investigated by this database.


SELECT
    CASE
        WHEN Maximum_Temperature >= 35 THEN '35 C or higher'
        ELSE 'Below 35 C'
    END AS Temperature_Group,
    COUNT(*) AS Number_Of_Records,
    ROUND(AVG(Mortality), 2) AS Avg_Mortality
FROM Heatwave
GROUP BY
    CASE
        WHEN Maximum_Temperature >= 35 THEN '35 C or higher'
        ELSE 'Below 35 C'
    END
ORDER BY Avg_Mortality DESC;