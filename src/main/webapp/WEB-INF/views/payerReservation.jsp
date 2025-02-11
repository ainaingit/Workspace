<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Payer Réservation</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">Paiement de la réservation</h2>
    <div class="card shadow-sm p-4">
        <!-- Détails de la réservation -->
        <!--  <h4 class="mb-3">Réservation ID : ${reservationDetails.reservationId}</h4> -->
        <p><strong>Date de réservation :</strong> ${reservationDetails.reservationDate}</p>
        <p><strong>Heure de début :</strong> ${reservationDetails.startHour}</p>
        <p><strong>Heure de fin :</strong> ${reservationDetails.endHour}</p>
        <p><strong>Durée :</strong> ${reservationDetails.duration} heures</p>
        <p><strong>Espace réservé :</strong> ${reservationDetails.workspaceName}</p>
        <p><strong>Options :</strong> ${reservationDetails.optionsNames}</p>
        <p><strong>Montant total :</strong> ${reservationDetails.totalAmount} €</p>
        <p><strong>Statut :</strong> ${reservationDetails.status}</p>

        <hr>

        <!-- Formulaire de paiement -->
        <form action="/processPayment" method="post">
            <!-- Champ caché pour passer l'ID de réservation -->
            <input type="hidden" name="reservationId" value="${reservationDetails.reservationId}">

            <!-- Input pour saisir le montant -->
            <div class="mb-3">
                <label for="mode" class="form-label">Reference ex : mvola , orange , temla , yas (€)</label>
                <input type="text" step="0.01" class="form-control" id="mode" name="mode" required>
            </div>

            <!-- Bouton de validation -->
            <button type="submit" class="btn btn-primary">Valider le paiement</button>
        </form>
    </div>
</div>
</body>
</html>
