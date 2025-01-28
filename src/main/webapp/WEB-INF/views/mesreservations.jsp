<%@ page import="com.example.workspace.objet.ReservationDetails" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Réservations</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <h2>Mes Réservations</h2>
        <table class="table table-bordered">
            <thead>
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
                <th>!</th>
                <th>!</th>
            </tr>
            </thead>
            <tbody>
            <%
                // Supposons que vous ayez une liste d'espaces dans le modèle
                List<ReservationDetails> espacesDeTravail = (List<ReservationDetails>) request.getAttribute("reservations");
                for (int i = 0; i < espacesDeTravail.size(); i++) {
                    ReservationDetails res  = espacesDeTravail.get(i);

            %>
            <tr>
            <td><%= res.getReservationId() %></td>
            <td><%= res.getReservationDate() %></td>
            <td><%= res.getStartHour()%></td>
            <td><%= res.getEndHour() %></td>
            <td><%= res.getDuration() %></td>
            <td><%= res.getWorkspaceName()%></td>
            <td><%= res.getOptionsNames()%></td>
            <td><%= res.getTotalAmount() %></td>
            <td><%= res.getStatus()%></td>
            <td>
                <!-- Formulaire pour le paiement avec l'ID de la réservation -->
                <form action="Payer" method="post">
                    <input type="hidden" name="reservationId" value="<%= res.getReservationId() %>">
                    <button type="submit">Payer</button>
                </form>
            </td>
            <td>
                <!-- Formulaire pour annuler avec l'ID de la réservation -->
                <form action="Annuler" method="post">
                    <input type="hidden" name="reservationId" value="<%= res.getReservationId() %>">
                    <button type="submit">Annuler</button>
                </form>
            </td>
            <% } %>
            </tbody>
        </table>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
