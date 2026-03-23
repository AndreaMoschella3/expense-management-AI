package com.management;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager
{
    private final OllamaClient ollamaClient;
    private final Dotenv dotenv;

    public DatabaseManager()
    {
        ollamaClient = new OllamaClient();
        dotenv = Dotenv.load();
    }

    public String getQuery(String userInput) throws Exception
    {
        String prompt = """
        Sei un assistente che converte frasi in italiano in query SQL INSERT.
        
        Input utente: "%s"
        
        Tabella target: expenses
        Struttura:
        - id INT AUTO_INCREMENT PRIMARY KEY (non inserire)
        - money_spent DECIMAL(7,2) NOT NULL → estrai l'importo numerico dalla frase
        - expense_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP (non inserire, ci pensa il DB)
        - description VARCHAR(255) → se l'utente menziona un luogo, categoria o motivo della spesa usalo come descrizione, altrimenti usa NULL
        
        Regole:
        - Rispondi SOLO con la query SQL INSERT, nessun testo aggiuntivo
        - Non includere id ed expense_date nella query
        - La descrizione deve essere in italiano
        - NON usare backtick o formattazione markdown
        - NON aggiungere commenti o spiegazioni
        - Scrivi solo ed esclusivamente la riga SQL
        - La spesa può riferirsi a qualsiasi persona (io, mia madre, mio padre ecc.) — estrai solo importo e descrizione
        - Esempi di descrizione: "supermercato", "benzina", "riparazione macchina", "bolletta luce"
        
        Esempio output atteso:
        INSERT INTO expenses (money_spent, description) VALUES (45.00, 'supermercato');
        """.formatted(userInput);

        String risposta = ollamaClient.askQuestion(prompt);
        return extractQuery(risposta);
    }

    private String extractQuery(String risposta)
    {
        // Rimuove i blocchi markdown ```sql ... ``` o ``` ... ```
        String cleaned = risposta
                .replaceAll("(?i)```sql", "")
                .replaceAll("```", "")
                .trim();

        for (String line : cleaned.split("\n"))
        {
            String trimmed = line.trim();
            if (trimmed.toUpperCase().startsWith("INSERT") ||
                    trimmed.toUpperCase().startsWith("SELECT") ||
                    trimmed.toUpperCase().startsWith("UPDATE") ||
                    trimmed.toUpperCase().startsWith("DELETE"))
            {
                return trimmed;
            }
        }
        return cleaned;
    }

    public void executeQuery(String query) throws Exception
    {
        String url  = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASS");

        try (Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement())
        {
            st.executeUpdate(query);
            System.out.println("Spesa salvata!");
        }

    }

}
