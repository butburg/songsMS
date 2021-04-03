# Pull Request 

## Checklist
- [ ] Ist **htwb-steven** als Reviewer eingetragen?
- [ ] Ist der Workflow `projects.yml` erfolgreich?

## Für Beleg 2, 3, 4

### Datenbank

- Username:
- Passwort:
- JDBC URL: 

### Anwendung

- Link zur Anwendung:
- Erledigte Zusatzaufgaben:

### Beispielanfragen

Jede **implementierte Anfrage** soll mit folgenden Template dokumentiert werden:

#### Template Für GET, DELETE Methoden
```
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/hallo
```

#### Template für POST, PUT Methoden
```
curl -X POST \
     -H "Content-Type: application/json" \
     -v "http://localhost:8080/hallo" \
     -d '{"title":"Wrecking Ball"}'
```
