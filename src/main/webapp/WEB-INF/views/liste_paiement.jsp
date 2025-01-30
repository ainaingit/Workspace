<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.workspace.entity.Payment" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Liste des Paiements</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .sidebar {
            height: 100vh;
            position: fixed;
            width: 250px;
            background-color: #343a40;
            padding-top: 20px;
        }
        .sidebar .nav-link {
            color: white;
        }
        .sidebar .nav-link:hover {
            background-color: #495057;
        }
        .content {
            margin-left: 270px;
            padding: 20px;
        }
    </style>
</head>
<body>

<!-- Menu latéral -->
<div class="sidebar">
    <h4 class="text-white text-center">Admin Panel</h4>
    <ul class="nav flex-column">
        <li class="nav-item"><a class="nav-link" href="/admin/dashboard">Dashboard</a></li>
        <li class="nav-item"><a class="nav-link active bg-primary" href="/admin/paiement">Paiements</a></li>
        <li class="nav-item"><a class="nav-link" href="/admin/reservations">Réservations</a></li>
        <li class="nav-item"><a class="nav-link" href="/logout">Déconnexion</a></li>
    </ul>
</div>

<!-- Contenu principal -->
<div class="content">
    <h2 class="mb-3">Liste des Paiements</h2>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Référence</th>
            <th>Date</th>
            <th>Mode de Paiement</th>
            <th>Statut</th>
            <th>Réservation</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Payment> liste = (List<Payment>) request.getAttribute("listePayment");
            if (liste != null) {
                for (int i = 0; i < liste.size(); i++) {
                    Payment paiement = liste.get(i);
        %>
        <tr>
            <td><%= i + 1 %></td>
            <td><%= paiement.getRef_payment() %></td>
            <td><%= paiement.getDate_payment() %></td>
            <td><%= paiement.getMode_payment() %></td>
            <td><%= paiement.getStatut() %></td>
            <td><%= paiement.getReservation() != null ? paiement.getReservation().getRef() : "N/A" %></td>
            <td>
                <% if ("EN_ATTENTE".equals(paiement.getStatut())) { %>
                <form action="/admin/validerPaiement" method="post" style="display:inline;">
                    <input type="hidden" name="paymentId" value="<%= paiement.getId() %>">
                    <button type="submit" class="btn btn-success btn-sm">Valider</button>
                </form>
                <% } else { %>
                <button class="btn btn-secondary btn-sm" disabled>Déjà validé</button>
                <% } %>
            </td>
        </tr>
        <% } } %>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
