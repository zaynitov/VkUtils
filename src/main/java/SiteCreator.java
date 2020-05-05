import freemarker.template.TemplateException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SiteCreator {

    static Map<Path, Path> allDirectoriesToLevelSite = new HashMap<>();
    static Set<Path> allLevelSites = new TreeSet<>();
    static Map<Path, List<Path>> folderToFiles = new HashMap();
    static int count = 0;

    public static void createSite() throws IOException, URISyntaxException, TemplateException {
        System.setProperty("file.encoding", "UTF-8");
        ClassLoader classLoader = SiteCreator.class.getClassLoader();
        Path rootPath = Paths.get(classLoader.getResource("ready").toURI());
        List<Path> collect = Files.walk(rootPath)
                .sorted()
                .collect(Collectors.toList());

        collect.forEach(path ->
        {
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
            boolean isFile = Files.isRegularFile(path);
            if (!isFile) {
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
                }
            }




          /*  Configuration cfg = new Configuration();
            Map<String, Object> root = new HashMap<>();
            root.put("twofolders", twoColumnsFolders);
            Template temp = cfg.getTemplate("test.ftl");
            Path path = Paths.get(rootPath.toString(), "/" + count++ + ".html");
            System.out.println("path = " + path);
            Path file = Files.createFile(path);
            OutputStream outputStream = new FileOutputStream(file.toString());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            temp.process(root, outputStreamWriter);*/
        }
    }
}