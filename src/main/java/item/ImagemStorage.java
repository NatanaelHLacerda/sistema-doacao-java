package item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

public final class ImagemStorage {

    private ImagemStorage() {}

    private static final Path DIR = Paths.get(
            System.getProperty("user.home"), ".projeto-doacao", "imagens");

    public static Path copiarParaArmazenamento(File origem) throws IOException {
        if (origem == null || !origem.exists()) {
            throw new IOException("Arquivo de imagem inválido.");
        }
        Files.createDirectories(DIR);

        String nome = origem.getName().toLowerCase(Locale.ROOT);
        String ext = "";
        int pos = nome.lastIndexOf('.');
        if (pos >= 0) ext = nome.substring(pos);

        Path destino = DIR.resolve(UUID.randomUUID() + ext);
        Files.copy(origem.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return destino;
    }

    public static boolean existe(String caminho) {
        if (caminho == null || caminho.isBlank()) return false;
        return Files.exists(Paths.get(caminho));
    }
}
