import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    public static void findInContent() throws IOException, URISyntaxException {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        Path configFilePath = Paths.get(classLoader.getResource("texts\\1.txt").toURI());
        List<Path> collect = Files.walk(configFilePath)
                .sorted()
                .collect(Collectors.toList());
        collect.forEach(System.out::println);
        collect.forEach(path -> {
            try {
                replaceByPath(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void replaceByPath(Path path) throws IOException {
        int countId = 2;
        int iL = 1;
        boolean add = false;
        System.out.println("path = " + path);
        Stream<String> lines = Files.lines(path);
        List<String> collect = lines.collect(Collectors.toList());
        List<String> replaced = new ArrayList<>();
        for (String line : collect) {
            if (line.contains("albert")) {
                replaced.add(line);
                String albert = line.replaceAll("albert", "").replaceAll(" ", "").replaceAll(",", "");
                System.out.println("albert = " + albert);
                File file = new File("D:\\Downloads\\" + albert);
                boolean file1 = file.isFile();
                System.out.println("file1 = " + file1);
                String path1 = file.getPath();
                System.out.println("path1 = " + path1);
                Path from = FileSystems.getDefault().getPath(path1);
                Path to = Paths.get("D:\\Downloads\\miracle\\albert" + iL++ + ".jpg");
                Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
//                boolean b = file.renameTo(new File("D:\\Downloads\\miracle\\albert" + iL++));
               // System.out.println("b = " + b);
            }
        }
        Files.write(path, replaced);
        lines.close();
        System.out.println("Find and Replace done!!!");
    }
}
