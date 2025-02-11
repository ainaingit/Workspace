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
    (w.price * r.duration + COALESCE(SUM(o.price * r.duration), 0)) AS total_amount,  -- Correction ici
    r.status AS status,
    COALESCE(STRING_AGG(o.name, ', ' ORDER BY o.name), 'Aucune option') AS options_names,  -- Liste des options sous forme de texte
    r.client_id AS client_id,
    w.name AS workspace_name  -- Nom de l'espace de travail
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
    SUM(CASE WHEN rd.status = 'PAYE' THEN rd.total_amount ELSE 0 END) AS chiffre_affaire_payes,
    SUM(CASE WHEN rd.status != 'PAYE' THEN rd.total_amount ELSE 0 END) AS chiffre_affaire_non_payes,
    SUM(rd.total_amount) AS chiffre_affaire_total  -- Somme des deux
FROM reservation_details rd
GROUP BY rd.reservation_date
ORDER BY rd.reservation_date;

SELECT * from  v_chiffre_affaire_par_jour ;

/**         **/

CREATE OR REPLACE VIEW vue_chiffre_affaire_total AS
SELECT
    -- Montant payé : Seuls les statuts 'PAYE' sont pris en compte
    SUM(CASE WHEN r.status = 'PAYE' THEN r.total_amount ELSE 0 END) AS montant_paye,

    -- Montant à payer : Tous les statuts autres que 'PAYE' sont considérés comme à payer
    SUM(CASE WHEN r.status != 'PAYE' THEN r.total_amount ELSE 0 END) AS montant_a_payer,

    -- Chiffre d'affaires total : Somme des montants payés et non payés
    SUM(r.total_amount) AS chiffre_affaire_total
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
     ),
     RankedCounts AS (
         -- Ajouter un classement basé sur le nombre de réservations
         SELECT
             f.hour_slot,
             COALESCE(h.reservations_count, 0) AS reservations_count,
             RANK() OVER (ORDER BY COALESCE(h.reservations_count, 0) DESC) AS rank
         FROM FullHours f
                  LEFT JOIN HourlyCounts h ON f.hour_slot = h.hour_slot
     )
SELECT
    hour_slot,
    reservations_count,
    rank
FROM RankedCounts
ORDER BY rank, hour_slot;  -- Trier d'abord par rang, puis par créneau horaire


TRUNCATE TABLE reservation_option, reservation, option, workspace, client,payment RESTART IDENTITY CASCADE;
