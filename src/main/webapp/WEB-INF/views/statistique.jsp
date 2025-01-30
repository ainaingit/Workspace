<%@ page import="com.example.workspace.vue.ChiffreAffaireParJour" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.workspace.vue.ChiffreAffaireTotal" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statistiques - Chiffre d'Affaire</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            background-color: #f4f6f9;
            display: flex;
            min-height: 100vh;
            font-family: 'Arial', sans-serif;
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
        .chart-container {
            position: relative;
            height: 40vh;
            width: 100%;
            margin-top: 30px;
        }
        .stat-box {
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .stat-box h4 {
            color: #495057;
            font-size: 1.2rem;
            margin-bottom: 10px;
        }
        .stat-box p {
            font-size: 1.5rem;
            color: #28a745;
            font-weight: bold;
        }
        .form-control {
            height: 45px;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h3 class="text-center text-white">Menu Admin</h3>
    <a href="/importerdonnees" class="btn btn-light">Importer les données</a>
    <a href="/admin/valider-paiement" class="btn btn-light">Valider les paiements</a>
    <a href="/admin/statistiques" class="btn btn-light">Statistiques</a>
</div>

<!-- Content -->
<div class="content">
    <h1 class="text-center mb-4">Statistiques - Chiffre d'Affaire</h1>

    <%
        // Récupérer l'objet ChiffreAffaireTotal depuis la requête
        ChiffreAffaireTotal chiffreAffaireTotal = (ChiffreAffaireTotal) request.getAttribute("chiffredaffaire");
    %>

    <!-- Statistiques sous forme de 3 cartes -->
    <div class="row mb-5">
        <div class="col-md-4">
            <div class="stat-box">
                <h4>Chiffre d'Affaire Total</h4>
                <p><%= chiffreAffaireTotal.getChiffreAffaireTotal() %> €</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-box">
                <h4>Montant payé</h4>
                <p><%= chiffreAffaireTotal.getMontantPaye() %> €</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-box">
                <h4>Chiffre d'Affaire Moyen</h4>
                <p><%= chiffreAffaireTotal.getMontantAPayer() %> €</p>
            </div>
        </div>
    </div>

    <!-- Formulaire de filtre par date -->
    <form action="" method="get" class="mb-4">
        <div class="row">
            <div class="col-md-5">
                <input type="date" class="form-control" name="startDate" placeholder="Date de début" required>
            </div>
            <div class="col-md-5">
                <input type="date" class="form-control" name="endDate" placeholder="Date de fin" required>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Filtrer</button>
            </div>
        </div>
    </form>

    <!-- Tableau des chiffres d'affaire -->
    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>Date</th>
                <th>Chiffre d'Affaire</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<ChiffreAffaireParJour> listeChiffreAffaire = (List<ChiffreAffaireParJour>) request.getAttribute("listechiffreaffaire");
                for (ChiffreAffaireParJour chiffreAffaire : listeChiffreAffaire) {
            %>
            <tr>
                <td><%= chiffreAffaire.getDatePaiement() %></td>
                <td><%= chiffreAffaire.getChiffreAffaire() %> €</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>

    <!-- Histogramme (Graphique) -->
    <div class="chart-container">
        <canvas id="chiffreAffaireChart"></canvas>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Récupérer les données de chiffre d'affaire et de dates depuis la page JSP
    const labels = <%= listeChiffreAffaire.stream().map(chiffreAffaire -> "\"" + chiffreAffaire.getDatePaiement() + "\"").collect(java.util.stream.Collectors.toList()) %>;
    const data = <%= listeChiffreAffaire.stream().map(chiffreAffaire -> chiffreAffaire.getChiffreAffaire()).collect(java.util.stream.Collectors.toList()) %>;

    // Créer l'histogramme avec Chart.js
    const ctx = document.getElementById('chiffreAffaireChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Chiffre d\'Affaire par Jour',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',  // Couleur des barres
                borderColor: 'rgba(54, 162, 235, 1)',    // Bordure des barres
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Date'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Chiffre d\'Affaire'
                    },
                    beginAtZero: true
                }
            }
        }
    });
</script>

</body>
</html>
