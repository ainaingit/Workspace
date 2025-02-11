<%@ page import="com.example.workspace.vue.ReservationDetails" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Réservations</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            min-height: 100vh;
            background-color: #f8f9fa; /* Couleur de fond claire */
        }
        .sidebar {
            width: 250px;
            background-color: #343a40; /* Couleur foncée pour le menu */
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
            background-color: #495057;
        }
        .content {
            margin-left: 270px;
            padding: 30px;
            flex: 1;
            background-color: #ffffff;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-family: 'Arial', sans-serif;
            color: #343a40;
            text-align: center;
        }
        .table-container {
            margin-top: 30px;
        }
        .table th, .table td {
            text-align: center;
        }
        .btn-custom {
            width: 180px;
            margin: 10px auto;
            display: block;
            background-color: #007bff;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 5px;
        }
        .btn-custom:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu</h3>
    <a href="/client-dashboard" class="btn btn-custom">Espaces de Travail</a>
    <a href="/mesreservation" class="btn btn-custom">Mes Réservations</a>
</div>

<!-- Content -->
<div class="content">
    <h1>Mes Réservations</h1>

    <div class="table-container">
        <table class="table table-bordered text-center">
            <thead class="table-dark">
            <tr>
                <th>ID Réservation</th>
                <th>Date</th>
                <th>Heure de début</th>
                <th>Heure de fin</th>
                <th>Durée</th>
                <th>Nom de l'espace</th>
                <th>Options</th>
                <th>Montant total</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<ReservationDetails> reservations = (List<ReservationDetails>) request.getAttribute("reservations");
                for (ReservationDetails res : reservations) {
                    String status = res.getStatus();
            %>
            <tr>
                <td><%= res.getReservationId() %></td>
                <td><%= res.getReservationDate() %></td>
                <td><%= res.getStartHour() %></td>
                <td><%= res.getEndHour() %></td>
                <td><%= res.getDuration() %>h</td>
                <td><%= res.getWorkspaceName() %></td>
                <td><%= res.getOptionsNames() %></td>
                <td><%= res.getTotalAmount() %> €</td>
                <td>
                    <% if ("PAYE".equals(status)) { %>
                    <span class="badge bg-success">Déjà payé</span>
                    <% } else if ("EN_ATTENTE".equals(status)) { %>
                    <span class="badge bg-warning text-dark">En attente</span>
                    <% } %>
                </td>
                <td>
                    <% if (!"PAYE".equals(status) && !"EN_ATTENTE".equals(status)) { %>
                    <!-- Bouton Payer -->
                    <form action="/payerReservation" method="post">
                        <input type="hidden" name="reservationId" value="<%= res.getReservationId() %>">
                        <button type="submit" class="btn btn-custom">Payer</button>
                    </form>
                    <!-- Bouton Annuler -->
                    <form action="/annulerReservation" method="post">
                        <input type="hidden" name="reservationId" value="<%= res.getReservationId() %>">
                        <button type="submit" class="btn btn-danger mt-2">Annuler</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Si un message est présent dans le modèle, afficher une alerte -->
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<script>
    alert("<%= message %>");
</script>
<%
    }
%>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
