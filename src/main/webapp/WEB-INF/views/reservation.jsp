<%@ page import="com.example.workspace.entity.Option" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.workspace.entity.Client" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="form" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réserver un espace de travail</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <script>
        function validateForm(event) {
            event.preventDefault();

            var startHour = parseInt(document.getElementById("startHour").value);
            var duration = parseInt(document.getElementById("duration").value);
            var clientId = document.getElementById("reserveurId").value;
            var reservationDate = document.getElementById("reservationDate").value;

            if (clientId === "") {
                alert("Veuillez sélectionner un client commandeur.");
                document.getElementById("reserveurId").focus();
                return false;
            }

            if (!reservationDate) {
                alert("Veuillez sélectionner une date de réservation.");
                document.getElementById("reservationDate").focus();
                return false;
            }

            if (startHour < 8 || startHour >= 18) {
                alert("L'heure de début doit être entre 8h et 18h.");
                document.getElementById("startHour").focus();
                return false;
            }

            if (duration < 1 ) {
                alert("La durée doit être comprise entre 1 et 4 heures.");
                document.getElementById("duration").focus();
                return false;
            }



            document.getElementById("reservationForm").submit();
        }
    </script>
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">Réserver un espace de travail</h2>
    <p class="text-center">Vous réservez l'espace <strong>${workspaceName}</strong></p>

    <div class="card">
        <div class="card-body">
            <form id="reservationForm" action="/confirmReservation" method="POST" onsubmit="return validateForm(event)">
                <input type="hidden" name="workspaceId" value="${workspaceId}" />
                <input type="hidden" name="workspaceName" value="${workspaceName}" />

                <div class="form-group">
                    <label for="reserveurId">Client Commandeur</label>
                    <select id="reserveurId" name="reserveurId" class="form-control" required>
                        <%
                            String numero = (String) request.getAttribute("numero");
                            Long id  = (Long) request.getAttribute("idalefa");

                        %>
                        <% if (numero != null) { %>
                        <option value="<%= id %>" selected><%= numero %></option>
                        <% } else { %>
                        <option value="" selected>-- Sélectionnez un client --</option>
                        <% } %>

                        <%
                            List<Client> clients = (List<Client>) request.getAttribute("clients");
                            for (Client client : clients) {
                                if (!client.getId().equals(id)) {
                        %>
                        <option value="<%= client.getId() %>"><%= client.getNumber() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Date de réservation</label>
                    <p id="reservationDateDisplay" class="form-control">
                        <%= request.getAttribute("selectedDate") != null ? request.getAttribute("selectedDate") : new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>
                    </p>
                    <!-- Champ caché pour envoyer la date dans le formulaire -->
                    <input type="hidden" id="reservationDate" name="reservationDate" value="<%= request.getAttribute("selectedDate") != null ? request.getAttribute("selectedDate") : new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                </div>


                <div class="form-group">
                    <label for="startHour">Heure de début (entre 8h et 18h)</label>
                    <input type="number" id="startHour" name="startHour" class="form-control" min="8" max="18" required placeholder="De 8h à 18h">
                </div>

                <div class="form-group">
                    <label for="duration">Durée (en heures, entre 1 et 4)</label>
                    <input type="number" id="duration" name="duration" class="form-control" min="1" max="4" required placeholder="1 à 4 heures">
                </div>

                <div class="form-group">
                    <label>Options supplémentaires :</label>
                    <div class="row">
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

                <div class="text-center">
                    <button type="submit" class="btn btn-primary btn-lg">Réserver</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
