<!DOCTYPE html>
<html>
<head>
    <title>Login Client</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2>Connexion Client</h2>
    <form action="/login_client" method="post">
        <div class="form-group row justify-content-center">
            <label for="numetu" class="col-md-3 col-form-label">Numéro étudiant</label>
            <div class="col-md-6">
                <input type="text" id="numetu" name="numero" class="form-control" required>
            </div>
        </div>
        <div class="form-group row justify-content-center">
            <div class="col-md-6 text-center">
                <button type="submit" class="btn btn-primary">Se connecter</button>
            </div>
        </div>
    </form>
    <br>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>
</div>
</body>
</html>
