DROP VIEW IF EXISTS reservation_details;

CREATE OR REPLACE VIEW reservation_details AS
SELECT
    r.id AS reservation_id,
    r.date AS reservation_date,
    r.start_hour AS start_hour,
    r.start_hour + r.duration AS end_hour,  -- Heure de fin calculée en fonction de l'heure de début et de la durée
    r.duration AS duration,
    -- Calcul du montant total en additionnant le prix de l'espace et des options
    (w.price + COALESCE(SUM(o.price), 0)) AS total_amount,
    r.status AS status,  -- Statut directement récupéré de la table reservation
    -- Utilisation de STRING_AGG pour concaténer les noms des options
    STRING_AGG(o.name, ', ' ORDER BY o.name) AS options_names,
    r.client_id AS client_id  -- Ajouter l'ID du client
FROM
    reservation r
        JOIN workspace w ON r.workspace_id = w.id  -- Joindre les informations de l'espace de travail
        LEFT JOIN reservation_option ro ON r.id = ro.reservation_id  -- Joindre les options de la réservation
        LEFT JOIN option o ON ro.option_id = o.id  -- Joindre les options
GROUP BY
    r.id, r.date, r.start_hour, r.duration, w.price, r.status, r.client_id;


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

*****************************/

CREATE OR REPLACE VIEW vue_chiffre_affaire_total AS
SELECT
    SUM(CASE WHEN r.status IN ('FAIT', 'PAYE') THEN r.total_amount ELSE 0 END) AS montant_paye,
    SUM(CASE WHEN r.status IN ('A_PAYER', 'EN ATTENTE') THEN r.total_amount ELSE 0 END) AS montant_a_payer,
    SUM(CASE WHEN r.status IN ('FAIT', 'PAYE') THEN r.total_amount ELSE 0 END) +
    SUM(CASE WHEN r.status IN ('A_PAYER', 'EN ATTENTE') THEN r.total_amount ELSE 0 END) AS chiffre_affaire_total
FROM
    reservation_details r;

select * from vue_chiffre_affaire_total;