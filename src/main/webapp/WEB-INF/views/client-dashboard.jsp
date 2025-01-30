<%@ page import="com.example.workspace.entity.Workspace" %>
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
            background-color: #e9ecef;
        }
        .sidebar {
            width: 250px;
            background-color: #6c757d;
            color: white;
            padding-top: 30px;
            position: fixed;
            height: 100%;
            border-radius: 0 15px 15px 0;
        }
        .sidebar a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .sidebar a:hover {
            background-color: #495057;
        }
        .content {
            margin-left: 270px;
            padding: 30px;
            flex: 1;
            background-color: #f8f9fa;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .btn-custom {
            width: 200px;
            margin: 10px 0;
        }
        h1 {
            font-family: 'Arial', sans-serif;
            color: #343a40;
        }
        .table-container {
            margin-top: 30px;
        }
        .bg-danger {
            background-color: red !important;
            color: white;
        }
        .bg-success {
            background-color: green !important;
            color: white;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu Client</h3>
    <a href="/client/espaces-travail" class="btn btn-light btn-custom">Liste des espaces de travail</a>
    <a href="/mesreservation" class="btn btn-light btn-custom">Mes réservations</a>
</div>

<!-- Content -->
<div class="content">
    <h1 class="text-center">Tableau de bord Client</h1>
    <p class="text-center">Bienvenue dans votre espace client. Sélectionnez une option dans le menu à gauche pour gérer vos espaces de travail et réservations.</p>

    <!-- Formulaire de sélection de date -->
    <div class="text-center mt-4">
        <h3>Select une date pour voir la dispo des espaces</h3>
        <form id="dateForm" class="form-inline justify-content-center">
            <div class="form-group">
                <label for="date" class="mr-2">Date :</label>
                <input type="date" id="date" name="date" class="form-control" required>
            </div>
            <button type="button" id="submitDate" class="btn btn-primary ml-3">Voir les Espaces</button>
        </form>
    </div>

    <!-- Tableau des espaces de travail -->
    <div class="table-container" id="workspaceTable">
        <table class="table table-bordered mt-4">
            <thead>
            <tr>
                <th>Espaces de Travail</th>
                <th>8h</th>
                <th>9h</th>
                <th>10h</th>
                <th>11h</th>
                <th>12h</th>
                <th>13h</th>
                <th>14h</th>
                <th>15h</th>
                <th>16h</th>
                <th>17h</th>
                <th>18h</th>
                <th>!</th>
            </tr>
            </thead>
            <tbody>
            <%
                // Supposons que vous ayez une liste d'espaces dans le modèle
                List<Workspace> espacesDeTravail = (List<Workspace>) request.getAttribute("liste_workspace");
                for (int i = 0; i < espacesDeTravail.size(); i++) {
                    Workspace espace = espacesDeTravail.get(i);
            %>
            <tr>
                <td><%= espace.getName() %></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>
                    <form action="/reserver" method="POST">
                        <input type="hidden" name="workspaceId" value="<%= espace.getId() %>" />
                        <input type="hidden" name="workspaceName" value="<%= espace.getName() %>" />  <!-- Ajoute ici une autre valeur -->
                        <button type="submit">Réserver</button>
                    </form>
                </td>

            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
