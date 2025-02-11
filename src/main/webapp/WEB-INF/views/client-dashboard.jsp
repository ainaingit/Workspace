<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.workspace.entity.Workspace" %>
<%@ page import="com.example.workspace.entity.Reservation" %>
<%@ page import="com.example.workspace.model.DashboardModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord Client</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        body {
            display: flex;
            min-height: 100vh;
            background-color: #f5f5dc;
        }
        .sidebar {
            width: 260px;
            background-color: #001f3f;
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
            margin-left: 280px;
            padding: 30px;
            flex: 1;
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-family: 'Arial', sans-serif;
            color: #001f3f;
            text-align: center;
        }
        .table-container {
            margin-top: 30px;
        }
        .bg-danger { background-color: #dc3545 !important; color: white; }
        .bg-purple { background-color: #6a0dad !important; color: white; }
        .bg-warning { background-color: #ffc107 !important; }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu Client</h3>
    <a href="/client-dashboard" class="btn btn-primary">📌 Espaces de Travail</a>
    <a href="/mesreservation" class="btn btn-primary">📅 Mes Réservations</a>
</div>

<!-- Content -->
<div class="content">
    <h1>📊 Tableau de bord Client</h1>
    <p class="text-center">Bienvenue dans votre espace client. Sélectionnez une date pour voir les disponibilités.</p>

    <!-- Sélection de date -->
    <div class="text-center mt-4">
        <h3>🔎 Sélectionnez une date</h3>
        <form id="dateForm" class="form-inline justify-content-center">
            <button type="button" id="prevDate" class="btn btn-secondary">◀ Précédent</button>
            <input type="date" id="date" name="date" class="form-control" required>
            <button type="button" id="submitDate" class="btn btn-primary ml-3">🔍 Voir</button>
            <button type="button" id="nextDate" class="btn btn-secondary ml-3">Suivant ▶</button>
        </form>
    </div>

    <!-- Tableau des espaces -->
    <div class="table-container" id="workspaceTable">
        <table class="table table-bordered mt-4 text-center">
            <thead class="table-dark">
            <tr>
                <th>Espaces</th>
                <% for (int hour = 8; hour <= 18; hour++) { %>
                <th><%= hour %>h</th>
                <% } %>
                <th>Réserver</th>
            </tr>
            </thead>
            <tbody>
            <%
                DashboardModel dashboardModel = (DashboardModel) request.getAttribute("dashboardModel");
                List<Workspace> espacesDeTravail = dashboardModel.getListeWorkspace();
                Map<Long, Map<Integer, String>> cellStyles = dashboardModel.getCellStyles();
                Map<Long, Map<Integer, String>> hourSymbols = dashboardModel.getHourSymbols();

                for (Workspace espace : espacesDeTravail) {
            %>
            <tr>
                <td><%= espace.getName() %></td>
                <% for (int hour = 8; hour <= 18; hour++) {
                    // Récupérer la classe et le symbole pour chaque heure
                    String cellClass = cellStyles.get(espace.getId()).get(hour);
                    String symbol = hourSymbols.get(espace.getId()).get(hour);
                %>
                <td class="<%= cellClass %>">
                    <%= symbol %>
                </td>
                <% } %>
                <td>
                    <form action="/reserver" method="POST" class="reservationForm">
                        <input type="hidden" name="workspaceId" value="<%= espace.getId() %>" />
                        <input type="hidden" name="workspaceName" value="<%= espace.getName() %>" />
                        <input type="hidden" name="selectedDate" id="selectedDate" value="<%= request.getParameter("date") != null ? request.getParameter("date") : "" %>" />
                        <button type="submit" class="btn btn-success">✔ Réserver</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<script>
    function getDateFromURL() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('date');
    }

    document.addEventListener("DOMContentLoaded", function() {
        const dateInput = document.getElementById('date');
        const selectedDate = getDateFromURL();

        if (selectedDate) {
            dateInput.value = selectedDate;
        }

        const reservationForms = document.querySelectorAll('.reservationForm');
        reservationForms.forEach(form => {
            form.addEventListener('submit', function(event) {
                const selectedDate = dateInput.value;
                if (!selectedDate) {
                    event.preventDefault();
                    alert("Veuillez sélectionner une date avant de réserver.");
                }
            });
        });
    });

    document.getElementById('submitDate').addEventListener('click', function() {
        const selectedDate = document.getElementById('date').value;
        if (selectedDate) {
            window.location.href = '/client-dashboard?date=' + selectedDate;
        }
    });

    // Navigation avec les boutons Précédent et Suivant
    document.getElementById('prevDate').addEventListener('click', function() {
        const selectedDate = document.getElementById('date').value;
        const date = new Date(selectedDate);
        date.setDate(date.getDate() - 1); // Recule d'un jour
        const newDate = date.toISOString().split('T')[0]; // Format YYYY-MM-DD
        window.location.href = '/client-dashboard?date=' + newDate;
    });

    document.getElementById('nextDate').addEventListener('click', function() {
        const selectedDate = document.getElementById('date').value;
        const date = new Date(selectedDate);
        date.setDate(date.getDate() + 1); // Avance d'un jour
        const newDate = date.toISOString().split('T')[0]; // Format YYYY-MM-DD
        window.location.href = '/client-dashboard?date=' + newDate;
    });
</script>

</body>
</html>
