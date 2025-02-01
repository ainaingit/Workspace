CREATE SEQUENCE reservation_ref_seq
    START WITH 1
    INCREMENT BY 1;


DROP VIEW IF EXISTS reservation_details;

CREATE OR REPLACE VIEW reservation_details AS
SELECT
    r.id AS reservation_id,
    r.date AS reservation_date,
    r.start_hour AS start_hour,
    (r.start_hour + INTERVAL '1 hour' * r.duration) AS end_hour,  -- Calcul de l'heure de fin
    r.duration AS duration,
    (w.price + COALESCE(SUM(o.price), 0)) AS total_amount,
    r.status AS status,
    STRING_AGG(o.name, ', ' ORDER BY o.name) AS options_names,
    r.client_id AS client_id,
    w.name AS workspace_name  -- Ajout du nom de l'espace de travail
FROM
    reservation r
        JOIN workspace w ON r.workspace_id = w.id
        LEFT JOIN reservation_option ro ON r.id = ro.reservation_id
        LEFT JOIN option o ON ro.option_id = o.id
GROUP BY
    r.id, r.date, r.start_hour, r.duration, w.price, r.status, r.client_id, w.name;


/** chiffre d affaire par jour , avec filtre de date **/
CREATE OR REPLACE VIEW v_chiffre_affaire_par_jour AS
SELECT
    rd.reservation_date AS date_paiement,
    SUM(rd.total_amount) AS chiffre_affaire
FROM reservation_details rd
WHERE rd.status = 'PAYE'  -- Filtrer uniquement les réservations payées
GROUP BY rd.reservation_date
ORDER BY rd.reservation_date;

SELECT * from  v_chiffre_affaire_par_jour ;

/**         **/

CREATE OR REPLACE VIEW vue_chiffre_affaire_total AS
SELECT
    SUM(CASE WHEN r.status IN ('FAIT', 'PAYE') THEN r.total_amount ELSE 0 END) AS montant_paye,
    SUM(CASE WHEN r.status IN ('A_PAYER', 'EN ATTENTE') THEN r.total_amount ELSE 0 END) AS montant_a_payer,
    SUM(CASE WHEN r.status IN ('FAIT', 'PAYE') THEN r.total_amount ELSE 0 END) +
    SUM(CASE WHEN r.status IN ('A_PAYER', 'EN ATTENTE') THEN r.total_amount ELSE 0 END) AS chiffre_affaire_total
FROM
    reservation_details r;

select * from vue_chiffre_affaire_total;


/** savoir le creneau d heure le plus afflient **/

CREATE OR REPLACE VIEW divise_hours AS
WITH ReservationSlots AS (
    -- Pour chaque réservation, générer les créneaux horaires occupés
    SELECT
        generate_series(
                (r.date + r.start_hour),
                (r.date + r.start_hour + INTERVAL '1 hour' * (r.duration - 1)),
                '1 hour'::interval
        ) AS occupied_hour
    FROM reservation r
),
     HourlyCounts AS (
         -- Compter le nombre de réservations pour chaque créneau (filtré entre 08:00 et 18:00)
         SELECT
             TO_CHAR(occupied_hour, 'HH24:00') AS hour_slot,
             COUNT(*) AS reservations_count
         FROM ReservationSlots
         WHERE occupied_hour::time BETWEEN '08:00:00' AND '18:00:00'
         GROUP BY TO_CHAR(occupied_hour, 'HH24:00')
     ),
     FullHours AS (
         -- Générer tous les créneaux horaires de 08:00 à 18:00 (en utilisant une date factice)
         SELECT TO_CHAR(gs.hour_slot_ts, 'HH24:00') AS hour_slot
         FROM (
                  SELECT generate_series(
                                 '2000-01-01 08:00:00'::timestamp,
                                 '2000-01-01 18:00:00'::timestamp,
                                 '1 hour'::interval
                         ) AS hour_slot_ts
              ) gs
     )
SELECT
    f.hour_slot,
    COALESCE(h.reservations_count, 0) AS reservations_count
FROM FullHours f
         LEFT JOIN HourlyCounts h ON f.hour_slot = h.hour_slot
ORDER BY f.hour_slot;
