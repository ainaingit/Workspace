<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.workspace.vue.DiviseHours" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Top des Créneaux Horaires</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        body {
            display: flex;
            min-height: 100vh;
            background-color: #f5f5dc; /* Cream */
        }
        .sidebar {
            width: 250px;
            background-color: #001f3f; /* Navy */
            color: white;
            padding-top: 30px;
            position: fixed;
            height: 100%;
            border-radius: 0 15px 15px 0;
            box-shadow: 3px 0 10px rgba(0, 0, 0, 0.2);
        }
        .sidebar a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 15px;
            margin: 10px;
            border-radius: 5px;
            transition: background 0.3s;
            text-align: center;
        }
        .sidebar a:hover {
            background-color: #003366;
        }
        .content {
            margin-left: 270px;
            padding: 30px;
            flex: 1;
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        h1 {
            font-family: 'Arial', sans-serif;
            color: #001f3f;
            text-align: center;
        }
    </style>
</head>
<body>

<!-- Menu vertical -->
<div class="sidebar">
    <h3 class="text-center">Menu Admin</h3>
    <a href="/importerdonnees" class="btn btn-light">Importer les données</a>
    <a href="/admin/valider-paiement" class="btn btn-light">Valider les paiements</a>
    <a href="/admin/statistiques" class="btn btn-light">Statistiques</a>
    <a href="/admin/divise-hours" class="btn btn-light">Top Créneaux</a>
    <a href="/logout" class="btn btn-light">Déconnexion</a>
</div>

<!-- Contenu principal -->
<div class="content">
    <h1>Top des Créneaux Horaires</h1>
    <p class="text-center">Voici le nombre de réservations pour chaque créneau horaire (de 08:00 à 18:00), trié par ordre croissant du nombre de réservations.</p>

    <div class="table-responsive">
        <table class="table table-bordered table-striped text-center">
            <thead class="table-dark">
            <tr>
                <th>Rang</th> <!-- Nouvelle colonne pour le rang -->
                <th>Créneau Horaire</th>
                <th>Nombre de Réservations</th>
            </tr>
            </thead>
            <tbody>
            <%
                // Récupération de la liste envoyée dans le modèle
                List<DiviseHours> listeDiviseHours = (List<DiviseHours>) request.getAttribute("diviseHours");
                if (listeDiviseHours != null) {
                    for (DiviseHours entry : listeDiviseHours) {
            %>
            <tr>
                <td><%= entry.getRank() %></td> <!-- Affichage du rang -->
                <td><%= entry.getHour_slot() %></td>
                <td><%= entry.getReservations_count() %></td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
    <!-- Affichage du nombre de créneaux -->
    <p class="text-center">Nombre total de créneaux horaires : <%= listeDiviseHours != null ? listeDiviseHours.size() : 0 %></p>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
