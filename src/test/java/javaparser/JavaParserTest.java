package javaparser;

import com.github.javaparser.utils.SourceRoot;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserTest {
    @Test
    void should_parse_file_from_regular_filesystem() {
        var srcPath = Paths.get("src/test/resources");
        var sourceRoot = new SourceRoot(srcPath);
        var compilationUnit = sourceRoot.parse("", "HelloWorld.java");

        assertThat(compilationUnit.toString()).isEqualTo("""
            public class HelloWorld {

                public static void main(String[] args) {
                    System.out.println("Hello world!");
                }
            }
            """);
    }

    @Test
    void should_parse_file_from_jimfs_filesystem() throws IOException {
        var fileSystem = Jimfs.newFileSystem(Configuration.unix());
        var srcPath = fileSystem.getPath("/src");
        Files.createDirectory(srcPath);

        var from = new File("src/test/resources/HelloWorld.java").toPath();
        var to = srcPath.resolve("HelloWorld.java");
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

        var sourceRoot = new SourceRoot(srcPath);
        var compilationUnit = sourceRoot.parse("", "HelloWorld.java");

        assertThat(compilationUnit.toString()).isEqualTo("""
            public class HelloWorld {

                public static void main(String[] args) {
                    System.out.println("Hello world!");
                }
            }
            """);
    }
}
