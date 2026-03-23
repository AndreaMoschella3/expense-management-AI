package com.management;

import java.util.Scanner;

//TIP Per <b>eseguire</b> il codice, premere <shortcut actionId="Run"/> o
// clicca sullicona {0} nel margine.
public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        DatabaseManager databaseManager = new DatabaseManager();

        System.out.println("Benvenuto nel database AI n.1 per tenere traccia delle tue spese.");

        boolean again = true;
        while (again)
        {
            System.out.println("Inserisci la spesa effettuata (quanto hai speso e opzionalmente una descrizione di essa):");
            String expense = input.nextLine();

            try {
                databaseManager.executeQuery(databaseManager.getQuery(expense));
            } catch (Exception e) {
                System.err.println("Errore: " + e.getMessage());
            }

            System.out.println("Vuoi aggiungere un altra spesa? (s/n)");
            String answer = input.nextLine();

            again = answer.equalsIgnoreCase("s");
        }

    }

}
