<!DOCTYPE html>
<html>
<head>
    <title>Login Administrateur</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2>Connexion Administrateur</h2>
    <form action="/login_admin" method="post">
        <div class="form-group row justify-content-center">
            <label for="username" class="col-md-3 col-form-label">Nom d'utilisateur</label>
            <div class="col-md-6">
                <input type="text" id="username" name="username" class="form-control" required>
            </div>
        </div>
        <div class="form-group row justify-content-center">
            <label for="password" class="col-md-3 col-form-label">Mot de passe</label>
            <div class="col-md-6">
                <input type="password" id="password" name="password" class="form-control" required>
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
