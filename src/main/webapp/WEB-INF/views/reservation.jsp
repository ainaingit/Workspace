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
    <script>
        // Fonction pour valider les champs
        function validateForm() {
            // Récupérer les valeurs des champs
            var startHour = parseInt(document.getElementById("startHour").value);
            var duration = parseInt(document.getElementById("duration").value);

            // Vérification de l'heure de début
            if (startHour < 8 || startHour > 18) {
                alert("L'heure de début doit être entre 8h et 18h.");
                document.getElementById("startHour").focus();
                return false; // Empêcher l'envoi du formulaire
            }

            // Vérification de la durée
            if (duration < 1 || duration > 4) {
                alert("La durée doit être comprise entre 1 et 4 heures.");
                document.getElementById("duration").focus();
                return false; // Empêcher l'envoi du formulaire
            }

            // Si tout est validé, envoyer le formulaire
            return true;
        }
    </script>
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">Réserver un espace de travail</h2>
    <p class="text-center">Vous réservez l'espace <strong>${workspaceName}</strong> (ID : ${workspaceId}).</p>

    <div class="card">
        <div class="card-body">
            <form action="/confirmReservation" method="POST" onsubmit="return validateForm()">
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
                    <label for="startHour">Heure de début (entre 8h et 18h)</label>
                    <input type="number" id="startHour" name="startHour" class="form-control" required placeholder="De 8h à 18h" min="8" max="18">
                </div>

                <!-- Champ pour la durée -->
                <div class="form-group">
                    <label for="duration">Durée (en heures, entre 1 et 4)</label>
                    <input type="number" id="duration" name="duration" class="form-control" min="1" max="4" required placeholder="1 à 4 heures">
                </div>

                <!-- Liste des options sous forme de cases à cocher -->
                <div class="form-group">
                    <label>Options supplémentaires :</label>
                    <div class="row">
                        <%-- Utilisation d'une boucle pour afficher les options --%>
                        <%
                            List<Option> options = (List<Option>) request.getAttribute("options");
                            for (Option option : options) {
                        %>
                        <div class="col-md-4">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="option" id="option<%= option.getId() %>" value="<%= option.getId() %>">
                                <label class="form-check-label" for="option<%= option.getId() %>">
                                    <%= option.getName() %>
                                </label>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>

                <!-- Bouton de soumission -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary btn-lg">Réserver</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- JS Bootstrap (optionnel) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
