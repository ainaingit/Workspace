<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        body {
            display: flex;
            min-height: 100vh;
            background-color: #f4f4f9;
        }
        .sidebar {
            width: 250px;
            background-color: #343a40;
            color: white;
            padding-top: 30px;
            position: fixed;
            height: 100%;
        }
        .sidebar a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px 15px;
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
        }
        .btn-primary {
            width: 200px;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu Admin</h3>
    <a href="/importerdonnees" class="btn btn-light">Importer les données</a>
    <a href="/admin/paiement" class="btn btn-light">Valider les paiements</a>
    <a href="/admin/statistiques" class="btn btn-light">Statistiques</a>
</div>

<!-- Content -->
<div class="content">
    <h1 class="text-center">Tableau de bord Administrateur</h1>
    <p class="text-center">Bienvenue sur votre tableau de bord. Sélectionnez une option dans le menu à gauche pour gérer l'administration.</p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
