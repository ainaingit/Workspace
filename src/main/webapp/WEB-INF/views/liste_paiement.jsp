<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.workspace.entity.Payment" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Paiements</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>

<!-- Menu de navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Admin Panel</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="/admin/dashboard">Dashboard</a></li>
                <li class="nav-item"><a class="nav-link active" href="/admin/paiement">Paiements</a></li>
                <li class="nav-item"><a class="nav-link" href="/admin/reservations">Réservations</a></li>
                <li class="nav-item"><a class="nav-link" href="/logout">Déconnexion</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-4">
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
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
