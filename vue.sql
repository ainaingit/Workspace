CREATE OR REPLACE VIEW workspace_availability AS
SELECT
    w.id AS workspace_id,
    w.name AS workspace_nom,
    h.heure AS heure,
    COALESCE(DATE(r.end_time), CURRENT_DATE) AS date_reservation, -- Colonne date extraite de r.end_time
    CASE
        WHEN r.id IS NOT NULL THEN
            CASE
                WHEN r.status = 'PAYE' THEN 'occupé'
                WHEN r.status = 'A_PAYER' THEN 'réservé'
                WHEN r.status = 'EN_ATTENTE' THEN 'en attente'
                ELSE 'occupé'
                END
        ELSE 'libre'
        END AS statut
FROM
    generate_series(8, 17) AS h(heure) -- Génère les heures de 8h à 17h
        CROSS JOIN
    workspace w
        LEFT JOIN
    reservation r
    ON
        w.id = r.workspace_id
            AND DATE(r.end_time) = CURRENT_DATE -- Remplace par une date dynamique si nécessaire
            AND h.heure >= EXTRACT(HOUR FROM r.start_time)
            AND h.heure < EXTRACT(HOUR FROM r.end_time);


