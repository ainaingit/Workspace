<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page d'accueil</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        .container {
            text-align: center;
            margin-top: 100px;
        }
        .btn {
            width: 200px;
            margin: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Bienvenue sur notre site</h1>
    <p>Veuillez choisir un type de connexion :</p>

    <div class="d-flex justify-content-center">
        <!-- Bouton Admin -->
        <a href="/admin/login" class="btn btn-primary">Admin</a>
        <!-- Bouton Client -->
        <a href="/client/login" class="btn btn-secondary">Client</a>
    </div>

    <a href="/admin/reset" class="btn btn-secondary">Reset Table</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
