<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.example.workspace.entity.Workspace" %>
<%@ page import="com.example.workspace.entity.Reservation" %>
<%@ page import="com.example.workspace.entity.ReservationStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalTime" %>
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
            background-color: #f5f5dc; /* Cream */
        }
        .sidebar {
            width: 260px;
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
        .btn-custom {
            width: 220px;
            margin: 10px auto;
            display: block;
            background-color: #003366;
            color: white;
            font-weight: bold;
        }
        .btn-custom:hover {
            background-color: #001f3f;
        }
        .table-container {
            margin-top: 30px;
        }
        .bg-danger {
            background-color: #dc3545 !important; /* Rouge */
            color: white;
        }
        .bg-purple {
            background-color: #6a0dad !important; /* Violet */
            color: white;
        }
        .bg-warning {
            background-color: #ffc107 !important; /* Jaune */
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu Client</h3>
    <a href="/client/espaces-travail" class="btn btn-custom">📌 Espaces de Travail</a>
    <a href="/mesreservation" class="btn btn-custom">📅 Mes Réservations</a>
</div>

<!-- Content -->
<div class="content">
    <h1>📊 Tableau de bord Client</h1>
    <p class="text-center">Bienvenue dans votre espace client. Sélectionnez une date pour voir les disponibilités.</p>

    <!-- Formulaire de sélection de date -->
    <div class="text-center mt-4">
        <h3>🔎 Sélectionnez une date</h3>
        <form id="dateForm" class="form-inline justify-content-center">
            <div class="form-group">
                <input type="date" id="date" name="date" class="form-control" required>
            </div>
            <button type="button" id="submitDate" class="btn btn-primary ml-3">🔍 Voir</button>
        </form>
    </div>

    <!-- Tableau des espaces de travail -->
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
                List<Workspace> espacesDeTravail = (List<Workspace>) request.getAttribute("liste_workspace");
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");

                for (Workspace espace : espacesDeTravail) {
            %>
            <tr>
                <td><%= espace.getName() %></td>
                <%
                    for (int hour = 8; hour <= 18; hour++) {
                        String cellClass = "bg-warning"; // Par défaut, libre (jaune)

                        for (Reservation res : reservations) {
                            if (res.getWorkspace().getId().equals(espace.getId())) {
                                int startHour = res.getStartHour().getHour();
                                int endHour = startHour + res.getDuration();

                                if (hour >= startHour && hour < endHour) {
                                    if (res.getStatus() == ReservationStatus.PAYE) {
                                        cellClass = "bg-danger"; // Rouge (PAYE)
                                    } else {
                                        cellClass = "bg-purple"; // Violet (autres statuts)
                                    }
                                    break;
                                }
                            }
                        }
                %>
                <td class="<%= cellClass %>"></td>
                <% } %>
                <td>
                    <form action="/reserver" method="POST">
                        <input type="hidden" name="workspaceId" value="<%= espace.getId() %>" />
                        <input type="hidden" name="workspaceName" value="<%= espace.getName() %>" />
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
    document.getElementById('submitDate').addEventListener('click', function() {
        const selectedDate = document.getElementById('date').value;
        if (selectedDate) {
            window.location.href = '/client-dashboard?date=' + selectedDate;
        }
    });
</script>

</body>
</html>
