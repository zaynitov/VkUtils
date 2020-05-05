import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoColumnsFoldersToFiles {
    private Path firstPath;
    private Path secondPath;
    private List<PathToRelativeFileName> firstPathFiles;
    private List<PathToRelativeFileName> secondPathFiles;
}
