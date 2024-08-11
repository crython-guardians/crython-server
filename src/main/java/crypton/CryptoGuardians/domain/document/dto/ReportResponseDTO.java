package crypton.CryptoGuardians.domain.document.dto;

import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.entity.DocumentView;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record ReportResponseDTO(
        int fileReadCount,
        int fileTheftCount,
        boolean updateAuthKey,
        List<ViewerDTO> viewers
) {
    public static ReportResponseDTO from(Document document, List<DocumentView> documentViews) {
        int fileReadCount = document.getFileReadCount();
        int fileTheftCount = document.getFileTheftCount();
        boolean updateAuthKey = document.isUpdateAuthKey();

        List<ViewerDTO> viewers = documentViews.stream()
                .map(view -> new ViewerDTO(view.getViewer().getId(), view.getViewer().getUsername(), view.getCreatedAt()))
                .sorted(Comparator.comparing(ViewerDTO::date).reversed())
                .collect(Collectors.toList());

        return new ReportResponseDTO(fileReadCount, fileTheftCount, updateAuthKey, viewers);
    }

    public record ViewerDTO(
            Long id,
            String name,
            Timestamp date
    ) {
    }
}
