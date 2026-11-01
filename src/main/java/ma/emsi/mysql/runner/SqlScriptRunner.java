package ma.emsi.mysql.runner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlScriptRunner {

    public static List<String> parseStatements(String filePath) throws Exception {
        String content;
        if (Files.exists(Paths.get(filePath))) {
            content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } else {
            try (InputStream in = SqlScriptRunner.class.getClassLoader().getResourceAsStream(filePath)) {
                if (in == null) {
                    throw new IllegalArgumentException("Fichier SQL introuvable : " + filePath);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                content = sb.toString();
            }
        }

        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String rawLine : content.split("\\r?\\n")) {
            String trimmed = rawLine.trim();
            if (trimmed.startsWith("--") || trimmed.startsWith("//") || trimmed.isEmpty()) {
                continue;
            }
            current.append(rawLine).append(" ");
            if (trimmed.endsWith(";")) {
                String stmt = current.toString().trim();
                if (stmt.endsWith(";")) {
                    stmt = stmt.substring(0, stmt.length() - 1);
                }
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                current = new StringBuilder();
            }
        }
        return statements;
    }

    public static void executeScript(Connection conn, String filePath, boolean printResults) throws Exception {
        List<String> statements = parseStatements(filePath);
        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                boolean isResultSet = stmt.execute(sql);
                if (printResults) {
                    System.out.println("mysql> " + sql + ";");
                    if (isResultSet) {
                        try (ResultSet rs = stmt.getResultSet()) {
                            printResultSet(rs);
                        }
                    } else {
                        int rows = stmt.getUpdateCount();
                        System.out.println("Query OK, " + rows + " row(s) affected.\n");
                    }
                }
            }
        }
    }

    public static void printResultSet(ResultSet rs) throws Exception {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        StringBuilder header = new StringBuilder("+");
        StringBuilder title = new StringBuilder("|");

        for (int i = 1; i <= cols; i++) {
            header.append("----------------------+");
            title.append(String.format(" %-20s |", md.getColumnLabel(i)));
        }

        System.out.println(header);
        System.out.println(title);
        System.out.println(header);

        int count = 0;
        while (rs.next()) {
            count++;
            StringBuilder row = new StringBuilder("|");
            for (int i = 1; i <= cols; i++) {
                String val = rs.getString(i);
                if (val == null) val = "NULL";
                if (val.length() > 20) val = val.substring(0, 17) + "...";
                row.append(String.format(" %-20s |", val));
            }
            System.out.println(row);
        }
        System.out.println(header);
        System.out.println(count + " row(s) in set.\n");
    }
}
