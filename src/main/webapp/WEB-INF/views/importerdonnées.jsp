<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Importation de fichiers</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">Importation de fichiers</h2>
    <form action="/import" method="POST" enctype="multipart/form-data">
        <div class="form-group">
            <label for="workspaceFile">Espace de travail (Fichier)</label>
            <input type="file" class="form-control-file" id="workspaceFile" name="workspaceFile" required>
        </div>

        <div class="form-group">
            <label for="reservationClientFile">Réservation et client (Fichier)</label>
            <input type="file" class="form-control-file" id="reservationClientFile" name="reservationClientFile" required>
        </div>

        <div class="form-group">
            <label for="optionalItemsFile">Liste des options payantes (imprimante, appareil photo, vidéoprojecteur, laptop)</label>
            <input type="file" class="form-control-file" id="optionalItemsFile" name="optionalItemsFile" required>
        </div>

        <div class="form-group text-center">
            <button type="submit" class="btn btn-primary">Importer les fichiers</button>
        </div>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
