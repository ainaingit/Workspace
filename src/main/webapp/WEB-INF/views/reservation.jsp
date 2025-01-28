<%@ page import="com.example.workspace.entity.Option" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="form" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réserver un espace de travail</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2>Réserver l'espace de travail</h2>
    <p>Vous réservez l'espace <strong>${workspaceName}</strong> (ID : ${workspaceId}).</p>

    <form action="/confirmReservation" method="POST">
        <!-- Champs cachés pour workspaceId et workspaceName -->
        <input type="hidden" name="workspaceId" value="${workspaceId}" />
        <input type="hidden" name="workspaceName" value="${workspaceName}" />

        <!-- Champ pour la date -->
        <div class="form-group">
            <label for="reservationDate">Date de réservation</label>
            <input type="date" id="reservationDate" name="reservationDate" class="form-control" required>
        </div>

        <!-- Champ pour l'heure de début -->
        <div class="form-group">
            <label for="startHour">Heure de début</label>
            <input type="number" id="startHour" name="startHour" class="form-control" required>
        </div>

        <!-- Champ pour la durée -->
        <div class="form-group">
            <label for="duration">Durée (en heures)</label>
            <input type="number" id="duration" name="duration" class="form-control" min="1" required>
        </div>

        <!-- Liste des options sous forme de cases à cocher -->
        <div class="form-group">
            <%-- Utilisation d'une boucle for classique pour afficher les options --%>
            <%
                List<Option> options = (List<Option>) request.getAttribute("options");
                for (Option option : options) {
            %>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" name="option" id="option<%= option.getId() %>" value="<%= option.getId() %>">
                <label class="form-check-label" for="option<%= option.getId() %>">
                    <%= option.getName() %>
                </label>
            </div>
            <%
                }
            %>
        </div>

        <!-- Bouton de soumission -->
        <button type="submit" class="btn btn-primary">Réserver</button>
    </form>
</div>

<!-- JS Bootstrap (optionnel) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
