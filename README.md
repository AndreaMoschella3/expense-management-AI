# 💸 Expense Management AI

Un'applicazione Java a riga di comando che ti permette di registrare le tue spese in **linguaggio naturale**. Basta scrivere frasi come _"ho speso 45 euro al supermercato"_ e il programma si occupa del resto: un modello AI locale (Ollama) interpreta la frase, genera la query SQL e salva i dati in un database MySQL.

---

## 🚀 Come funziona

```
Utente scrive          Ollama AI locale        MySQL
"ho speso 45€"   →    genera la query    →   salva i dati
 al supermercato       INSERT INTO...         nel database
```

1. L'utente inserisce una frase in italiano
2. La frase viene inviata a **Ollama** (AI locale su `localhost:11434`)
3. Ollama genera una query SQL `INSERT`
4. La query viene eseguita su **MySQL**
5. La spesa è salvata!

---

## 🛠️ Stack tecnico

| Componente | Tecnologia |
|---|---|
| Linguaggio | Java 17 |
| Build tool | Maven |
| AI locale | Ollama + `llama3.2:3b` |
| Database | MySQL |
| HTTP Client | `java.net.http.HttpClient` (nativo Java 11+) |
| JSON | Jackson Databind |
| Config | dotenv-java |

---

## 📋 Prerequisiti

- **Java 17+** installato
- **Maven** installato
- **MySQL** in esecuzione in locale
- **Ollama** installato e avviato (`ollama serve`)
- Modello Ollama scaricato:
  ```bash
  ollama pull llama3.2:3b
  ```

---

## ⚙️ Setup

### 1. Clona il repository

```bash
git clone https://github.com/tuo-username/expense-management.git
cd expense-management
```

### 2. Crea il database MySQL

```sql
CREATE DATABASE expenseDB;

USE expenseDB;

CREATE TABLE expenses (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    money_spent  DECIMAL(7, 2)  NOT NULL,
    expense_date TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description  VARCHAR(255)
);
```

### 3. Crea il file `.env`

Crea un file `.env` nella root del progetto:

```env
DB_URL=jdbc:mysql://localhost:3306/expenseDB
DB_USER=il_tuo_utente
DB_PASS=la_tua_password
```

> ⚠️ Il file `.env` è nel `.gitignore` e non viene caricato su GitHub.

### 4. Build del progetto

```bash
mvn clean package
```

### 5. Avvia l'applicazione

```bash
java -jar target/spese-ai.jar
```

---

## 💬 Esempi di utilizzo

```
Benvenuto nel database AI n.1 per tenere traccia delle tue spese.
Inserisci la spesa effettuata:
> ho speso 45 euro al supermercato
Spesa salvata!

Vuoi aggiungere un'altra spesa? (s/n)
> s

Inserisci la spesa effettuata:
> 30 euro di benzina
Spesa salvata!

Vuoi aggiungere un'altra spesa? (s/n)
> n
```

---

## 📁 Struttura del progetto

```
expense-management/
├── src/
│   └── main/
│       └── java/
│           └── com/management/
│               ├── Main.java             # Entry point e loop CLI
│               ├── OllamaClient.java     # Comunicazione con Ollama API
│               └── DatabaseManager.java  # Prompt, query e connessione MySQL
├── .env                                  # Credenziali locali (non in git)
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🔒 .gitignore consigliato

```gitignore
target/
.env
*.class
*.iml
.idea/
```

---

## 🔮 Possibili sviluppi futuri

- [ ] Lettura spese: _"quanto ho speso questo mese?"_
- [ ] Categorie automatiche
- [ ] Export CSV / report mensile
- [ ] Interfaccia web con Spring Boot

---

## 👤 Autore

**Andrea Moschella** — [GitHub](https://github.com/tuo-username)
