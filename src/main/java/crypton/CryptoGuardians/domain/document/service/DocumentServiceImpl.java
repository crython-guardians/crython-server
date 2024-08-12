package crypton.CryptoGuardians.domain.document.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import crypton.CryptoGuardians.domain.document.dto.*;
import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.entity.DocumentKey;
import crypton.CryptoGuardians.domain.document.entity.DocumentShare;
import crypton.CryptoGuardians.domain.document.entity.DocumentView;
import crypton.CryptoGuardians.domain.document.repository.DocumentKeyRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentShareRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentViewRepository;
import crypton.CryptoGuardians.domain.user.entity.User;
import crypton.CryptoGuardians.domain.user.repository.UserRepository;
import crypton.CryptoGuardians.global.error.exception.Exception404;
import crypton.CryptoGuardians.global.error.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentKeyRepository documentKeyRepository;
    private final DocumentViewRepository documentViewRepository;
    private final DocumentShareRepository documentShareRepository;
    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Override
    public void saveFile(UploadRequestDTO uploadRequestDTO) {
        MultipartFile file = uploadRequestDTO.file();
        Long userId = uploadRequestDTO.uploadUserId();
        User uploadUser = userRepository.findById(userId).orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다"));
        String originalFilename = file.getOriginalFilename();

        // 파일명 앞에 UUID 추가
        String uuid = UUID.randomUUID().toString();
        String newFilename = uuid + "_" + originalFilename;

        try {
            BlobId blobId = BlobId.of(bucketName, newFilename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getBytes());

            // 파일 메타데이터 DB에 저장
            Document document = new Document(
                    newFilename,
                    formatFileSize(file.getSize()),
                    uploadUser,
                    false
            );

            // documentKey 설정
            document.setDocumentKey(DocumentKey.createDocumentKey());

            documentRepository.save(document);
        } catch (IOException e) {
            throw new Exception500("파일 업로드에 실패했습니다.");
        }
    }

    @Override
    public DownloadResponseDTO loadFileAsResource(Long documentId) {
        Document document = findById(documentId);

        try {
            Blob blob = storage.get(BlobId.of(bucketName, document.getFileName()));
            if (blob == null) {
                throw new Exception404("파일을 찾을 수 없습니다");
            }

            byte[] content = blob.getContent();
            // 파일명에서 UUID 부분 슬라이싱
            String originalFileName = document.getFileName().substring(document.getFileName().indexOf("_") + 1);
            Resource resource = new ByteArrayResource(content);

            return new DownloadResponseDTO(originalFileName, resource);
        } catch (Exception e) {
            throw new Exception404("파일을 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteFile(Long documentId) {
        Document document = findById(documentId);

        try {
            storage.delete(BlobId.of(bucketName, document.getFileName()));
            // DB에서 파일 메타 데이터 삭제
            documentRepository.deleteById(documentId);
        } catch (Exception e) {
            throw new Exception500("파일 삭제에 실패했습니다.");
        }
    }

    @Override
    public List<DocumentResponseDTO> getFilesByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new Exception404("유저를 찾을 수 없습니다.");
        }

        List<Document> documents = documentRepository.findByUploadUserIdOrderByCreatedAtAsc(userId);
        return documents.stream()
                .map(DocumentResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponseDTO> getFilesByUserAndKeyword(Long userId, String keyword) {
        if (!userRepository.existsById(userId)) {
            throw new Exception404("유저를 찾을 수 없습니다.");
        }

        List<Document> documents = documentRepository.searchDocumentOrderByCreatedAtAsc(userId, keyword);
        return documents.stream()
                .map(DocumentResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorizeResponseDTO getAuthorizeKey(Long documentId) {
        Document document = findById(documentId);
        return new AuthorizeResponseDTO(document.getDocumentKey().getAuthKey());
    }

    @Override
    public ReportResponseDTO getReport(Long documentId) {
        Document document = findById(documentId);
        List<DocumentView> documentViews = documentViewRepository.findAllByDocumentId(documentId);
        return ReportResponseDTO.from(document, documentViews);
    }

    @Override
    public void saveViewLog(Long documentId, ViewLogRequestDTO viewLogRequestDTO) {
        Document document = findById(documentId);
        User viewer = userRepository.findById(viewLogRequestDTO.viewerId()).orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));

        document.fileRead();
        documentRepository.save(document);

        DocumentView documentView = DocumentView.createDocumentView(document, viewer, viewLogRequestDTO.timestamp());
        documentViewRepository.save(documentView);
    }

    @Override
    public void fileShare(Long documentId, DocShareRequestDTO shareRequestDTO) {
        Document document = findById(documentId);
        Long sharedUserId = userRepository.findByUsername(shareRequestDTO.sharedUser()).orElseThrow(()
                -> new Exception404("유저를 찾을 수 없습니다")).getId();

        DocumentShare documentShare = new DocumentShare(sharedUserId, document);
        documentShareRepository.save(documentShare);
    }

    @Override
    public Document findById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() -> new Exception404("파일을 찾을 수 없습니다."));
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 KB";
        if (size < 1024) return "1 KB";

        final String[] units = new String[]{"KB", "MB"};
        int unitIndex = 0;
        double adjustedSize = size;

        // KB로 변환
        adjustedSize /= 1024.0;

        // 필요한 경우 MB로 변환
        if (adjustedSize >= 1024) {
            adjustedSize /= 1024.0;
            unitIndex = 1;
        }

        // 소수점 둘째자리까지 반올림
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(adjustedSize) + " " + units[unitIndex];
    }
}
