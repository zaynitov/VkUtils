import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SiteCreator {

    static Map<Path, Path> allDirectoriesToLevelSite = new HashMap<>();
    static Set<Path> allLevelSites = new TreeSet<>();
    static Map<Path, List<Path>> folderToFiles = new HashMap();

    public static void createSite() throws IOException, URISyntaxException, TemplateException {


        System.setProperty("file.encoding", "UTF-8");
        ClassLoader classLoader = SiteCreator.class.getClassLoader();
        Path rootPath = Paths.get(classLoader.getResource("sites").toURI());
        List<Path> collect = Files.walk(rootPath)
                .sorted()
                .collect(Collectors.toList());
        //   collect.forEach(System.out::println);


        collect.forEach(path ->
        {
            boolean isFile = Files.isRegularFile(path);
            if (isFile) {
                folderToFiles.compute(path.getParent(), (key, value) -> {
                    if (value == null) {
                        ArrayList<Path> paths = new ArrayList<>();
                        paths.add(path);
                        return paths;
                    } else {
                        value.add(path);
                        return value;
                    }
                });
            } else {
                if (!path.equals(rootPath)) {
                    Path parent = path.getParent();
                    allDirectoriesToLevelSite.put(path, parent);
                    allLevelSites.add(parent);
                }
            }

        });


        for (Path levelSite : allLevelSites) {


            List<Path> allFoldersInSiteLevel = allDirectoriesToLevelSite.entrySet().stream().filter(entrySet -> levelSite.equals(entrySet.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

            Map<Path, List<PathToRelativeFileName>> folderInSiteToFiles = new HashMap<>();
            for (Path folderInSite : allFoldersInSiteLevel) {
                List<Path> filesPaths = folderToFiles.get(folderInSite);
                List<PathToRelativeFileName> pathToRelativeFileNames = null;
                if (filesPaths != null) {
                    pathToRelativeFileNames = filesPaths.stream().map(path -> new PathToRelativeFileName(path, path.toString().replace("C:\\Users\\Albert\\IdeaProjects\\vk\\build\\resources\\main\\sites\\", ""))).collect(Collectors.toList());
                }
                folderInSiteToFiles.put(folderInSite, pathToRelativeFileNames);
            }


            List<TwoColumnsFoldersToFiles> twoColumnsFolders = new ArrayList<>();

            List<Map.Entry<Path, List<PathToRelativeFileName>>> folderInSiteToFilesList = new ArrayList<>(folderInSiteToFiles.entrySet());


            for (int i = 0; i < folderInSiteToFilesList.size(); i++) {
                if (i + 1 < folderInSiteToFilesList.size()) {
                    twoColumnsFolders.add(
                            new TwoColumnsFoldersToFiles(folderInSiteToFilesList.get(i).getKey(),
                                    folderInSiteToFilesList.get(i + 1).getKey(),
                                    folderInSiteToFilesList.get(i).getValue(),
                                    folderInSiteToFilesList.get(i + 1).getValue()));
                    i++;
                } else {
                    twoColumnsFolders.add(
                            new TwoColumnsFoldersToFiles(folderInSiteToFilesList.get(i).getKey(),
                                    null,
                                    folderInSiteToFilesList.get(i).getValue(),
                                    null));
                    break;
                }

            }


            Configuration cfg = new Configuration();
// модель данных
            Map<String, Object> root = new HashMap<>();
            root.put("twofolders", twoColumnsFolders);
// шаблон
            Template temp = cfg.getTemplate("test.ftl");

// обработка шаблона и модели данных
            OutputStream outputStream = new FileOutputStream("C:\\Users\\Albert\\IdeaProjects\\vk\\src\\main\\resources\\ready\\1.html");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
// вывод в консоль
            temp.process(root, outputStreamWriter);

            break;

// Конфигурация


        }











     /*       if (isFile) {
                Path parentPath = path.getParent();
                Set<Path> allFolders= new TreeSet<>();
                while (!path.getParent().equals(rootPath)){
                    path = path.getParent();
                    allFolders.add(path)
                }
                new FourLevelSite()
            }
*/



    /*

        collect.forEach(path -> {
            try {
                replaceByPath(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
    }

    public static void replaceByPath(Path path) throws IOException {
        int countId = 2;
        int iL = 1;
        boolean add = false;
        boolean isDirectory = Files.isDirectory(path);


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
