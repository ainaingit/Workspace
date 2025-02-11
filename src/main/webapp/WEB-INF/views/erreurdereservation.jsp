<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Erreur de réservation</title>
</head>
<body>
<h1>Réservation</h1>

<c:if test="${not empty Erreur}">
    <p style="color: red;">${Erreur}</p>  <!-- Affiche l'erreur dynamique -->
</c:if>

<c:if test="${not empty reussite}">
    <p style="color: green;">${reussite}</p>  <!-- Affiche le message de réussite -->
</c:if>

<!-- Retour à la page de réservation -->
<a href="/reserver">Retour à la réservation</a>
</body>
</html>
